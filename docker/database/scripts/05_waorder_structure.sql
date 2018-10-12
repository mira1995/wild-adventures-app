CREATE SCHEMA waorder;


CREATE SEQUENCE waorder.order_id_seq;

CREATE TABLE waorder.order (
                id INTEGER NOT NULL DEFAULT nextval('waorder.order_id_seq'),
                order_date TIMESTAMP NOT NULL,
                status VARCHAR NOT NULL,
                is_paid BOOLEAN NOT NULL,
                useraccount_id VARCHAR NOT NULL,
                CONSTRAINT order_pk PRIMARY KEY (id)
);


ALTER SEQUENCE waorder.order_id_seq OWNED BY waorder.order.id;

CREATE TABLE waorder.order_session (
                order_id INTEGER NOT NULL,
                session_id INTEGER NOT NULL,
                CONSTRAINT order_session_pk PRIMARY KEY (order_id, session_id)
);


ALTER TABLE waorder.order_session ADD CONSTRAINT order_order_session_fk
FOREIGN KEY (order_id)
REFERENCES waorder.order (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;