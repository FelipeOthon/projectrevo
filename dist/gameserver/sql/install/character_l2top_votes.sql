CREATE TABLE IF NOT EXISTS `character_l2top_votes` (
  `date` bigint(14) NOT NULL DEFAULT '0',
  `id` int(10) NOT NULL DEFAULT '0',
  `nick` varchar(255) NOT NULL DEFAULT '',
  `multipler` int(9) NOT NULL DEFAULT '0',
  `has_reward` int(1) NOT NULL DEFAULT '0',
  `sms` int(1) NOT NULL DEFAULT '0',
  `fixDate` VARCHAR(15) NOT NULL DEFAULT '',
  `account` VARCHAR(45) NOT NULL DEFAULT '',
  `HWID` VARCHAR(64) DEFAULT '',
  PRIMARY KEY (`date`,`id`,`nick`,`multipler`)
  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;
