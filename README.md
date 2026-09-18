# 君君美甲后台管理系统

基于 **RuoYi-Vue-Plus 5.6.2** 二次开发的美甲门店经营管理后台。鉴权使用 **Sa-Token**（非 Spring Security / Shiro），前后端分离。

## 技术栈

| 层级 | 版本与组件 |
| --- | --- |
| 后端 | JDK 17+、Spring Boot 3.5、Sa-Token 1.45、MyBatis-Plus、MySQL 8、Redis、Hutool、Knife4j/SpringDoc、FastExcel |
| 前端 | Vue 3、Vite、TypeScript、Pinia、Vue Router、Element Plus、Axios、ECharts |
| 复用 | 动态菜单、角色权限、用户/字典/参数、操作日志、登录日志、分页、统一响应、代码生成 |

## 默认账号

- 账号：`admin`
- 密码：`admin123`
- 超级管理员角色拥有全部菜单与按钮权限
- 首次登录会强制修改初始密码

## 本地启动

依赖：JDK 17 或 21、Maven 3.8+、Node.js 20+、MySQL 8、Redis（密码 `ruoyi123`）、MinIO。

一键拉起（推荐，环境重置后可恢复）：

```bash
bash script/bin/start-dev.sh
```

或按步骤启动：

```bash
# 1. 初始化数据库
mysql -uroot -proot -e "CREATE DATABASE IF NOT EXISTS \`ry-vue\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
mysql -uroot -proot ry-vue < script/sql/ry_vue_5.X.sql
mysql -uroot -proot ry-vue < script/sql/ry_workflow.sql
mysql -uroot -proot ry-vue < script/sql/nail_business.sql

# 2. 启动 Redis（需密码 ruoyi123）
redis-server --requirepass ruoyi123

# 3. 启动 MinIO
bash script/bin/start-minio.sh

# 4. 启动后端（默认端口 18080）
mvn -pl ruoyi-admin -am -DskipTests package
java -jar ruoyi-admin/target/ruoyi-admin.jar --spring.profiles.active=dev

# 5. 启动前端（默认端口 18418）
cd plus-ui
npm install
npm run dev
```

浏览器访问 `http://localhost:18418`。接口文档：`http://localhost:18080/doc.html`。

开发环境配置见 `ruoyi-admin/src/main/resources/application-dev.yml`（数据源 `root/root`，库名 `ry-vue`）。

文件上传默认使用 **MinIO** 对象存储（`sys_oss_config.config_key=minio`，账号 `ruoyi/ruoyi123`，桶 `ruoyi`，端口 `9000`）。本地开发先启动：

```bash
bash script/bin/start-minio.sh
```

若已有旧库被切到 local，可执行 `script/sql/fix_oss_minio.sql` 切回 MinIO。

## Docker 部署

```bash
mvn -pl ruoyi-admin -am -DskipTests package
docker compose up -d --build
```

前端映射 `18480`，后端 `18080`。首次启动会自动执行 `script/sql` 下初始化脚本。

## 模块说明

- **复用**：`ruoyi-admin`、`ruoyi-system`、`ruoyi-common-*`、plus-ui 系统管理页面
- **新增**：`ruoyi-modules/ruoyi-nail` 美甲业务；`plus-ui/src/views/{member,employee,nail,store}`
- **改造**：登录强制改密、工作台看板、角色数据权限增加「本门店」、品牌与端口、关闭本地 SnailJob / Admin 客户端依赖

详细架构、DDL、接口与页面清单见 `docs/ARCHITECTURE.md`。

## 权限标识示例

- `system:user:list` / `system:user:add`
- `member:member:edit` / `member:member:recharge`
- `employee:employee:remove`
- `nail:order:add`（收银下单）
