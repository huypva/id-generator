CREATE TABLE config (
  id enum('1') NOT NULL DEFAULT '1' COMMENT 'Exactly one row in table' PRIMARY KEY,
  date_bits  TINYINT NOT NULL,
  worker_id_bits TINYINT NOT NULL,
  sequence_bits TINYINT NOT NULL,
  epoch_date DATE NOT NULL
)ENGINE = INNODB;
INSERT INTO config(date_bits, worker_id_bits, sequence_bits, epoch_date) VALUES (15, 28, 20, '2022-01-01');

CREATE TABLE worker (
  id enum('1') NOT NULL DEFAULT '1' COMMENT 'Exactly one row in table' PRIMARY KEY,
  last_date DATE NOT NULL,
  worker_id BIGINT NOT NULL
) ENGINE = INNODB;
INSERT INTO worker(last_date, worker_id) VALUES (now(), 0);



