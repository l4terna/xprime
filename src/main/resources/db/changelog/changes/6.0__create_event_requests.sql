-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE IF NOT EXISTS event_requests(
     id BIGSERIAL PRIMARY KEY,
     base_id BIGINT REFERENCES event_base(id) NOT NULL,
    status VARCHAR(30) NOT NULL,
    moderation_comment TEXT NOT NULL
    );
--rollback DROP TABLE event_requests;

-- changeset laterna:2
    ALTER TABLE event_requests
    ALTER COLUMN moderation_comment DROP NOT NULL;
--rollback ALTER TABLE event_requests ALTER COLUMN moderation_comment SET NOT NULL;
