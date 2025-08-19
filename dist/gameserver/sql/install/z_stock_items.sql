CREATE TABLE `z_stock_items` (
  `id` bigint(9) NOT NULL auto_increment,
  `itemId` smallint(5) unsigned NOT NULL default '0',
  `itemName` varchar(128) CHARACTER SET UTF8 NOT NULL default '',
  `enchant` smallint(5) unsigned NOT NULL default '0',
  `variation1_id` int(11) default '0',
  `variation2_id` int(11) default '0',
  `variation_stone_id` int(11) default '0',
  `price` int(11) default '0',
  `money` int(11) default '0',
  `type` tinyint(1) default '0',
  `ownerId` int(10) unsigned NOT NULL default '0',
  `shadow` tinyint(1) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;