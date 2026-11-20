ENV_FILE := environment/.env

ifneq (,$(wildcard $(ENV_FILE)))
    include $(ENV_FILE)
    export
endif

# OS detection
ifeq ($(OS),Windows_NT)
    GRADLE := gradlew.bat
else
    UNAME_S := $(shell uname -s)
    GRADLE := ./gradlew
endif

# Docker configuration
COMPOSE := docker compose
COMPOSE_FILE := environment/docker-compose.yaml
PROJECT := payment-processor
COMPOSE_ARGS := --env-file $(ENV_FILE) -f $(COMPOSE_FILE) -p $(PROJECT)

.DEFAULT_GOAL := run

.PHONY: up down restart gradle-build boot-run run

# Docker commands

up: ## Start Docker containers
	$(COMPOSE) $(COMPOSE_ARGS) up -d

down: ## Stop and remove containers and networks
	$(COMPOSE) $(COMPOSE_ARGS) down

restart: ## Restart all containers
	$(COMPOSE) $(COMPOSE_ARGS) restart

# Gradle commands

gradle-build: ## Build Spring Boot application
	$(GRADLE) clean build -x test

boot-run: ## Run Spring Boot application
	$(GRADLE) bootRun

# Combined commands

run: ## Build application, start dependencies and run Spring Boot
	$(MAKE) gradle-build
	$(MAKE) up
	$(MAKE) boot-run

stop: ## Stop application
	$(MAKE) down