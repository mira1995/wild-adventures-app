CREATE SCHEMA wacategory;

CREATE SEQUENCE wacategory.category_id_seq;

CREATE TABLE wacategory.category (
                id INTEGER NOT NULL DEFAULT nextval('wacategory.category_id_seq'),
                title VARCHAR(250) NOT NULL,
                description VARCHAR(1000) NOT NULL,
                CONSTRAINT category_pk PRIMARY KEY (id)
);


ALTER SEQUENCE wacategory.category_id_seq OWNED BY wacategory.category.id;