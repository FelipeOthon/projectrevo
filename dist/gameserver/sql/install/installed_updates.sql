DROP TABLE IF EXISTS `installed_updates`;
CREATE TABLE `installed_updates` (
	`file_name` VARCHAR(300) CHARACTER SET UTF8 NOT NULL DEFAULT '0',
	PRIMARY KEY  (`file_name`)
) ENGINE=MyISAM;

INSERT INTO installed_updates (`file_name`) VALUES
("2018_05_04 00-08"),
("2018_07_03 22-10"),
("2018_07_19 21-02"),
("2018_09_09 22-29"),
("2018_10_13 20-58"),
("2019_02_23 02-52"),
("2019_04_13 00-45"),
("2019_04_22 18-21");