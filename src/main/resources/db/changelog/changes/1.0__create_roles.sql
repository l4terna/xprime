
-- liquibase formatted sql

-- changeset author:initial-roles
CREATE TABLE roles (
   id BIGSERIAL PRIMARY KEY,
   name VARCHAR(50) NOT NULL UNIQUE
);
--rollback DROP TABLE roles;