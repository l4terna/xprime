-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE IF NOT EXISTS regions (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     user_id BIGINT REFERENCES users(id),
     created_at TIMESTAMP NOT NULL,
     contact_email VARCHAR(255)
);
--rollback DROP TABLE regions;
