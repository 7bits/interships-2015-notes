CREATE TABLE usernotes (
	user_id BIGINT REFERENCES users(id),
	note_id BIGINT REFERENCES notes(id)
);