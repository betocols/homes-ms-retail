CREATE TABLE store (
  store_id  BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'The id of the store',
  name      VARCHAR(50) NOT NULL COMMENT 'The name of the store',
  PRIMARY KEY (store_id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1;
