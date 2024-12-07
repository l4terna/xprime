-- liquibase formatted sql

--changeset laterna:1
CREATE TABLE notifications (
   id BIGSERIAL PRIMARY KEY,
   recipient_id BIGINT REFERENCES users(id) NOT NULL,
   content TEXT NOT NULL,
   type VARCHAR(50) NOT NULL,
   read BOOLEAN DEFAULT FALSE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
--rollback DROP TABLE notifications;

--changeset laterna:2
ALTER TABLE notifications
ALTER COLUMN recipient_id DROP NOT NULL;