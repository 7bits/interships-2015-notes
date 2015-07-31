ALTER TABLE users
    ADD COLUMN token_at TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL,
    ADD COLUMN token VARCHAR(40) NOT NULL DEFAULT md5(random()::text);
CREATE INDEX token_idx ON users (token);
CREATE UNIQUE INDEX email_idx ON users (email);