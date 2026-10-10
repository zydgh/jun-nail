SET NAMES utf8mb4;

SET @sql := IF(
  (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'nail_order' AND COLUMN_NAME = 'customer_name') = 0,
  'ALTER TABLE nail_order ADD COLUMN customer_name varchar(64) default null comment ''客户姓名'' AFTER employee_id',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := IF(
  (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'nail_order' AND COLUMN_NAME = 'customer_phone') = 0,
  'ALTER TABLE nail_order ADD COLUMN customer_phone varchar(20) default null comment ''客户手机号'' AFTER customer_name',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := IF(
  (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'nail_order' AND COLUMN_NAME = 'praise') = 0,
  'ALTER TABLE nail_order ADD COLUMN praise char(1) default ''0'' comment ''是否好评（0否 1是）'' AFTER point_amount',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := IF(
  (SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'nail_order' AND COLUMN_NAME = 'praise_images') = 0,
  'ALTER TABLE nail_order ADD COLUMN praise_images varchar(500) default null comment ''好评图片OSS ID'' AFTER praise',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

UPDATE sys_menu SET visible = '1', status = '1' WHERE menu_id = 2122;
