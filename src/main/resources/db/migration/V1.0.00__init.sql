CREATE TABLE store (
  store_id      BIGINT          NOT NULL AUTO_INCREMENT COMMENT 'Id of the store',
  name          VARCHAR(255)    NOT NULL COMMENT 'Name of the store',
  PRIMARY KEY (store_id)
);

CREATE TABLE product (
  product_id    BIGINT          NOT NULL AUTO_INCREMENT COMMENT 'Id of the product',
  store_id      BIGINT          NOT NULL                COMMENT 'Id of the associated store',
  name          VARCHAR(255)    NOT NULL                COMMENT 'Name of the product',
  description   VARCHAR(255)    NOT NULL                COMMENT 'Description of the product',
  sku           VARCHAR(10)     NOT NULL                COMMENT 'Product SKU code',
  price         DECIMAL(15, 2)  NOT NULL                COMMENT 'Price of the product',
  FOREIGN KEY (store_id) REFERENCES store (store_id),
  PRIMARY KEY (product_id)
);

CREATE TABLE stock (
  product_id    BIGINT          NOT NULL COMMENT 'Id of the associated product',
  store_id      BIGINT          NOT NULL COMMENT 'Id of the associated store',
  count         BIGINT          NOT NULL COMMENT 'Amount of product in store',
  PRIMARY KEY (product_id),
  FOREIGN KEY (store_id) REFERENCES store (store_id),
  FOREIGN KEY (product_id) REFERENCES product (product_id)
);

CREATE TABLE order_complete (
    order_id     BIGINT     NOT NULL AUTO_INCREMENT     COMMENT 'Id of the order',
    store_id     BIGINT     NOT NULL                    COMMENT 'If of the associated store',
    order_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP   COMMENT 'Date of the order',
    status       INTEGER                                COMMENT 'Status of the order',
    first_name   VARCHAR(50)                            COMMENT 'First name of the buyer',
    last_name    VARCHAR(50)                            COMMENT 'Last name of the buyer',
    email        VARCHAR(255)                           COMMENT 'Email of the buyer',
    phone        VARCHAR(25)                            COMMENT 'Phone number of the buyer',
    PRIMARY KEY (order_id),
    FOREIGN KEY (store_id) REFERENCES store (store_id)
);

CREATE TABLE order_item (
    order_item_id           BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'Id of the order item',
    order_id                BIGINT NOT NULL                 COMMENT 'Id of the associated order',
    product_id              BIGINT NOT NULL                 COMMENT 'Id of the associated product',
    count                   INTEGER NOT NULL                COMMENT 'Amount of product requested',
    PRIMARY KEY (order_item_id),
    FOREIGN KEY (order_id) REFERENCES order_complete (order_id),
    FOREIGN KEY (product_id) REFERENCES product (product_id)
)
