CREATE TABLE IF NOT EXISTS `character_quests` (
  `char_id` int NOT NULL DEFAULT 0,
  `id` varchar(40) NOT NULL DEFAULT '',
  `var`  varchar(20) NOT NULL DEFAULT '',
  `value` varchar(255),
  PRIMARY KEY  (`char_id`,`id`,`var`)
) ENGINE=MyISAM;