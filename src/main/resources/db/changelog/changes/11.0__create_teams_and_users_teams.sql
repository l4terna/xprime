
-- liquibase formatted sql
-- changeset laterna:1
CREATE TABLE teams (
   id BIGSERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   description TEXT,
   region_id BIGINT NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT fk_region FOREIGN KEY (region_id) REFERENCES regions(id)
);

-- changeset laterna:2
CREATE TABLE users_teams (
     user_id BIGINT NOT NULL,
     team_id BIGINT NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     CONSTRAINT pk_users_teams PRIMARY KEY (user_id, team_id),
     CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
     CONSTRAINT fk_team FOREIGN KEY (team_id) REFERENCES teams(id)
);