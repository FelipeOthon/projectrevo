DROP TABLE IF EXISTS `account_variables`;
CREATE TABLE `account_variables` (
  `account_name` VARCHAR(45) NOT NULL DEFAULT '',
  `var`  VARCHAR(50) NOT NULL DEFAULT '',
  `value` VARCHAR(255) ,
  PRIMARY KEY (`account_name`,`var`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

REPLACE INTO installed_updates (`file_name`) VALUES ("2018_10_13 20-58");