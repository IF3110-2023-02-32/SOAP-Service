CREATE TABLE IF NOT EXISTS `logging` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `ip` varchar(16) NOT NULL,
  `endpoint` varchar(255) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `unlocking` (
  `socmed_id` int NOT NULL,
  `dashboard_id` int DEFAULT NULL,
  `link_code` varchar(8) NOT NULL UNIQUE,
  PRIMARY KEY (`socmed_id`)
);

CREATE TABLE IF NOT EXISTS `apikey` (
  `id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(255) NOT NULL UNIQUE,
  `client` varchar(255) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

INSERT INTO apikey(`key`, `client`) VALUES ('abcdefgh12345678', 'socmed_app'),
('ijklmnop12345678', 'dashboard_app');