ALTER TABLE usernotes DROP CONSTRAINT usernotes_user_id_fkey;

ALTER TABLE usernotes ADD CONSTRAINT usernotes_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;