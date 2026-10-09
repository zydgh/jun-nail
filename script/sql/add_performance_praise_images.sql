SET NAMES utf8mb4;

SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'nail_employee_performance'
    AND COLUMN_NAME = 'praise_images'
);

SET @sql := IF(
  @col_exists = 0,
  'ALTER TABLE nail_employee_performance ADD COLUMN praise_images varchar(500) default null comment ''好评图片OSS ID，逗号分隔'' AFTER praise',
  'SELECT 1'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
