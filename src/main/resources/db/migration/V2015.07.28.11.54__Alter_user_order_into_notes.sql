CREATE SEQUENCE user_order_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

ALTER TABLE notes ADD COLUMN user_order REAL DEFAULT nextval('user_order_seq'::regclass);

