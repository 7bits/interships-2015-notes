ALTER TABLE usernotes DROP CONSTRAINT usernotes_note_id_fkey;

ALTER TABLE usernotes ADD FOREIGN KEY (note_id) REFERENCES notes (id) ON DELETE CASCADE;