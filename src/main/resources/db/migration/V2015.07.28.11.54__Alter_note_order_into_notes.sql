CREATE SEQUENCE note_order_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;

ALTER TABLE notes ADD COLUMN note_order REAL DEFAULT nextval('note_order_seq') NOT NULL;

