CREATE TABLE IF NOT EXISTS  `account_spamers` (
  `account` varchar(255) NOT NULL,
  `expire` int(11) NOT NULL,
  PRIMARY KEY (`account`)
);