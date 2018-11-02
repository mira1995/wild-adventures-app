CREATE SCHEMA waorder;

CREATE SEQUENCE waorder.orders_id_seq;

CREATE TABLE waorder.orders (
                id INTEGER NOT NULL DEFAULT nextval('waorder.orders_id_seq'),
                order_date TIMESTAMP NOT NULL,
                status VARCHAR NOT NULL,
                is_paid BOOLEAN NOT NULL,
                useraccount_id INTEGER NOT NULL,
                CONSTRAINT orders_pk PRIMARY KEY (id)
);


ALTER SEQUENCE waorder.orders_id_seq OWNED BY waorder.orders.id;

CREATE SEQUENCE waorder.order_id_seq;

CREATE TABLE waorder.order_demand (
                id INTEGER NOT NULL DEFAULT nextval('waorder.order_id_seq'),
                order_date TIMESTAMP NOT NULL,
                status VARCHAR NOT NULL,
                is_paid BOOLEAN NOT NULL,
                useraccount_id INTEGER NOT NULL,
                order_id INTEGER NOT NULL,
                demand_type VARCHAR NOT NULL,
                demand_message VARCHAR NOT NULL,
                answer_message VARCHAR,
                CONSTRAINT order_demand_pk PRIMARY KEY (id)
);


ALTER SEQUENCE waorder.order_id_seq OWNED BY waorder.order_demand.id;

CREATE TABLE waorder.order_demand_session (
                demand_id INTEGER NOT NULL,
                session_id INTEGER NOT NULL,
                CONSTRAINT order_demand_session_pk PRIMARY KEY (demand_id, session_id)
);


CREATE TABLE waorder.order_session (
                order_id INTEGER NOT NULL,
                session_id INTEGER NOT NULL,
                CONSTRAINT order_session_pk PRIMARY KEY (order_id, session_id)
);


ALTER TABLE waorder.order_session ADD CONSTRAINT order_order_session_fk
FOREIGN KEY (order_id)
REFERENCES waorder.orders (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE waorder.order_demand ADD CONSTRAINT order_order_demand_fk
FOREIGN KEY (order_id)
REFERENCES waorder.orders (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE waorder.order_demand_session ADD CONSTRAINT order_demand_order_demand_session_fk
FOREIGN KEY (demand_id)
REFERENCES waorder.order_demand (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;