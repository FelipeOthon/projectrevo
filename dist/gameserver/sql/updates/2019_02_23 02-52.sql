ALTER TABLE items ADD COLUMN `variation_stone_id` int(7) NOT NULL AFTER `flags`;
ALTER TABLE items ADD COLUMN `variation1_id` int(7) NOT NULL AFTER `variation_stone_id`;
ALTER TABLE items ADD COLUMN `variation2_id` int(7) NOT NULL AFTER `variation1_id`;

DROP TABLE IF EXISTS `augmentations`;

ALTER TABLE z_bbs_mail DROP COLUMN aug_hex;
ALTER TABLE z_bbs_mail DROP COLUMN aug_id;
ALTER TABLE z_bbs_mail DROP COLUMN aug_lvl;
ALTER TABLE z_bbs_mail ADD COLUMN `variation_stone_id` int(11) NOT NULL AFTER `item_ench`;
ALTER TABLE z_bbs_mail ADD COLUMN `variation1_id` int(11) NOT NULL AFTER `variation_stone_id`;
ALTER TABLE z_bbs_mail ADD COLUMN `variation2_id` int(11) NOT NULL AFTER `variation1_id`;

ALTER TABLE z_stock_items DROP COLUMN augment;
ALTER TABLE z_stock_items DROP COLUMN augAttr;
ALTER TABLE z_stock_items DROP COLUMN augLvl;
ALTER TABLE z_stock_items ADD COLUMN `variation_stone_id` int(11) NOT NULL AFTER `enchant`;
ALTER TABLE z_stock_items ADD COLUMN `variation1_id` int(11) NOT NULL AFTER `variation_stone_id`;
ALTER TABLE z_stock_items ADD COLUMN `variation2_id` int(11) NOT NULL AFTER `variation1_id`;

REPLACE INTO installed_updates (`file_name`) VALUES ("2019_02_23 02-52");