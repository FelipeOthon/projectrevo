ALTER TABLE characters CHANGE `LastHWID` `last_hwid` VARCHAR(128) DEFAULT '';
ALTER TABLE characters ADD COLUMN `last_ip` VARCHAR(16) DEFAULT '';
REPLACE INTO installed_updates (`file_name`) VALUES ("2019_04_22 18-21");