-- 君君美甲业务库（在 ry-vue 系统库上增量执行）
SET NAMES utf8mb4;

ALTER TABLE sys_user ADD COLUMN must_change_pwd char(1) DEFAULT '1' COMMENT '是否必须修改初始密码（0否 1是）' AFTER remark;
UPDATE sys_user SET must_change_pwd = '1' WHERE user_name = 'admin';
UPDATE sys_user SET nick_name = '君君管理员' WHERE user_id = 1;
INSERT INTO sys_config VALUES(20, '000000', '首次登录强制改密', 'sys.account.forceModifyPwd', 'true', 'Y', 103, 1, sysdate(), null, null, 'true:首次登录强制修改密码');

INSERT INTO sys_role VALUES(5, '000000', '店长', 'store_manager', 5, 7, 1, 1, '0', '0', 103, 1, sysdate(), null, null, '本门店数据权限');
INSERT INTO sys_role VALUES(6, '000000', '美甲师', 'nail_artist', 6, 5, 1, 1, '0', '0', 103, 1, sysdate(), null, null, '仅本人数据权限');

INSERT INTO sys_dict_type VALUES(20, '000000', '会员来源', 'nail_member_source', 103, 1, sysdate(), null, null, '会员来源');
INSERT INTO sys_dict_type VALUES(21, '000000', '余额变动类型', 'nail_balance_type', 103, 1, sysdate(), null, null, '余额变动');
INSERT INTO sys_dict_type VALUES(22, '000000', '积分变动类型', 'nail_point_type', 103, 1, sysdate(), null, null, '积分变动');
INSERT INTO sys_dict_type VALUES(23, '000000', '预约状态', 'nail_appoint_status', 103, 1, sysdate(), null, null, '预约状态');
INSERT INTO sys_dict_type VALUES(24, '000000', '订单状态', 'nail_order_status', 103, 1, sysdate(), null, null, '订单状态');
INSERT INTO sys_dict_type VALUES(25, '000000', '支付方式', 'nail_pay_type', 103, 1, sysdate(), null, null, '支付方式');
INSERT INTO sys_dict_type VALUES(29, '000000', '业绩付款方式', 'nail_performance_pay_type', 103, 1, sysdate(), null, null, '员工业绩付款方式');
INSERT INTO sys_dict_type VALUES(26, '000000', '员工职位', 'nail_employee_position', 103, 1, sysdate(), null, null, '员工职位');
INSERT INTO sys_dict_type VALUES(27, '000000', '项目分类', 'nail_item_category', 103, 1, sysdate(), null, null, '项目分类');
INSERT INTO sys_dict_type VALUES(28, '000000', '提成结算状态', 'nail_commission_status', 103, 1, sysdate(), null, null, '提成结算');

INSERT INTO sys_dict_data VALUES(200, '000000', 1, '到店', 'store', 'nail_member_source', '', 'primary', 'Y', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(201, '000000', 2, '美团', 'meituan', 'nail_member_source', '', 'success', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(202, '000000', 3, '抖音', 'douyin', 'nail_member_source', '', 'warning', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(203, '000000', 4, '老客介绍', 'referral', 'nail_member_source', '', 'info', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(210, '000000', 1, '充值', 'recharge', 'nail_balance_type', '', 'success', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(211, '000000', 2, '消费', 'consume', 'nail_balance_type', '', 'danger', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(212, '000000', 3, '退款', 'refund', 'nail_balance_type', '', 'warning', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(213, '000000', 4, '调整', 'adjust', 'nail_balance_type', '', 'info', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(214, '000000', 5, '赠送', 'gift', 'nail_balance_type', '', 'primary', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(220, '000000', 1, '消费赠送', 'consume', 'nail_point_type', '', 'success', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(221, '000000', 2, '兑换', 'exchange', 'nail_point_type', '', 'warning', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(222, '000000', 3, '调整', 'adjust', 'nail_point_type', '', 'info', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(230, '000000', 1, '待到店', '0', 'nail_appoint_status', '', 'info', 'Y', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(231, '000000', 2, '已完成', '1', 'nail_appoint_status', '', 'success', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(232, '000000', 3, '已取消', '2', 'nail_appoint_status', '', 'danger', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(240, '000000', 1, '待支付', '0', 'nail_order_status', '', 'info', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(241, '000000', 2, '已完成', '1', 'nail_order_status', '', 'success', 'Y', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(242, '000000', 3, '已退款', '2', 'nail_order_status', '', 'danger', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(250, '000000', 1, '余额', 'balance', 'nail_pay_type', '', 'primary', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(251, '000000', 2, '微信', 'wechat', 'nail_pay_type', '', 'success', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(252, '000000', 3, '支付宝', 'alipay', 'nail_pay_type', '', 'warning', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(253, '000000', 4, '现金', 'cash', 'nail_pay_type', '', 'info', 'Y', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(282, '000000', 1, '美团', 'meituan', 'nail_performance_pay_type', '', 'warning', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(283, '000000', 2, '抖音', 'douyin', 'nail_performance_pay_type', '', 'danger', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(284, '000000', 3, '现金', 'cash', 'nail_performance_pay_type', '', 'info', 'Y', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(286, '000000', 4, '微信', 'wechat', 'nail_performance_pay_type', '', 'success', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(285, '000000', 5, '支付宝', 'alipay', 'nail_performance_pay_type', '', 'primary', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(260, '000000', 1, '店长', 'manager', 'nail_employee_position', '', 'primary', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(261, '000000', 2, '美甲师', 'artist', 'nail_employee_position', '', 'success', 'Y', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(262, '000000', 3, '前台', 'front', 'nail_employee_position', '', 'info', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(270, '000000', 1, '手部护理', 'hand', 'nail_item_category', '', 'primary', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(271, '000000', 2, '美甲款式', 'style', 'nail_item_category', '', 'success', 'Y', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(272, '000000', 3, '卸甲修补', 'repair', 'nail_item_category', '', 'warning', 'N', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(280, '000000', 1, '未结算', '0', 'nail_commission_status', '', 'info', 'Y', 103, 1, sysdate(), null, null, '');
INSERT INTO sys_dict_data VALUES(281, '000000', 2, '已结算', '1', 'nail_commission_status', '', 'success', 'N', 103, 1, sysdate(), null, null, '');


drop table if exists nail_store;
create table nail_store (
  id bigint(20) not null comment '主键',
  store_code varchar(32) not null comment '门店编号',
  store_name varchar(64) not null comment '门店名称',
  phone varchar(20) default null comment '联系电话',
  address varchar(255) default null comment '门店地址',
  manager varchar(32) default null comment '负责人',
  sort int(4) default 0 comment '排序',
  status char(1) default '0' comment '状态（0正常 1停用）',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='门店表';
create unique index uk_nail_store_code on nail_store(tenant_id, store_code, del_flag);

drop table if exists nail_member_level;
create table nail_member_level (
  id bigint(20) not null comment '主键',
  level_name varchar(32) not null comment '等级名称',
  discount decimal(10,2) not null default 1.00 comment '折扣',
  upgrade_point int(11) default 0 comment '升级积分',
  sort int(4) default 0 comment '排序',
  status char(1) default '0' comment '状态（0正常 1停用）',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='会员等级表';

drop table if exists nail_member;
create table nail_member (
  id bigint(20) not null comment '主键',
  member_no varchar(32) not null comment '会员编号',
  name varchar(32) not null comment '姓名',
  phone varchar(20) not null comment '手机号',
  sex char(1) default '2' comment '性别',
  birthday date default null comment '生日',
  level_id bigint(20) default null comment '会员等级',
  point int(11) default 0 comment '积分',
  balance decimal(10,2) default 0.00 comment '余额',
  total_recharge decimal(10,2) default 0.00 comment '累计充值',
  total_consume decimal(10,2) default 0.00 comment '累计消费',
  consume_times int(11) default 0 comment '消费次数',
  last_visit_time datetime default null comment '最近到店时间',
  store_id bigint(20) default null comment '所属门店',
  source varchar(32) default null comment '来源',
  tags varchar(255) default null comment '标签',
  status char(1) default '0' comment '状态（0正常 1停用）',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='会员表';
create unique index uk_nail_member_no on nail_member(tenant_id, member_no, del_flag);
create index idx_nail_member_phone on nail_member(phone);
create index idx_nail_member_store on nail_member(store_id);
create index idx_nail_member_birthday on nail_member(birthday);

drop table if exists nail_member_balance_log;
create table nail_member_balance_log (
  id bigint(20) not null comment '主键',
  member_id bigint(20) not null comment '会员ID',
  change_type varchar(20) not null comment '变动类型',
  amount decimal(10,2) not null comment '变动金额',
  before_balance decimal(10,2) default 0.00 comment '变动前余额',
  after_balance decimal(10,2) default 0.00 comment '变动后余额',
  biz_no varchar(64) default null comment '业务单号',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='会员余额变动表';
create index idx_nail_bal_member on nail_member_balance_log(member_id);

drop table if exists nail_member_point_log;
create table nail_member_point_log (
  id bigint(20) not null comment '主键',
  member_id bigint(20) not null comment '会员ID',
  change_type varchar(20) not null comment '变动类型',
  point int(11) not null comment '变动积分',
  before_point int(11) default 0 comment '变动前积分',
  after_point int(11) default 0 comment '变动后积分',
  biz_no varchar(64) default null comment '业务单号',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='会员积分变动表';
create index idx_nail_point_member on nail_member_point_log(member_id);

drop table if exists nail_employee;
create table nail_employee (
  id bigint(20) not null comment '主键',
  employee_no varchar(32) not null comment '员工编号',
  name varchar(32) not null comment '姓名',
  phone varchar(20) not null comment '手机号',
  sex char(1) default '2' comment '性别',
  avatar varchar(255) default null comment '头像',
  store_id bigint(20) default null comment '所属门店',
  position varchar(32) default null comment '职位',
  grade varchar(32) default null comment '等级',
  commission_rate decimal(10,2) default 0.00 comment '提成比例',
  hire_date date default null comment '入职日期',
  user_id bigint(20) default null comment '关联系统用户',
  skill varchar(255) default null comment '擅长项目',
  status char(1) default '0' comment '状态（0正常 1停用）',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='员工表';
create unique index uk_nail_employee_no on nail_employee(tenant_id, employee_no, del_flag);
create index idx_nail_emp_user on nail_employee(user_id);
create index idx_nail_emp_store on nail_employee(store_id);

drop table if exists nail_employee_shift;
create table nail_employee_shift (
  id bigint(20) not null comment '主键',
  employee_id bigint(20) not null comment '员工ID',
  store_id bigint(20) default null comment '门店ID',
  shift_date date not null comment '排班日期',
  start_time varchar(8) default null comment '开始时间',
  end_time varchar(8) default null comment '结束时间',
  status char(1) default '0' comment '状态（0正常 1停用）',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='员工排班表';
create index idx_nail_shift_emp on nail_employee_shift(employee_id, shift_date);

drop table if exists nail_employee_performance;
create table nail_employee_performance (
  id bigint(20) not null comment '主键',
  employee_id bigint(20) not null comment '员工ID',
  store_id bigint(20) default null comment '门店ID',
  amount decimal(10,2) not null default 0.00 comment '业绩金额',
  pay_type varchar(20) default null comment '付款方式',
  item_id bigint(20) default null comment '项目ID',
  item_name varchar(64) default null comment '项目名称',
  customer_name varchar(64) default null comment '客户姓名',
  customer_phone varchar(20) default null comment '客户手机号',
  consume_time datetime not null comment '消费时间',
  praise char(1) default '0' comment '是否好评（0否 1是）',
  praise_images varchar(500) default null comment '好评图片OSS ID，逗号分隔',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='员工业绩表';
create index idx_nail_performance_emp_time on nail_employee_performance(employee_id, consume_time);
create index idx_nail_performance_store_time on nail_employee_performance(store_id, consume_time);

drop table if exists nail_service_item;
create table nail_service_item (
  id bigint(20) not null comment '主键',
  item_code varchar(32) not null comment '项目编号',
  item_name varchar(64) not null comment '项目名称',
  category varchar(32) default null comment '分类',
  duration_min int(11) default 60 comment '时长(分钟)',
  price decimal(10,2) not null default 0.00 comment '原价',
  member_price decimal(10,2) default null comment '会员价',
  sort int(4) default 0 comment '排序',
  status char(1) default '0' comment '状态（0正常 1停用）',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='美甲服务项目表';
create unique index uk_nail_item_code on nail_service_item(tenant_id, item_code, del_flag);

drop table if exists nail_appointment;
create table nail_appointment (
  id bigint(20) not null comment '主键',
  appoint_no varchar(32) not null comment '预约单号',
  member_id bigint(20) not null comment '会员ID',
  employee_id bigint(20) default null comment '美甲师ID',
  store_id bigint(20) default null comment '门店ID',
  item_id bigint(20) default null comment '项目ID',
  appoint_time datetime not null comment '预约时间',
  duration_min int(11) default 60 comment '时长(分钟)',
  status char(1) default '0' comment '预约状态',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='预约表';
create unique index uk_nail_appoint_no on nail_appointment(tenant_id, appoint_no, del_flag);
create index idx_nail_appoint_time on nail_appointment(appoint_time);

drop table if exists nail_order;
create table nail_order (
  id bigint(20) not null comment '主键',
  order_no varchar(32) not null comment '订单编号',
  member_id bigint(20) default null comment '会员ID',
  employee_id bigint(20) default null comment '美甲师ID',
  customer_name varchar(64) default null comment '客户姓名',
  customer_phone varchar(20) default null comment '客户手机号',
  store_id bigint(20) default null comment '门店ID',
  order_time datetime default null comment '下单时间',
  pay_type varchar(20) default null comment '支付方式',
  total_amount decimal(10,2) default 0.00 comment '订单总额',
  discount_amount decimal(10,2) default 0.00 comment '优惠金额',
  pay_amount decimal(10,2) default 0.00 comment '实付金额',
  point_amount int(11) default 0 comment '赠送积分',
  praise char(1) default '0' comment '是否好评（0否 1是）',
  praise_images varchar(500) default null comment '好评图片OSS ID',
  status char(1) default '1' comment '订单状态',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='订单表';
create unique index uk_nail_order_no on nail_order(tenant_id, order_no, del_flag);
create index idx_nail_order_time on nail_order(order_time);
create index idx_nail_order_member on nail_order(member_id);

drop table if exists nail_order_item;
create table nail_order_item (
  id bigint(20) not null comment '主键',
  order_id bigint(20) not null comment '订单ID',
  item_id bigint(20) default null comment '项目ID',
  item_name varchar(64) default null comment '项目名称',
  quantity int(11) default 1 comment '数量',
  price decimal(10,2) default 0.00 comment '单价',
  amount decimal(10,2) default 0.00 comment '金额',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='订单明细表';
create index idx_nail_order_item_oid on nail_order_item(order_id);

drop table if exists nail_commission;
create table nail_commission (
  id bigint(20) not null comment '主键',
  employee_id bigint(20) not null comment '员工ID',
  order_id bigint(20) default null comment '订单ID',
  order_no varchar(32) default null comment '订单编号',
  commission_rate decimal(10,2) default 0.00 comment '提成比例',
  commission_amount decimal(10,2) default 0.00 comment '提成金额',
  status char(1) default '0' comment '结算状态',
  settle_time datetime default null comment '结算时间',

  tenant_id varchar(20) default '000000' comment '租户编号',
  create_dept bigint(20) default null comment '创建部门',
  create_by bigint(20) default null comment '创建者',
  create_time datetime default null comment '创建时间',
  update_by bigint(20) default null comment '更新者',
  update_time datetime default null comment '更新时间',
  del_flag char(1) default '0' comment '删除标志（0正常 1删除）',
  remark varchar(500) default null comment '备注',
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='员工提成表';
create index idx_nail_comm_emp on nail_commission(employee_id);

-- 美甲菜单
insert into sys_menu values('2090', '营业看板', '0', '0', 'dashboard', 'nail/dashboard/index', '', 1, 0, 'C', '0', '0', 'nail:dashboard:list', 'dashboard', 103, 1, sysdate(), null, null, '工作台看板');
insert into sys_menu values('2100', '会员管理', '0', '6', 'member', null, '', 1, 0, 'M', '0', '0', '', 'peoples', 103, 1, sysdate(), null, null, '会员管理目录');
insert into sys_menu values('2101', '员工管理', '0', '7', 'employee', null, '', 1, 0, 'M', '0', '0', '', 'user', 103, 1, sysdate(), null, null, '员工管理目录');
insert into sys_menu values('2102', '美甲业务', '0', '8', 'nail', null, '', 1, 0, 'M', '0', '0', '', 'component', 103, 1, sysdate(), null, null, '美甲业务目录');
insert into sys_menu values('2110', '会员列表', '2100', '1', 'list', 'member/member/index', '', 1, 0, 'C', '0', '0', 'member:member:list', 'user', 103, 1, sysdate(), null, null, '会员列表');
insert into sys_menu values('2111', '会员等级', '2100', '2', 'level', 'member/level/index', '', 1, 0, 'C', '0', '0', 'member:level:list', 'star', 103, 1, sysdate(), null, null, '会员等级');
insert into sys_menu values('2112', '余额记录', '2100', '3', 'balance', 'member/balanceLog/index', '', 1, 0, 'C', '0', '0', 'member:balance:list', 'money', 103, 1, sysdate(), null, null, '余额记录');
insert into sys_menu values('2113', '积分记录', '2100', '4', 'point', 'member/pointLog/index', '', 1, 0, 'C', '0', '0', 'member:point:list', 'chart', 103, 1, sysdate(), null, null, '积分记录');
insert into sys_menu values('2120', '员工列表', '2101', '1', 'list', 'employee/employee/index', '', 1, 0, 'C', '0', '0', 'employee:employee:list', 'peoples', 103, 1, sysdate(), null, null, '员工列表');
insert into sys_menu values('2121', '员工排班', '2101', '2', 'shift', 'employee/shift/index', '', 1, 0, 'C', '0', '0', 'employee:shift:list', 'date', 103, 1, sysdate(), null, null, '员工排班');
insert into sys_menu values('2122', '员工业绩', '2101', '3', 'performance', 'employee/performance/index', '', 1, 0, 'C', '1', '1', 'employee:performance:list', 'trophy', 103, 1, sysdate(), null, null, '已并入订单收银');
insert into sys_menu values('2130', '门店管理', '2102', '1', 'store', 'store/store/index', '', 1, 0, 'C', '0', '0', 'store:store:list', 'international', 103, 1, sysdate(), null, null, '门店管理');
insert into sys_menu values('2131', '服务项目', '2102', '2', 'item', 'nail/item/index', '', 1, 0, 'C', '0', '0', 'nail:item:list', 'list', 103, 1, sysdate(), null, null, '服务项目');
insert into sys_menu values('2132', '预约管理', '2102', '3', 'appointment', 'nail/appointment/index', '', 1, 0, 'C', '0', '0', 'nail:appointment:list', 'date', 103, 1, sysdate(), null, null, '预约管理');
insert into sys_menu values('2133', '订单收银', '2102', '4', 'order', 'nail/order/index', '', 1, 0, 'C', '0', '0', 'nail:order:list', 'shopping', 103, 1, sysdate(), null, null, '订单收银');
insert into sys_menu values('2134', '员工提成', '2102', '5', 'commission', 'nail/commission/index', '', 1, 0, 'C', '0', '0', 'nail:commission:list', 'money', 103, 1, sysdate(), null, null, '员工提成');
insert into sys_menu values('2200', '查询', '2110', '1', '#', null, '', 1, 0, 'F', '0', '0', 'member:member:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2201', '新增', '2110', '2', '#', null, '', 1, 0, 'F', '0', '0', 'member:member:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2202', '修改', '2110', '3', '#', null, '', 1, 0, 'F', '0', '0', 'member:member:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2203', '删除', '2110', '4', '#', null, '', 1, 0, 'F', '0', '0', 'member:member:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2204', '导出', '2110', '5', '#', null, '', 1, 0, 'F', '0', '0', 'member:member:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2205', '导入', '2110', '6', '#', null, '', 1, 0, 'F', '0', '0', 'member:member:import', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2206', '充值', '2110', '7', '#', null, '', 1, 0, 'F', '0', '0', 'member:member:recharge', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2207', '调账', '2110', '8', '#', null, '', 1, 0, 'F', '0', '0', 'member:member:adjust', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2208', '查询', '2111', '1', '#', null, '', 1, 0, 'F', '0', '0', 'member:level:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2209', '新增', '2111', '2', '#', null, '', 1, 0, 'F', '0', '0', 'member:level:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2210', '修改', '2111', '3', '#', null, '', 1, 0, 'F', '0', '0', 'member:level:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2211', '删除', '2111', '4', '#', null, '', 1, 0, 'F', '0', '0', 'member:level:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2212', '导出', '2111', '5', '#', null, '', 1, 0, 'F', '0', '0', 'member:level:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2213', '查询', '2112', '1', '#', null, '', 1, 0, 'F', '0', '0', 'member:balance:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2214', '新增', '2112', '2', '#', null, '', 1, 0, 'F', '0', '0', 'member:balance:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2215', '修改', '2112', '3', '#', null, '', 1, 0, 'F', '0', '0', 'member:balance:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2216', '删除', '2112', '4', '#', null, '', 1, 0, 'F', '0', '0', 'member:balance:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2217', '导出', '2112', '5', '#', null, '', 1, 0, 'F', '0', '0', 'member:balance:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2218', '查询', '2113', '1', '#', null, '', 1, 0, 'F', '0', '0', 'member:point:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2219', '新增', '2113', '2', '#', null, '', 1, 0, 'F', '0', '0', 'member:point:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2220', '修改', '2113', '3', '#', null, '', 1, 0, 'F', '0', '0', 'member:point:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2221', '删除', '2113', '4', '#', null, '', 1, 0, 'F', '0', '0', 'member:point:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2222', '导出', '2113', '5', '#', null, '', 1, 0, 'F', '0', '0', 'member:point:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2223', '查询', '2120', '1', '#', null, '', 1, 0, 'F', '0', '0', 'employee:employee:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2224', '新增', '2120', '2', '#', null, '', 1, 0, 'F', '0', '0', 'employee:employee:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2225', '修改', '2120', '3', '#', null, '', 1, 0, 'F', '0', '0', 'employee:employee:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2226', '删除', '2120', '4', '#', null, '', 1, 0, 'F', '0', '0', 'employee:employee:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2227', '导出', '2120', '5', '#', null, '', 1, 0, 'F', '0', '0', 'employee:employee:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2228', '充值', '2120', '7', '#', null, '', 1, 0, 'F', '0', '0', 'employee:employee:recharge', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2229', '调账', '2120', '8', '#', null, '', 1, 0, 'F', '0', '0', 'employee:employee:adjust', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2230', '查询', '2121', '1', '#', null, '', 1, 0, 'F', '0', '0', 'employee:shift:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2231', '新增', '2121', '2', '#', null, '', 1, 0, 'F', '0', '0', 'employee:shift:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2232', '修改', '2121', '3', '#', null, '', 1, 0, 'F', '0', '0', 'employee:shift:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2233', '删除', '2121', '4', '#', null, '', 1, 0, 'F', '0', '0', 'employee:shift:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2234', '导出', '2121', '5', '#', null, '', 1, 0, 'F', '0', '0', 'employee:shift:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2235', '查询', '2130', '1', '#', null, '', 1, 0, 'F', '0', '0', 'store:store:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2236', '新增', '2130', '2', '#', null, '', 1, 0, 'F', '0', '0', 'store:store:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2237', '修改', '2130', '3', '#', null, '', 1, 0, 'F', '0', '0', 'store:store:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2238', '删除', '2130', '4', '#', null, '', 1, 0, 'F', '0', '0', 'store:store:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2239', '导出', '2130', '5', '#', null, '', 1, 0, 'F', '0', '0', 'store:store:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2240', '查询', '2131', '1', '#', null, '', 1, 0, 'F', '0', '0', 'nail:item:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2241', '新增', '2131', '2', '#', null, '', 1, 0, 'F', '0', '0', 'nail:item:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2242', '修改', '2131', '3', '#', null, '', 1, 0, 'F', '0', '0', 'nail:item:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2243', '删除', '2131', '4', '#', null, '', 1, 0, 'F', '0', '0', 'nail:item:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2244', '导出', '2131', '5', '#', null, '', 1, 0, 'F', '0', '0', 'nail:item:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2245', '查询', '2132', '1', '#', null, '', 1, 0, 'F', '0', '0', 'nail:appointment:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2246', '新增', '2132', '2', '#', null, '', 1, 0, 'F', '0', '0', 'nail:appointment:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2247', '修改', '2132', '3', '#', null, '', 1, 0, 'F', '0', '0', 'nail:appointment:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2248', '删除', '2132', '4', '#', null, '', 1, 0, 'F', '0', '0', 'nail:appointment:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2249', '导出', '2132', '5', '#', null, '', 1, 0, 'F', '0', '0', 'nail:appointment:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2250', '查询', '2133', '1', '#', null, '', 1, 0, 'F', '0', '0', 'nail:order:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2251', '新增', '2133', '2', '#', null, '', 1, 0, 'F', '0', '0', 'nail:order:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2252', '修改', '2133', '3', '#', null, '', 1, 0, 'F', '0', '0', 'nail:order:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2253', '删除', '2133', '4', '#', null, '', 1, 0, 'F', '0', '0', 'nail:order:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2254', '导出', '2133', '5', '#', null, '', 1, 0, 'F', '0', '0', 'nail:order:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2255', '查询', '2134', '1', '#', null, '', 1, 0, 'F', '0', '0', 'nail:commission:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2256', '新增', '2134', '2', '#', null, '', 1, 0, 'F', '0', '0', 'nail:commission:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2257', '修改', '2134', '3', '#', null, '', 1, 0, 'F', '0', '0', 'nail:commission:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2258', '删除', '2134', '4', '#', null, '', 1, 0, 'F', '0', '0', 'nail:commission:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2259', '导出', '2134', '5', '#', null, '', 1, 0, 'F', '0', '0', 'nail:commission:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2260', '查询', '2122', '1', '#', null, '', 1, 0, 'F', '0', '0', 'employee:performance:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2261', '新增', '2122', '2', '#', null, '', 1, 0, 'F', '0', '0', 'employee:performance:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2262', '修改', '2122', '3', '#', null, '', 1, 0, 'F', '0', '0', 'employee:performance:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2263', '删除', '2122', '4', '#', null, '', 1, 0, 'F', '0', '0', 'employee:performance:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2264', '导出', '2122', '5', '#', null, '', 1, 0, 'F', '0', '0', 'employee:performance:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('2290', '看板查询', '2090', '1', '#', null, '', 1, 0, 'F', '0', '0', 'nail:dashboard:query', '#', 103, 1, sysdate(), null, null, '');

insert into sys_role_menu values(5, 2101);
insert into sys_role_menu values(5, 2122);
insert into sys_role_menu values(5, 2260);
insert into sys_role_menu values(5, 2261);
insert into sys_role_menu values(5, 2262);
insert into sys_role_menu values(5, 2263);
insert into sys_role_menu values(5, 2264);
insert into sys_role_menu values(6, 2101);
insert into sys_role_menu values(6, 2122);
insert into sys_role_menu values(6, 2260);
insert into sys_role_menu values(6, 2261);
insert into sys_role_menu values(6, 2262);
insert into sys_role_menu values(6, 2263);
insert into sys_role_menu values(6, 2264);

-- 演示数据（字段顺序与建表一致：业务字段在前，租户与审计字段在后）
insert into nail_store values(1,'MD001','君君美甲旗舰店','13800001111','上海市静安区南京西路1号','君君',1,'0','000000',103,1,sysdate(),null,null,'0','旗舰店');
insert into nail_store values(2,'MD002','君君美甲徐汇店','13800002222','上海市徐汇区衡山路88号','小徐',2,'0','000000',103,1,sysdate(),null,null,'0','分店');

insert into nail_member_level values(1,'普通会员',1.00,0,1,'0','000000',103,1,sysdate(),null,null,'0','入会即享');
insert into nail_member_level values(2,'银卡会员',0.95,500,2,'0','000000',103,1,sysdate(),null,null,'0','9.5折');
insert into nail_member_level values(3,'金卡会员',0.90,2000,3,'0','000000',103,1,sysdate(),null,null,'0','9折');

insert into nail_service_item values(1,'XM001','基础护理','hand',45,88.00,78.00,1,'0','000000',103,1,sysdate(),null,null,'0','手部基础护理');
insert into nail_service_item values(2,'XM002','日式美甲','style',90,198.00,178.00,2,'0','000000',103,1,sysdate(),null,null,'0','经典日式');
insert into nail_service_item values(3,'XM003','卸甲修补','repair',30,48.00,38.00,3,'0','000000',103,1,sysdate(),null,null,'0','卸甲');

insert into nail_employee values(1,'YG001','林小美','13900001111','1',null,1,'artist','中级',0.20,'2024-03-01',null,'日式美甲,延长','0','000000',103,1,sysdate(),null,null,'0','头牌美甲师');
insert into nail_employee values(2,'YG002','陈店长','13900002222','0',null,1,'manager','高级',0.10,'2023-01-08',1,'门店管理','0','000000',103,1,sysdate(),null,null,'0','关联admin');

insert into nail_member values(1,'HY10001','张敏','13600001111','1','1996-09-15',2,120,680.00,800.00,320.00,3,sysdate(),1,'store','熟客,爱粉色','0','000000',103,1,sysdate(),null,null,'0','演示会员');
insert into nail_member values(2,'HY10002','李悦','13600002222','1','1999-09-18',1,20,0.00,0.00,88.00,1,sysdate(),1,'douyin','学生','0','000000',103,1,sysdate(),null,null,'0','生日临近');
insert into nail_member values(3,'HY10003','王芳','13600003333','1','1988-03-02',3,860,1280.00,2000.00,1560.00,8,sysdate(),2,'referral','金卡','0','000000',103,1,sysdate(),null,null,'0','徐汇店会员');

insert into nail_member_balance_log values(1,1,'recharge',800.00,0.00,800.00,'CZ001','000000',103,1,sysdate(),null,null,'0','开卡充值');
insert into nail_member_balance_log values(2,1,'consume',-120.00,800.00,680.00,'DDDEMO','000000',103,1,sysdate(),null,null,'0','消费扣款');

insert into nail_order values(1,'DD10001',1,1,1,sysdate(),'balance',198.00,20.00,178.00,178,'1','000000',103,1,sysdate(),null,null,'0','演示订单');
insert into nail_order_item values(1,1,2,'日式美甲',1,198.00,198.00,'000000',103,1,sysdate(),null,null,'0',null);
insert into nail_commission values(1,1,1,'DD10001',0.20,35.60,'0',null,'000000',103,1,sysdate(),null,null,'0',null);
