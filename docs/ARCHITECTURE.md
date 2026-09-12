# 君君美甲后台 — 架构与清单

## 1. 项目架构

```
ruoyi-admin          启动入口、登录鉴权、Sa-Token 策略
ruoyi-common/*       统一响应、日志 AOP、Excel、租户、限流、加密
ruoyi-modules/ruoyi-system   用户/角色/菜单/字典/参数/日志
ruoyi-modules/ruoyi-nail     会员、员工、门店、预约、订单、提成、看板
plus-ui              Vue3 + TS 管理端
```

包名：系统沿用 `org.dromara.*`，业务新增 `org.dromara.nail`（兼容若依 Mapper 扫描 `org.dromara.**.mapper`）。

鉴权：**Sa-Token** + `StpInterface`（框架已实现），接口使用 `@SaCheckPermission`。Token 存 Redis。

操作日志：复用 `@Log` + AOP 异步写入 `sys_oper_log`。

数据权限：

| 值 | 含义 |
| --- | --- |
| 1 | 全部数据 |
| 3/4 | 本部门 / 本部门及以下（create_dept） |
| 5 | 仅本人（员工本人订单/预约/提成） |
| 7 | 本门店（nail_employee.store_id） |

## 2. 数据库 ER（业务）

- `nail_store` 1:N `nail_member` / `nail_employee` / `nail_order` / `nail_appointment`
- `nail_member_level` 1:N `nail_member`
- `nail_member` 1:N `nail_member_balance_log` / `nail_member_point_log`
- `nail_order` 1:N `nail_order_item`；1:N `nail_commission`
- `nail_employee` 可选关联 `sys_user.user_id`
- 系统表沿用若依：`sys_user`、`sys_role`、`sys_menu`、`sys_role_menu`、`sys_user_role`、`sys_dept`、`sys_oper_log`、`sys_logininfor`、`sys_dict_*`、`sys_config`

完整 DDL 与初始化数据：`script/sql/ry_vue_5.X.sql`、`script/sql/nail_business.sql`。

## 3. 接口清单（业务）

统一前缀无 context-path。分页均为 `GET .../list`，导出 `POST .../export`。

| 模块 | 路径 | 权限前缀 |
| --- | --- | --- |
| 门店 | `/store/store` | `store:store` |
| 会员 | `/member/member` | `member:member` |
| 会员充值 | `POST /member/member/recharge` | `member:member:recharge` |
| 余额调整 | `POST /member/member/adjust` | `member:member:adjust` |
| 生日提醒 | `GET /member/member/birthday` | `member:member:list` |
| 会员等级 | `/member/level` | `member:level` |
| 余额流水 | `/member/balanceLog` | `member:balance` |
| 积分流水 | `/member/pointLog` | `member:point` |
| 员工 | `/employee/employee` | `employee:employee` |
| 排班 | `/employee/shift` | `employee:shift` |
| 项目 | `/nail/item` | `nail:item` |
| 预约 | `/nail/appointment` | `nail:appointment` |
| 订单 | `/nail/order` | `nail:order` |
| 收银 | `POST /nail/order/checkout` | `nail:order:add` |
| 提成 | `/nail/commission` | `nail:commission` |
| 看板 | `GET /nail/dashboard/overview` | `nail:dashboard:list` |

系统管理接口复用若依：`/system/user`、`/system/role`、`/system/menu`、`/monitor/operlog`、`/monitor/logininfor` 等。

## 4. 前端路由与页面

动态路由来自后台菜单。页面文件：

| 菜单 | 组件 |
| --- | --- |
| 登录 | `plus-ui/src/views/login.vue` |
| 工作台 | `plus-ui/src/views/index.vue` |
| 营业看板 | `plus-ui/src/views/nail/dashboard/index.vue` |
| 用户/角色/菜单/字典/参数 | `views/system/*` |
| 操作/登录日志 | `views/monitor/operlog`、`logininfor` |
| 会员列表/等级/余额/积分 | `views/member/*` |
| 员工/排班 | `views/employee/*` |
| 门店/项目/预约/订单/提成 | `views/store`、`views/nail/*` |

按钮指令：`v-hasPermi="['member:member:add']"`。
