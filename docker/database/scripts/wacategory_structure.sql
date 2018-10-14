CREATE SCHEMA wacategory;

CREATE SEQUENCE wacategory.category_id_seq;

CREATE TABLE wacategory.category (
                id INTEGER NOT NULL DEFAULT nextval('wacategory.category_id_seq'),
                title VARCHAR(250) NOT NULL,
                description VARCHAR(1000) NOT NULL,
                CONSTRAINT category_pk PRIMARY KEY (id)
);


ALTER SEQUENCE wacategory.category_id_seq OWNED BY wacategory.category.id;

CREATE TABLE wacategory.category_adventure (
                category_id INTEGER NOT NULL,
                adventure_id INTEGER NOT NULL,
                CONSTRAINT category_adventure_pk PRIMARY KEY (category_id, adventure_id)
);


ALTER TABLE wacategory.category_adventure ADD CONSTRAINT category_category_adventure_fk
FOREIGN KEY (category_id)
REFERENCES wacategory.category (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;