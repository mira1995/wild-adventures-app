CREATE SCHEMA IF NOT EXISTS waimage;

CREATE TABLE waimage.image_type (
                code VARCHAR(3) NOT NULL,
                name VARCHAR NOT NULL,
                CONSTRAINT image_type_pk PRIMARY KEY (code)
);


CREATE SEQUENCE waimage.image_image_id_seq;