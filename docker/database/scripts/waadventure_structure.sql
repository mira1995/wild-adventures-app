CREATE SCHEMA waadventure;

CREATE SEQUENCE waadventure.adventure_id_seq;

CREATE TABLE waadventure.adventure (
                id INTEGER NOT NULL DEFAULT nextval('waadventure.adventure_id_seq'),
                title VARCHAR(250) NOT NULL,
                description VARCHAR(1000) NOT NULL,
                status VARCHAR(20) NOT NULL,
                location VARCHAR(100) NOT NULL,
                CONSTRAINT adventure_pk PRIMARY KEY (id)
);


ALTER SEQUENCE waadventure.adventure_id_seq OWNED BY waadventure.adventure.id;

CREATE SEQUENCE waadventure.session_id_seq;

CREATE TABLE waadventure.session (
                id INTEGER NOT NULL DEFAULT nextval('waadventure.session_id_seq'),
                adventure_id INTEGER NOT NULL,
                start_date TIMESTAMP(0) NOT NULL,
                end_date TIMESTAMP(0) NOT NULL,
                price NUMERIC(4,2) NOT NULL,
                CONSTRAINT session_pk PRIMARY KEY (id)
);


ALTER SEQUENCE waadventure.session_id_seq OWNED BY waadventure.session.id;

ALTER TABLE waadventure.session ADD CONSTRAINT adventure_session_fk
FOREIGN KEY (adventure_id)
REFERENCES waadventure.adventure (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;