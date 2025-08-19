DROP TABLE IF EXISTS `buffer_skillsave`;
CREATE TABLE `buffer_skillsave` (
  `charId` int(10) NOT NULL DEFAULT 0,
  `name` varchar(35) CHARACTER SET UTF8 NOT NULL DEFAULT '',
  `skills` varchar(500) DEFAULT '',
  PRIMARY KEY (`charId`,`name`)
) ENGINE=MyISAM;
