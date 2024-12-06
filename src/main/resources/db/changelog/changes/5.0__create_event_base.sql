-- 5.0__create_event_base.sql
-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE IF NOT EXISTS event_base(
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    min_age INTEGER NOT NULL,
    max_age INTEGER NOT NULL,
    location VARCHAR(255) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    max_participants INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    region_id BIGINT REFERENCES regions(id) NOT NULL
);
--rollback DROP TABLE event_base;
