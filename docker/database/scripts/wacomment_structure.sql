CREATE SCHEMA wacomment;

CREATE SEQUENCE wacomment.comment_id_seq;

CREATE TABLE wacomment.comment (
                id INTEGER NOT NULL DEFAULT nextval('wacomment.comment_id_seq'),
                content VARCHAR(1000) NOT NULL,
                parent_id INTEGER,
                CONSTRAINT comment_pk PRIMARY KEY (id)
);


ALTER SEQUENCE wacomment.comment_id_seq OWNED BY wacomment.comment.id;

ALTER TABLE wacomment.comment ADD CONSTRAINT comment_comment_fk
FOREIGN KEY (parent_id)
REFERENCES wacomment.comment (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;