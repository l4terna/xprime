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

-- changeset laterna:2
ALTER TABLE regions ADD COLUMN description TEXT;
--rollback ALTER TABLE regions DROP COLUMN description;

-- changeset laterna:3
ALTER TABLE regions ADD COLUMN image_url VARCHAR(255);
--rollback ALTER TABLE regions DROP COLUMN image_url;

-- changeset laterna:4
ALTER TABLE regions ADD COLUMN federal_district VARCHAR(255);
--rollback ALTER TABLE regions DROP COLUMN federal_district;