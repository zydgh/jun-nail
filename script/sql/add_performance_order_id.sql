SET NAMES utf8mb4;

-- 员工业绩关联收银订单。order_id 可空，手录业绩不填；非空值唯一，同一订单只保留一条业绩。
-- MySQL 唯一索引允许多个 NULL，因此不影响历史手录数据。

SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'nail_employee_performance'
    AND COLUMN_NAME = 'order_id'
);

SET @sql := IF(
  @col_exists = 0,
  'ALTER TABLE nail_employee_performance ADD COLUMN order_id bigint(20) default null comment ''订单ID'' AFTER employee_id',
  'SELECT 1'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists := (
  SELECT COUNT(*)
  FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'nail_employee_performance'
    AND INDEX_NAME = 'uk_nail_performance_order_id'
);

SET @sql := IF(
  @idx_exists = 0,
  'ALTER TABLE nail_employee_performance ADD UNIQUE INDEX uk_nail_performance_order_id (order_id)',
  'SELECT 1'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
