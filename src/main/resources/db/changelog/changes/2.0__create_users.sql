-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR NOT NULL,
    lastname VARCHAR NOT NULL,
    patronymic VARCHAR NOT NULL,
    role_id BIGINT REFERENCES roles(id) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
--rollback DROP TABLE users;

-- changeset laterna:2
ALTER TABLE users
ADD COLUMN IF NOT EXISTS email_verified BOOLEAN DEFAULT FALSE;

--rollback ALTER TABLE users DROP COLUMN IF EXISTS email_verified;