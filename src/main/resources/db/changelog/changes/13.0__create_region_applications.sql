-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE IF NOT EXISTS region_applications (
     id BIGSERIAL PRIMARY KEY,
     region_id BIGINT REFERENCES regions(id) NOT NULL,
     applicant_id BIGINT REFERENCES users(id) NOT NULL,
     title VARCHAR(255) NOT NULL,
     description TEXT NOT NULL,
     status VARCHAR(20) NOT NULL,
     created_at TIMESTAMP NOT NULL,
     response_message TEXT
);
--rollback DROP TABLE region_applications;