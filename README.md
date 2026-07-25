# serv-api

> 基于 Spring Boot 3.x 构建的企业级后端 API 服务框架

---

## 简介

`serv-api` 是一个开箱即用的后端 API 服务框架，基于 Spring Boot 3.x 构建。旨在帮助开发者快速搭建符合企业需求的后端服务，省去重复搭建项目的时间，专注于业务逻辑开发。

---

## 特性&提供

### 核心特性

- **JWT 无状态认证** - 集成 Spring Security + JWT 实现安全的无状态认证机制
- **请求日志切面** - 自动记录请求路径、参数、耗时等关键信息
- **敏感词脱敏** - 通过注解自动对敏感字段进行脱敏处理
- **熔断降级** - 基于 Resilience4j 实现服务熔断和限流控制
- **全局异常处理** - 统一的异常处理机制，返回标准化错误响应
- **分布式链路追踪** - 通过 TraceId 过滤器实现请求链路追踪
- **统一响应封装** - 统一的 API 响应格式，便于前端处理
- **统一异常处理** - 统一的异常处理机制，返回标准化错误响应
- **多环境配置** - 支持开发、测试、生产环境配置切换
- **Druid 数据库连接池监控** - 使用Druid Spring Boot Starter 集成Druid数据库连接池与监控

### 提供的能力

- 用户认证模块（登录、注册、注销）
- 健康检查接口
- 熔断降级测试接口
- Redis 缓存集成
- MySQL 数据库 CRUD 操作支持
- 多环境配置切换（开发/生产）
- 登录限流
- 审计日志
- 异常处理体系

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+

### 步骤

1. **克隆项目**

```bash
git clone https://github.com/your-username/serv-api.git
cd serv-api
```

2. **配置数据库**

修改 `src/main/resources/application-dev.yaml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/example_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

3. **初始化数据库**

执行 `db_schema/db.sql` 创建表结构。

4. **运行项目**

```bash
# 开发模式
mvn spring-boot:run

# 或打包运行
mvn clean package
java -jar target/serv-api-0.0.1-SNAPSHOT.jar
```

5. **验证服务**

```bash
curl http://localhost:8080/api/health
```

---

## 开发建议

### 目录结构

```
src/main/java/com/iy/api/
├── aspect/          # 切面类（日志、敏感词处理）
├── common/          # 通用组件（常量、枚举、工具类）
├── config/          # 配置类（安全、数据源、Redis等）
├── controller/      # REST API 控制层
├── filter/          # 过滤器（JWT、TraceId）
├── handler/         # 处理器（全局异常、MyBatis元数据）
├── mapper/          # MyBatis Plus Mapper
├── model/           # 数据模型（实体、VO、安全模型）
└── service/         # 业务逻辑层（接口+实现）
```

### 开发流程

1. 在 `model/entity` 中定义数据库实体
2. 在 `mapper` 中创建对应的 Mapper 接口
3. 在 `service` 中定义服务接口和实现
4. 在 `controller` 中编写 REST API
5. 使用 `@Sensitive` 注解标记需要脱敏的字段

### 配置管理

- 使用 `application.yaml` 作为主配置
- 通过 `spring.profiles.active` 切换环境配置
- 生产环境使用 `application-prod.yaml`

---

## 技术选型&文档

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | LTS 版本，性能稳定 |
| Spring Boot | 3.2.5 | 应用框架 |
| Spring Security | 6.x | 安全框架 |
| MyBatis Plus | 3.5.5 | ORM 框架 |
| MySQL | 8.0+ | 关系型数据库 |
| Redis | 6.0+ | 缓存数据库 |
| Druid | 1.2.18 | 数据库连接池 |
| Resilience4j | 2.1.0 | 熔断降级框架 |
| JWT | 0.12.5 | 身份认证 |

### 参考文档

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [MyBatis Plus 文档](https://baomidou.com/)
- [Resilience4j 文档](https://resilience4j.readme.io/)

---

## License

MIT License

```
MIT License

Copyright (c) 2024 serv-api

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
