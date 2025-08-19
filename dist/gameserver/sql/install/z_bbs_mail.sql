CREATE TABLE `z_bbs_mail` (
  `id` bigint(255) NOT NULL AUTO_INCREMENT,
  `from` varchar(35) CHARACTER SET UTF8 NOT NULL DEFAULT '',
  `to` varchar(35) CHARACTER SET UTF8 NOT NULL DEFAULT '',
  `tema` varchar(255) CHARACTER SET UTF8 NOT NULL DEFAULT '',
  `text` varchar(11255) CHARACTER SET UTF8 NOT NULL DEFAULT '',
  `datetime` decimal(20,0) DEFAULT NULL,
  `read` smallint(3) NOT NULL,
  `item_id` smallint(5) unsigned NOT NULL DEFAULT '0',
  `item_count` bigint(20) unsigned NOT NULL DEFAULT '0',
  `item_ench` smallint(5) unsigned NOT NULL DEFAULT '0',
  `variation1_id` int(11) DEFAULT '0',
  `variation2_id` int(11) NOT NULL DEFAULT '0',
  `variation_stone_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;