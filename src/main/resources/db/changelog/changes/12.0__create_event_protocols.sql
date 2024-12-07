-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE IF NOT EXISTS event_protocols (
     id BIGSERIAL PRIMARY KEY,
     original_file_name VARCHAR(255) NOT NULL,
     stored_file_name VARCHAR(255) NOT NULL,
     file_path VARCHAR(1024) NOT NULL,
     content_type VARCHAR(255) NOT NULL,
     file_size BIGINT NOT NULL,
     created_at TIMESTAMP,
     event_base_id BIGINT REFERENCES event_base(id),
     region_id BIGINT REFERENCES regions(id)
);
--rollback DROP TABLE event_protocols;