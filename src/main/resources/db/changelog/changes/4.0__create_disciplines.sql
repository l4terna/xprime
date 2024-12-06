-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE IF NOT EXISTS disciplines(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL
);
--rollback DROP TABLE disciplines;