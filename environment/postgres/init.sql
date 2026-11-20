-- keycloak
create database keycloak;

\c keycloak

create role keycloak;

grant connect on database keycloak to keycloak;
grant all privileges on schema public to keycloak;
alter default privileges in schema public grant all on tables to keycloak;
alter default privileges in schema public grant all on sequences to keycloak;
alter default privileges in schema public grant all on functions to keycloak;

create user keycloak_user with password 'keycloak_user';

grant keycloak to keycloak_user;

-- payment app
create database payment;

\c payment

create schema payment;

create role payment_app;

grant connect on database payment to payment_app;
grant usage, create on schema payment to payment_app;

create user payment_user with password 'payment_user';

grant payment_app to payment_user;