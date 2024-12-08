-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE region_news (
     id BIGSERIAL PRIMARY KEY,
     title VARCHAR(255) NOT NULL,
     content TEXT NOT NULL,
     preview_image_path VARCHAR(255),
     created_at TIMESTAMP,
     region_id BIGINT NOT NULL,
     CONSTRAINT fk_region_news_region FOREIGN KEY (region_id) REFERENCES regions(id)
);
--rollback DROP TABLE region_news;