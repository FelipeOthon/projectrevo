DROP TABLE IF EXISTS `teleport`;
CREATE TABLE `teleport` (
  `Description` varchar(75) DEFAULT NULL,
  `id` decimal(11,0) NOT NULL DEFAULT '0',
  `loc_x` decimal(9,0) DEFAULT NULL,
  `loc_y` decimal(9,0) DEFAULT NULL,
  `loc_z` decimal(9,0) DEFAULT NULL,
  `price` decimal(6,0) DEFAULT NULL,
  `fornoble` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teleport
-- ----------------------------
INSERT INTO `teleport` VALUES 
('Ember', '100', '184808', '-106344', '-6335', '1', '0'),
('Uruka', '101', '3800', '-7544', '-3478', '1', '0'),
('Queen Shyeed', '102', '78456', '-53992', '-6163', '1', '0'),
('Flamestone Giant', '103', '144376', '-6504', '-4750', '1', '0'),
('Longhorn Golkonda', '104', '115816', '15800', '6971', '1', '0'),
('Ketras Chief Brakki', '105', '145288', '-83912', '-6136', '1', '0'),
('Ketras Hero Hekaton', '106', '147720', '-73000', '-4930', '1', '0'),
('Ketras Commander Tayr', '107', '145512', '-82200', '-6041', '1', '0'),
('Fire of Wrath Shuriel', '108', '112600', '17096', '6971', '1', '0'),
('Ocean Flame Ashakiel', '109', '124088', '154616', '-3687', '1', '0'),
('Last Lesser Giant Glaki', '110', '172888', '55144', '-5983', '1', '0'),
('Ketra', '111', '146974', '-67135', '-3640', '1', '0'),
('Varka', '112', '125706', '-40848', '-3726', '1', '0'),
('Dino', '113', '11224', '-24264', '-3668', '1', '0'),
('FOG', '114', '173656', '-115464', '-3789', '1', '0'),
('Noble', '115', '85784', '-85608', '-3528', '1', '0');