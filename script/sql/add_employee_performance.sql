create table if not exists nail_employee_performance (
  id bigint(20) not null comment '主键',
  employee_id bigint(20) not null comment '员工ID',
  store_id bigint(20) default null comment '门店ID',
  amount decimal(10,2) not null default 0.00 comment '业绩金额',
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
  primary key (id),
  index idx_nail_performance_emp_time (employee_id, consume_time),
  index idx_nail_performance_store_time (store_id, consume_time)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='员工业绩表';

insert into sys_menu
select 2122, '员工业绩', 2101, 3, 'performance',
       'employee/performance/index', '', 1, 0, 'C', '0', '0',
       'employee:performance:list', 'trophy', 103, 1, sysdate(),
       null, null, '员工业绩与排行榜'
where not exists (select 1 from sys_menu where menu_id = 2122);

insert into sys_menu
select 2260, '查询', 2122, 1, '#', null, '', 1, 0, 'F',
       '0', '0', 'employee:performance:query', '#', 103, 1, sysdate(),
       null, null, ''
where not exists (select 1 from sys_menu where menu_id = 2260);

insert into sys_menu
select 2261, '新增', 2122, 2, '#', null, '', 1, 0, 'F',
       '0', '0', 'employee:performance:add', '#', 103, 1, sysdate(),
       null, null, ''
where not exists (select 1 from sys_menu where menu_id = 2261);

insert into sys_menu
select 2262, '修改', 2122, 3, '#', null, '', 1, 0, 'F',
       '0', '0', 'employee:performance:edit', '#', 103, 1, sysdate(),
       null, null, ''
where not exists (select 1 from sys_menu where menu_id = 2262);

insert into sys_menu
select 2263, '删除', 2122, 4, '#', null, '', 1, 0, 'F',
       '0', '0', 'employee:performance:remove', '#', 103, 1, sysdate(),
       null, null, ''
where not exists (select 1 from sys_menu where menu_id = 2263);

insert into sys_menu
select 2264, '导出', 2122, 5, '#', null, '', 1, 0, 'F',
       '0', '0', 'employee:performance:export', '#', 103, 1, sysdate(),
       null, null, ''
where not exists (select 1 from sys_menu where menu_id = 2264);

insert ignore into sys_role_menu(role_id, menu_id)
select role_id, 2101 from sys_role where role_key in ('store_manager', 'nail_artist');
insert ignore into sys_role_menu(role_id, menu_id)
select role_id, 2122 from sys_role where role_key in ('store_manager', 'nail_artist');
insert ignore into sys_role_menu(role_id, menu_id)
select role_id, menu_id from sys_role
cross join (
  select 2260 menu_id union all select 2261 union all select 2262
  union all select 2263 union all select 2264
) performance_menu
where role_key in ('store_manager', 'nail_artist');
