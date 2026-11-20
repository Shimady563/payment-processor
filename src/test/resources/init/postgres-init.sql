create schema test;

create role test_role;

grant connect on database test to test_role;
grant usage, create on schema test to test_role;

grant test_role to test;