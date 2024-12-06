-- liquibase formatted sql

-- changeset laterna:1
INSERT INTO roles (name) VALUES
('FSP_ADMIN'),
('REGION_ADMIN'),
('USER');