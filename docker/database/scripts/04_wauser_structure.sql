CREATE SCHEMA wauser;

CREATE SEQUENCE wauser.useraccount_id_seq;

CREATE TABLE wauser.useraccount (
                id INTEGER NOT NULL DEFAULT nextval('wauser.useraccount_id_seq'),
                pseudo VARCHAR(50) NOT NULL,
                password VARCHAR NOT NULL,
                email VARCHAR NOT NULL,
                firstname VARCHAR(50) NOT NULL,
                lastname VARCHAR(50) NOT NULL,
                address VARCHAR NOT NULL,
                postal_code INTEGER NOT NULL,
                city VARCHAR NOT NULL,
                country VARCHAR NOT NULL,
                phone_number VARCHAR(40) NOT NULL,
                birth_date DATE NOT NULL,
                profile_image_id INTEGER,
                active BOOLEAN NOT NULL,
                role VARCHAR(5) NOT NULL,
                CONSTRAINT useraccount_pk PRIMARY KEY (id)
);


ALTER SEQUENCE wauser.useraccount_id_seq OWNED BY wauser.useraccount.id;

CREATE UNIQUE INDEX useraccount_idx
 ON wauser.useraccount
 ( pseudo, email );