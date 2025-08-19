DROP TABLE IF EXISTS `auto_chat`;
CREATE TABLE `auto_chat` (
  `groupId` INT NOT NULL default '0',
  `npcId` INT NOT NULL default '0',
  `chatDelay` BIGINT NOT NULL default '-1',
  PRIMARY KEY  (`groupId`, `npcId`)
  ) ENGINE=MyISAM;

INSERT INTO `auto_chat` VALUES 
(1,31093,-1),
(2,31094,-1),
(3,22124,-1),
(3,22126,-1),
(3,22129,-1),
(4,22134,-1);