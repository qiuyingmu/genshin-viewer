# 原神查看器 v3 — Vue + Spring Boot 全栈工程化

## 项目结构

```
genshin-server/               # Spring Boot 3 后端
├── pom.xml                   # Maven 依赖
├── src/main/resources/
│   ├── application.yml       # 配置（dev/prod 双环境）
│   └── db/schema.sql         # 数据库建表
└── src/main/java/com/genshin/
    ├── GenshinApplication.java
    ├── config/               # Security, CORS, MyBatis-Plus, Health
    ├── common/               # Result, BusinessException, GlobalExceptionHandler
    ├── security/             # JWT, AuthFilter, UserDetailsService
    ├── aop/                  # @OperationLog + Aspect
    └── modules/
        ├── user/             # 登录/注册/刷新/me
        ├── artifact/         # 圣遗物 CRUD
        ├── character/        # 角色 CRUD
        ├── gacha/            # 抽卡导入/统计
        ├── uid/              # Enka API 代理
        └── operationlog/     # 操作日志查询

genshin-app/                  # Vue 3 + Vite 前端
├── package.json
├── vite.config.js            # API 代理配置
└── src/
    ├── main.js / App.vue     # Three.js 粒子背景
    ├── router/               # 路由守卫
    ├── stores/               # Pinia auth store
    ├── api/                  # Axios 拦截器 + 自动刷新 Token
    ├── views/                # Login, Dashboard, Artifacts, Gacha, Characters
    └── styles/main.css       # 全局样式
```

## 工程质量保障

| 维度 | 实现 |
|------|------|
| 安全 | Spring Security + JWT 双令牌 (access/refresh), BCrypt 加密, CORS 白名单 |
| 质量 | 全局统一异常处理, 统一 Result 返回体, Jakarta Validation 参数校验 |
| 运维 | AOP 操作日志全记录 (谁/什么时候/做了什么/耗时), /health + /ready 健康检查 |
| 性能 | MyBatis-Plus 连接池, 数据库索引, Three.js 粒子优化 |
| 存储 | 开发环境 H2 嵌入式, 生产环境 MySQL 可切换 |

## 启动方式

```bash
# 1. 启动后端
cd genshin-server
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot
mvn spring-boot:run

# 2. 启动前端 (新开一个终端)
cd genshin-app
npm install
npm run dev

# 3. 浏览器访问
http://localhost:5173
```

## API 一览

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/register | 注册 |
| POST | /api/auth/login | 登录 |
| POST | /api/auth/refresh | 刷新令牌 |
| GET | /api/auth/me | 当前用户信息 |
| GET | /api/uid/{uid} | 代理 Enka UID 查询 |
| GET/POST/DELETE | /api/artifacts | 圣遗物 CRUD |
| GET/POST/DELETE | /api/characters | 角色 CRUD |
| GET | /api/gacha | 抽卡列表 |
| POST | /api/gacha/import | 批量导入抽卡 |
| GET | /api/gacha/stats | 抽卡统计 |
| GET | /api/operations | 操作日志 |
| GET | /health | 健康检查 |
| GET | /ready | 就绪检查 |
