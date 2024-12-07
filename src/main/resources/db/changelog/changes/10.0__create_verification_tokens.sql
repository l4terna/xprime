
-- liquibase formatted sql

-- changeset author:email-verification:1
CREATE TABLE verification_tokens (
     id BIGSERIAL PRIMARY KEY,
     token VARCHAR(255) NOT NULL UNIQUE,
     user_id BIGINT NOT NULL,
     expiry_date TIMESTAMP NOT NULL
);

--rollback DROP TABLE IF EXISTS verification_tokens;