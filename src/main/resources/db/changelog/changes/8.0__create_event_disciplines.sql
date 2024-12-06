-- 8.0__create_event_disciplines.sql
-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE IF NOT EXISTS event_disciplines(
    event_base_id BIGINT REFERENCES event_base(id),
    discipline_id BIGINT REFERENCES disciplines(id),
    PRIMARY KEY (event_base_id, discipline_id)
);
--rollback DROP TABLE event_disciplines;