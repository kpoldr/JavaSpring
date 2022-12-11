
DROP TABLE IF EXISTS order_rows;
DROP TABLE IF EXISTS orders;
DROP SEQUENCE IF EXISTS  order_row_seq;
DROP SEQUENCE IF EXISTS order_seq;


CREATE SEQUENCE order_seq START WITH 1;
CREATE SEQUENCE order_row_seq START WITH 1;


CREATE TABLE orders (
                        order_id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('order_seq'),
                        order_number VARCHAR(255) NOT NULL
);

CREATE TABLE order_rows (
                           order_row_id INT NOT NULL PRIMARY KEY DEFAULT nextval('order_row_seq'),
                           order_id BIGINT NOT NULL REFERENCES orders ON DELETE CASCADE,
                           item_name VARCHAR(255) NOT NULL,
                           quantity INT NOT NULL,
                           price DOUBLE PRECISION NOT NULL
--                            CONSTRAINT fk_orders FOREIGN KEY(order_id) REFERENCES orders(order_id)
);