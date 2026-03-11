# Campus（后端）- 大学新生智慧迎新平台（Agent + 管理后台）

> 后端仓库：`lcx-defender/campus`  
> 技术栈：Spring Boot / Spring Security / JWT / Redis / MyBatis-Plus / MySQL / Spring AI / x-file-storage

## 1. 项目简介
Campus 是一个面向高校迎新与咨询场景的后台服务，提供账号登录、权限控制、迎新业务数据管理、文件上传与存储、登录/操作审计日志，以及基于大模型的智能问答/对话能力接入。

## 2. 核心功能
- 认证与鉴权：Spring Security + JWT；支持接口权限校验、在线用户与强制下线
- 业务模块：迎新相关信息管理（学生、教师、部门/组织、宿舍等）
- 文件服务：基于 x-file-storage 统一对接本地/OSS 等存储平台（支持头像/图片等上传）
- 日志审计：
    - 登录日志：记录登录/退出、IP、地点、浏览器/操作系统等
    - 操作日志：基于 AOP + 自定义注解记录关键接口的请求/响应/异常/耗时等
- AI 能力：通过 Spring AI 接入大模型（支持聊天模型与向量模型配置，为智能问答/Agent 做基础能力）

## 3. 技术栈
- Java / Spring Boot
- Spring Security + JWT
- Redis（登录态/在线用户/缓存等）
- MyBatis-Plus + MyBatis XML Mapper
- MySQL（Druid 连接池）
- Spring AI（OpenAI compatible / Ollama）
- x-file-storage（Aliyun OSS 等）
- 其他：Lombok、Hutool、Fastjson2 等

## 4. 目录结构（示例）

- `src/main/java/com/lcx/campus`
    - `controller/` 接口层
    - `service/` 业务层
    - `mapper/` 数据访问层（含 XML 对应）
    - `domain/` 实体/DTO/VO
    - `config/` 配置类（如 MyBatis-Plus 分页拦截器等）
    - `aspect/` AOP 日志切面（操作审计）
    - `handler/` 安全/异常处理（如退出处理器等）
    - `utils/` 工具类（异步日志工厂、IP 解析等）
- `src/main/resources`
    - `mapper/` MyBatis XML
    - `sql/` 数据库初始化脚本（如 `campus.sql`）

## 5. 环境准备
- JDK：17+（建议与项目实际 `pom.xml` 对齐）
- Maven：3.8+
- MySQL：8.x
- Redis：6.x+
- （可选）Ollama：本地模型服务（如果使用本地大模型）
- （可选）阿里云 OSS：如果使用对象存储

## 6. 配置文件（关键项）

建议做法：
1) 新建 `application-dev.yml`
2) 在 `application.yml` 中激活 dev profile（如 `spring.profiles.active=dev`）
3) 将 `appliaction-template.yml` 中的配置项复制到 `application-dev.yml`，并根据本地环境修改

> 注意：密钥与密码不要提交到仓库。

## 7. 数据库初始化
1) 执行初始化脚本：
- `src/main/resources/sql/campus.sql`

## 8. 启动方式
### 8.1 本地启动（推荐）
1) 启动 Redis
2) 启动 MySQL 并导入 SQL
3) 配置 `application-dev.yml`
4) 使用 IDE 运行 Spring Boot 主类

### 8.2 常见端口
- 后端服务：默认 8088（可在application.yml中修改）

## 9. 接口联调说明
- 前端通过代理访问后端（见前端仓库 `vite.config.js`）
- 需要 token 的接口：前端在请求拦截器里统一加 `Authorization`

## 10. 关键实现说明
- 操作日志（AOP）：`@Log` 注解 + `LogAspect` 拦截并异步落库 `sys_operate_log`
- 登录日志：`AsyncFactory.recordLoginInfo` 异步记录 `sys_login_info`
- 在线用户：Redis 存储登录态，支持查询与强制下线
- MyBatis-Plus：分页拦截器 + 业务分页查询

## 11. 常见问题
- 401 / 无权限：检查 token 是否携带、过期时间、后端权限配置
- Redis 连接失败：检查 host/port/password 与防火墙
- AI 调用失败：检查 API Key、base-url 是否可达、模型名是否正确
- 文件上传失败：检查 x-file-storage 平台配置与 OSS 权限
- 超级管理员 `360123` 密码为 `admin123456`（建议修改）