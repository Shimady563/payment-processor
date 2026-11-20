# Payment Processor

Spring Boot service that receives task from Kafka and processes them using workers.

## Requirements

- JDK 25 (the Gradle toolchain requests Java 25)
- Docker Engine with Compose
- GNU Make

On Windows, `make` selects `gradlew.bat`. On other systems it selects `./gradlew`.

## Run the service

From the repository root:

```bash
make
```

`make` is `make run`. It does three things:

1. `builds project using gradle build`.
2. `creaates local environment using docker compose`.
3. `runs an application using gradle spring boot plugin`.

Other targets:

| Command             | Effect                                                                          |
|---------------------|---------------------------------------------------------------------------------|
| `make up`           | Start Compose services in the background                                        |
| `make down`         | Stop and remove containers and the Compose network. The Postgres volume is kept |
| `make restart`      | Restart running containers                                                      |
| `make gradle-build` | `clean build`, tests excluded                                                   |
| `make boot-run`     | Run the application when dependencies are already up                            |
| `make stop`         | Same as `make down`                                                             |

URLs once `bootRun` is listening:

| URL                                          | Purpose                                                                                        |
|----------------------------------------------|------------------------------------------------------------------------------------------------|
| http://localhost:8080/api/v1/swagger-ui.html | OpenAPI UI                                                                                     |
| http://localhost:8080/api/v1/actuator/health | Health                                                                                         |
| http://localhost:8081                        | Keycloak (admin console: `keycloak_admin` / `keycloak_admin`)                                  |
| `localhost:5433`                             | PostgreSQL (`payment` / `payment_user` / `payment_user`, or superuser `postgres` / `postgres`) |

## What the service does

- Serves a stateless REST API under `/api/v1` with API for Tasks and Users management.
- Uses Keycloak OAuth2 resource server for stateless JWT based authentication and authorization.
- Publishes an OpenAPI UI and exposes Actuator health, info, and metrics.
- Polls Kafka messages with Tasks and saves them locally.
- Stores Tasks in PostgreSQL database.
- Runs tasks concurrently using worker pool and maintains their state in database.

## Decisions

**Keycloak is the identity provider.** The application uses Keycloak resource server for users maintenance. It provides
stateless JWT based authentication flow out of the box. The stateless approach allows for easy service use in
microservice architecture. Keycloak was chosen to practice resource server integration with Spring Boot app.

**Default container configs.** For local use, the default `payment` realm and databases are created on corresponding
docker container startup. Default keycloak realm contains `payment-processor` client and two users: `payement-admin`
with corresponding admin realm and client roles, 'payment-user' with user roles. Postgres init script contains creation
of separate databases for Keycloak (`keycloak`) and Application (`payment`). For both apps there is also creation of:
specific role with minimal permissions, specific user with newly created role.

**Modular application properties structure.** Application properties are divided into several files and then imported
into an application.yaml file. This modular structure helps to group similar properties and separate them into distinct
files for ease of use and editing.

**Permissions are an application enum.** `security.jwt.auth.converter.roles-to-permissions` maps Keycloak role strings
onto `UserPermissions`. Controllers authorize against those permission names.

## Configuration

It is possible to specify runtime configuration properties using `environment/.env`. Those values will be imported
automatically when running project using Makefile. This type of configuration is also very convenient for running
applications in production using Kubernetes, especially with Helm.
The passwords in `.env` and in the Keycloak realm import are local development credentials.

### Versions

| Variable           | Default     | Used for              |
|--------------------|-------------|-----------------------|
| `POSTGRES_VERSION` | `17-alpine` | Postgres image        |
| `KEYCLOAK_VERSION` | `26.8`      | Keycloak image        |
| `KAFKA_VERSION`    | `8.1.0`     | Confluent Kafka image |

### Database

| Variable                             | Default          | Used for                                          |
|--------------------------------------|------------------|---------------------------------------------------|
| `DB_URL`                             | `localhost:5433` | JDBC host and port                                |
| `DB_NAME`                            | `payment`        | Application database                              |
| `DB_SCHEMA`                          | `payment`        | Application schema                                |
| `DB_USERNAME`                        | `payment_user`   | Application login                                 |
| `DB_PASSWORD`                        | `payment_user`   | Application password                              |
| `DB_HIKARI_RE_WRITE_BATCHED_INSERTS` | `true`           | Hikari datasource 'reWriteBachedInserts' property |
| `DB_HIBERNATE_ORDER_INSERTS`         | `true`           | Hibernate 'order_inserts' property                |
| `DB_HIBERNATE_ORDER_UPDATES`         | `true`           | Hibernate 'order_updates' property                |
| `DB_HIBERNATE_BATCH_VERSIONED_DATA`  | `true`           | Hibernate 'batch_versioned_data' property         |
| `DB_JDBC_BATCH_SIZE`                 | `50`             | Jdbc default batch size                           |
| `DB_LIQUIBASE_ENABLED`               | `true`           | Liquibase feature flag                            |

### Security

| Variable                                       | Default                                | Used for                                   |
|------------------------------------------------|----------------------------------------|--------------------------------------------|
| `SECURITY_KEYCLOAK_URL`                        | `http://localhost:8081/realms/payment` | JWT issuer                                 |
| `SECURITY_KEYCLOAK_CLIENT_RESOURCE_ID`         | `payment-processor`                    | Client whose roles are read from the token |
| `SECURITY_KEYCLOAK_CLIENT_PRINCIPAL_ATTRIBUTE` | `preferred_username`                   | Claim used as the username                 |
| `SECURITY_WHITELIST`                           | Swagger and Actuator paths             | Unauthenticated paths                      |
| `SECURITY_ALLOWED_ORIGINS`                     | `http://localhost:8080`                | CORS origins                               |

### Apidocs

| Variable                              | Default            | Used for                        |
|---------------------------------------|--------------------|---------------------------------|
| `APIDOCS_ENABLED`                     | `true`             | Apidocs feature flag            |
| `APIDOCS_SWAGGER_ENABLED`             | `true`             | Apidocs swagger ui feature flag |
| `APIDOCS_SWAGGER_DISABLE_DEFAULT_URL` | `true`             | Swagger default url switch      |
| `APIDOCS_SWAGGER_PATH`                | `/swagger-ui.html` | Apidocs swagger ui path         |

### General

| Variable                     | Default               | Used for                                  |
|------------------------------|-----------------------|-------------------------------------------|
| `SERVER_PORT`                | `8080`                | HTTP port                                 |
| `SERVER_BASEPATH`            | `/api/v1`             | Servlet context path                      |
| `PROFILES_INCLUDE`           | `local`               | Extra Spring profile                      |
| `LOGGING_LEVEL_BASEPACKAGE`  | `debug`               | Logging level for project package classes |
| `ACTUATOR_ENDPOINTS_INCLUDE` | `health,info,metrics` | Actuator ednpoints to include in exposure |

## Custom properties

| Property                                           | Default                                            | Purpose                                                                   |
|----------------------------------------------------|----------------------------------------------------|---------------------------------------------------------------------------|
| `security.whitelist`                               | Swagger and Actuator paths                         | Paths to add to security whitelist                                        |
| `security.allowed-origins`                         | `http://localhost:8080`                            | Origin that are allowed by CORS configuration                             |
| `security.jwt.auth.converter.resource-id`          | `payment-processor`                                | Keycloak resource server client id                                        |
| `security.jwt.auth.converter.roles-to-permissions` | Map of default Keycloak roles to `UserPermisssion` | Mapping of Keycloak client roles to permissions understood by application |
| `security.jwt.auth.converter.principal-attribute`  | `preferred_username`                               | Perfereed principal attribute in Jwt                                      |

## Call the API

Password grant against the public client:

```bash
curl -s -X POST "http://localhost:8081/realms/payment/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=payment-processor" \
  -d "username=user" \
  -d "password=user-password"
```

Seeded users in the `payment` realm:

| Username | Password         | Client role | Permission       |
|----------|------------------|-------------|------------------|
| `user`   | `user-password`  | `user`      | `READ_USER_INFO` |
| `admin`  | `admin-password` | `admin`     | `READ_USER_INFO` |

A missing or invalid token returns `401` with `{"message": "...", "code": 401}`. An authenticated caller without
`READ_USER_INFO` returns `403` with the same shape. Swagger UI sends the bearer token through the **JWT** scheme.

## Stack

| Piece       | Version / choice                |
|-------------|---------------------------------|
| Java        | 25                              |
| Gradle      | 9.8.2                           |
| Spring Boot | 4.1.2                           |
| Web         | MVC stack                       |
| Security    | Ketcloak OAuth2 resource server |
| Persistence | Spring Data JPA, PostgreSQL     |
| Mapping     | MapStruct 1.6.3                 |
| API docs    | springdoc 3.2.0                 |
| Messaging   | `spring-boot-starter-kafka`     |
| Tests       | JUnit 5, Testcontainers         |
