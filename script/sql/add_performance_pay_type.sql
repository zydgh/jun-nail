SET NAMES utf8mb4;

SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'nail_employee_performance'
    AND COLUMN_NAME = 'pay_type'
);

SET @sql := IF(
  @col_exists = 0,
  'ALTER TABLE nail_employee_performance ADD COLUMN pay_type varchar(20) default null comment ''付款方式'' AFTER amount',
  'SELECT 1'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

INSERT INTO sys_dict_type (dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, remark)
SELECT 29, '000000', '业绩付款方式', 'nail_performance_pay_type', 103, 1, sysdate(), '员工业绩付款方式'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_id = 29 OR dict_type = 'nail_performance_pay_type');

INSERT INTO sys_dict_data (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, create_dept, create_by, create_time, remark)
SELECT 282, '000000', 1, '美团', 'meituan', 'nail_performance_pay_type', '', 'warning', 'N', 103, 1, sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 282 OR (dict_type = 'nail_performance_pay_type' AND dict_value = 'meituan'));

INSERT INTO sys_dict_data (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, create_dept, create_by, create_time, remark)
SELECT 283, '000000', 2, '抖音', 'douyin', 'nail_performance_pay_type', '', 'danger', 'N', 103, 1, sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 283 OR (dict_type = 'nail_performance_pay_type' AND dict_value = 'douyin'));

INSERT INTO sys_dict_data (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, create_dept, create_by, create_time, remark)
SELECT 284, '000000', 3, '现金', 'cash', 'nail_performance_pay_type', '', 'info', 'Y', 103, 1, sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 284 OR (dict_type = 'nail_performance_pay_type' AND dict_value = 'cash'));

INSERT INTO sys_dict_data (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, create_dept, create_by, create_time, remark)
SELECT 285, '000000', 4, '支付宝', 'alipay', 'nail_performance_pay_type', '', 'primary', 'N', 103, 1, sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 285 OR (dict_type = 'nail_performance_pay_type' AND dict_value = 'alipay'));

INSERT INTO sys_dict_data (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, create_dept, create_by, create_time, remark)
SELECT 286, '000000', 5, '微信', 'wechat', 'nail_performance_pay_type', '', 'success', 'N', 103, 1, sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 286 OR (dict_type = 'nail_performance_pay_type' AND dict_value = 'wechat'));
