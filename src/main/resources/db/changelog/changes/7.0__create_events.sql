-- 7.0__create_events.sql
-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE IF NOT EXISTS events(
     id BIGSERIAL PRIMARY KEY,
     base_id BIGINT REFERENCES event_base(id) NOT NULL,
    request_id BIGINT REFERENCES event_requests(id)
);
--rollback DROP TABLE events;