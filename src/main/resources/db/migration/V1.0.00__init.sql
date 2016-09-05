CREATE TABLE store (
  store_id      BIGINT          NOT NULL AUTO_INCREMENT COMMENT 'The id of the store',
  name          VARCHAR(255)    NOT NULL COMMENT 'The name of the store',
  PRIMARY KEY (store_id)
);

CREATE TABLE product (
  product_id    BIGINT          NOT NULL AUTO_INCREMENT COMMENT 'The id of the product',
  store_id      BIGINT          NOT NULL,
  name          VARCHAR(255)    NOT NULL COMMENT 'The name of the product',
  description   VARCHAR(255)    NOT NULL COMMENT 'The description of the product',
  sku           VARCHAR(10)     NOT NULL COMMENT 'The product SKU code',
  price         DECIMAL(15, 2)  NOT NULL COMMENT 'The price of the product',
  FOREIGN KEY (store_id) REFERENCES store (store_id),
  PRIMARY KEY (product_id)
);
