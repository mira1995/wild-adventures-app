CREATE SCHEMA waimage;

CREATE TABLE waimage.image_type (
                code VARCHAR(3) NOT NULL,
                name VARCHAR NOT NULL,
                CONSTRAINT image_type_pk PRIMARY KEY (code)
);


CREATE SEQUENCE waimage.image_image_id_seq;

CREATE TABLE waimage.image (
                image_id INTEGER NOT NULL DEFAULT nextval('waimage.image_image_id_seq'),
                alt VARCHAR NOT NULL,
                description VARCHAR NOT NULL,
                uri VARCHAR NOT NULL,
                type_code VARCHAR(3) NOT NULL,
                CONSTRAINT image_pk PRIMARY KEY (image_id)
);


ALTER SEQUENCE waimage.image_image_id_seq OWNED BY waimage.image.image_id;

CREATE UNIQUE INDEX image_idx
 ON waimage.image
 ( uri );

CREATE TABLE waimage.image_adventure (
                adventure_id INTEGER NOT NULL,
                image_id INTEGER NOT NULL,
                CONSTRAINT image_adventure_pk PRIMARY KEY (adventure_id, image_id)
);


CREATE TABLE waimage.image_category (
                category_id INTEGER NOT NULL,
                image_id INTEGER NOT NULL,
                CONSTRAINT image_category_pk PRIMARY KEY (category_id, image_id)
);


CREATE TABLE waimage.image_useraccount (
                image_id INTEGER NOT NULL,
                CONSTRAINT image_useraccount_pk PRIMARY KEY (image_id)
);


ALTER TABLE waimage.image ADD CONSTRAINT image_type_image_fk
FOREIGN KEY (type_code)
REFERENCES waimage.image_type (code)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE waimage.image_useraccount ADD CONSTRAINT image_image_useraccount_fk
FOREIGN KEY (image_id)
REFERENCES waimage.image (image_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE waimage.image_category ADD CONSTRAINT image_image_category_fk
FOREIGN KEY (image_id)
REFERENCES waimage.image (image_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE waimage.image_adventure ADD CONSTRAINT image_image_adventure_fk
FOREIGN KEY (image_id)
REFERENCES waimage.image (image_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;