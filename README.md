# 尚泰铭成ERP管理系统 - 后端项目

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-green.svg)](https://spring.io/projects/spring-boot)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.5.7-blue.svg)](https://mp.baomidou.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

> 基于芋道开源框架二次开发的企业级ERP管理系统后端项目

## 📖 项目简介

**尚泰铭成ERP管理系统**是一个基于 [芋道 ruoyi-vue-pro](https://gitee.com/zhijiantianya/ruoyi-vue-pro) 二次开发的企业级ERP管理平台，专注于销售订单管理、采购付款管理和数据统计分析。

### 🎯 核心功能

#### 1. 客户管理模块
- 客户信息CRUD
- 联系人管理
- 客户销售历史查询
- 数据导出

#### 2. 供应商管理模块
- 供应商信息CRUD
- 账期配置（按月、按周期自定义）
- 供应商采购统计
- 数据导出

#### 3. 订单管理模块
- **销售开单**：业务员快速创建销售订单
- **订单审核**：多级审核流程（业务员 → Boss）
- **成本填充**：订单完成后填充采购成本
- **成本编辑**：Boss和管理员可二次编辑已填充的成本
- **收款管理**：标注客户收款状态
- **数据导出**：Excel格式导出订单数据
- **订单详情**：查看订单完整信息

#### 4. 采购付款管理模块
- **自动生成付款单**：成本填充时按供应商自动生成
- **付款计划**：根据供应商账期自动拆分付款阶段
- **付款记录**：详细的付款历史追踪
- **同步更新**：成本编辑时自动同步更新付款单

#### 5. 统计报表模块
- **客户销售统计**：按客户统计销售额和利润
- **供应商采购统计**：按供应商统计采购成本
- **员工业绩统计**：销售业绩排行榜

#### 6. 系统管理模块
- 用户管理（CRUD、导入导出）
- 角色权限管理（菜单权限、数据权限）
- 部门管理（树形结构）
- 岗位管理
- 字典管理
- 操作日志
- 登录日志
- 文件管理
- 定时任务

---

## 🔨 二次开发说明

### 相比芋道源码的主要改动

#### 1. 精简模块

删除了以下不需要的模块，减少系统复杂度和资源占用：

- ❌ **AI模块** (`stmc-module-ai`)：聊天、写作、绘图、音乐、思维导图等
- ❌ **BPM工作流模块** (`stmc-module-bpm`)：流程设计器、流程审批、任务管理等
- ❌ **CRM模块** (`stmc-module-crm`)：线索、客户、商机、合同等（使用自定义简化版客户管理）
- ❌ **商城模块** (`stmc-module-mall`)：商品、订单、营销、优惠券等
- ❌ **会员模块** (`stmc-module-member`)：会员管理、等级、积分、签到等
- ❌ **支付模块** (`stmc-module-pay`)：支付宝/微信支付集成
- ❌ **IoT物联网模块** (`stmc-module-iot`)：设备管理、产品管理、数据采集等
- ❌ **公众号模块** (`stmc-module-mp`)：微信公众号、粉丝管理、消息推送等
- ❌ **租户管理**：SaaS多租户功能（单租户场景）
- ❌ **数据报表**：报表设计器、大屏设计器（暂不需要）

#### 2. 简化登录流程

**Controller层简化**：
- ❌ 移除手机号登录接口 (`smsLogin`)
- ❌ 移除社交授权登录接口 (`socialLogin`)
- ❌ 移除注册接口 (`register`)
- ❌ 移除发送短信验证码接口 (`sendSmsCode`)
- ✅ 保留账号密码登录 (`login`)
- ✅ 保留刷新令牌 (`refreshToken`)
- ✅ 保留登出 (`logout`)

#### 3. 核心ERP功能开发

##### 订单管理增强

**新增接口**：
- 🆕 `PUT /erp/order/edit-cost`：编辑订单成本（Boss和管理员）
  - 权限：`erp:order:edit-cost`
  - 业务逻辑：更新订单成本 + 物理删除旧付款记录 + 重新生成付款单

**业务流程优化**：
```
待审核(0) → 待填充成本(10) → 已完成(20)
                    ↓              ↓
              自动生成付款单      成本可随时编辑
                                  ↓
                            付款单同步更新
```

**关键Service方法**：
- `OrderServiceImpl.editOrderCost()`：成本编辑核心逻辑
  - 校验订单状态（必须为已完成）
  - 校验成本已填充
  - 校验供应商付款一致性
  - 更新订单明细成本
  - 更新订单汇总成本
  - 物理删除旧付款计划和付款单
  - 按新成本重新生成付款单

##### 付款管理自动化

**核心机制**：
- 🆕 **自动生成付款单**：成本填充时按供应商分组自动创建
  - `PaymentServiceImpl.createPaymentFromCostFill()`
  - 每个供应商生成一个付款单
  - 聚合该供应商所有商品的采购金额

- 🆕 **付款计划拆分**：根据供应商账期配置自动分期
  - `PaymentPlanServiceImpl.createSinglePaymentPlan()`
  - 支持单期付款（当前实现）
  - 预留多期付款扩展能力

- 🆕 **同步更新机制**：成本编辑时物理删除并重新生成
  - `PaymentMapper.deleteByOrderId()`
  - `PaymentPlanMapper.deleteByOrderId()`
  - 保证付款数据与订单成本强一致

- 🆕 **强一致性校验**：同供应商商品付款信息必须一致
  - 前端实现，后端信任前端数据
  - 同一供应商的所有商品：付款日期、付款状态、备注必须相同

##### 统计报表

**新增统计接口**：
- 🆕 `GET /erp/statistics/customer-sales`：客户销售统计
  - 按客户聚合销售额、成本、利润
  - 支持日期范围筛选
  - 支持导出Excel

- 🆕 `GET /erp/statistics/supplier-purchase`：供应商采购统计
  - 按供应商聚合采购金额
  - 支持日期范围筛选
  - 支持导出Excel

- 🆕 `GET /erp/statistics/employee-sales`：员工业绩统计
  - 按业务员统计销售业绩
  - 销售排名、业绩对比
  - 支持导出Excel

#### 4. Bug修复

**日期格式兼容性**：
- 🐛 修复后端返回日期数组格式 `[2026, 1, 15]` 导致前端日期选择器报错
- ✅ 统一使用字符串格式 `"2026-01-15"` 返回给前端
- ✅ 兼容数组和字符串两种格式的输入

**付款日期默认值**：
- 🐛 修复付款日期为null时的数据库约束错误
- ✅ 添加默认值处理逻辑

**文案优化**：
- 🎨 订单模块："付款状态" → "收款状态"（明确订单是收款，采购才是付款）
- 🎨 状态枚举："已付款/未付款" → "已收款/未收款"

---

## 💻 技术栈

### 核心框架

| 框架                                                          | 说明                  | 版本      |
|-------------------------------------------------------------|---------------------|---------|
| [Spring Boot](https://spring.io/projects/spring-boot)       | 应用开发框架              | 2.7.18  |
| [MyBatis Plus](https://mp.baomidou.com/)                    | MyBatis 增强工具包       | 3.5.7   |
| [Spring Security](https://spring.io/projects/spring-security) | 安全框架                | 5.7.11  |
| [Redis](https://redis.io/)                                  | 缓存数据库               | 6.0+    |
| [MySQL](https://www.mysql.com/)                             | 关系型数据库              | 8.0+    |
| [Druid](https://github.com/alibaba/druid)                   | 数据库连接池              | 1.2.23  |
| [Redisson](https://github.com/redisson/redisson)            | Redis客户端           | 3.32.0  |
| [Hibernate Validator](https://hibernate.org/validator/)      | 参数校验框架              | 6.2.5   |
| [Springdoc](https://springdoc.org/)                         | API文档（Swagger）      | 1.7.0   |
| [MapStruct](https://mapstruct.org/)                         | Java Bean转换        | 1.6.3   |
| [Lombok](https://projectlombok.org/)                        | 简化Java代码           | 1.18.34 |
| [Hutool](https://hutool.cn/)                                | Java工具库            | 5.8.25  |
| [Guava](https://github.com/google/guava)                    | Google工具库          | 33.2.1  |

### 项目模块

| 模块                      | 说明                    |
|-------------------------|------------------------|
| `stmc-dependencies`    | Maven依赖版本管理          |
| `stmc-framework`       | 框架扩展（安全、数据库、Redis等）  |
| `stmc-server`          | 启动类和配置               |
| `stmc-module-system`   | 系统管理模块（用户、角色、菜单、字典等） |
| `stmc-module-infra`    | 基础设施模块（文件、配置、定时任务等） |
| `stmc-module-erp`      | ERP核心模块（订单、客户、供应商等） |

**已删除模块**：
- ~~`stmc-module-ai`~~（AI大模型）
- ~~`stmc-module-bpm`~~（工作流）
- ~~`stmc-module-crm`~~（CRM）
- ~~`stmc-module-mall`~~（商城）
- ~~`stmc-module-member`~~（会员）
- ~~`stmc-module-pay`~~（支付）
- ~~`stmc-module-mp`~~（公众号）
- ~~`stmc-module-iot`~~（物联网）
- ~~`stmc-module-report`~~（报表）

---

## 🚀 快速开始

### 环境要求

- **JDK**：8 或 11+
- **Maven**：3.6+
- **MySQL**：8.0+
- **Redis**：6.0+
- **Node.js**：16.18+ （前端项目）

### 安装步骤

#### 1. 克隆项目

```bash
git clone https://github.com/huangguobing/stmc-boot-mini.git
cd stmc-boot-mini
```

#### 2. 创建数据库

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `stmc_erp` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. 导入SQL脚本

按顺序执行 `sql/mysql/` 目录下的脚本：

```bash
# 1. 基础表结构和数据
sql/mysql/stmc-erp-data.sql

# 2. 权限脚本（如果需要）
sql/mysql/add_order_edit_cost_permission.sql
```

#### 4. 修改配置

编辑 `stmc-server/src/main/resources/application-dev.yaml`：

```yaml
spring:
  # 数据库配置
  datasource:
    url: jdbc:mysql://localhost:3306/stmc_erp?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true
    username: root
    password: 你的数据库密码

  # Redis配置
  redis:
    host: localhost
    port: 6379
    password: 你的Redis密码（如果有）
```

#### 5. 编译运行

**使用Maven**：

```bash
# 编译打包
mvn clean package -DskipTests

# 运行
java -jar stmc-server/target/stmc-server.jar
```

**使用IDE**：

1. 导入Maven项目
2. 找到 `StmcServerApplication.java`
3. 右键运行

#### 6. 访问系统

- **后端地址**：http://localhost:48080
- **API文档**：http://localhost:48080/doc.html
- **默认账号**：
  - 超级管理员：`admin / admin123`
  - Boss角色：`boss / admin123`
  - 业务员：`salesman / admin123`

---

## 📁 项目结构

```
stmc-boot-mini/
├── sql/                                # SQL脚本
│   └── mysql/
│       ├── stmc-erp-data.sql          # 基础数据
│       └── deployment_*.sql            # 部署脚本
├── stmc-dependencies/                  # 依赖版本管理
├── stmc-framework/                     # 框架扩展
│   ├── stmc-common/                   # 通用工具
│   ├── stmc-spring-boot-starter-biz-data-permission/   # 数据权限
│   ├── stmc-spring-boot-starter-biz-dict/              # 字典
│   ├── stmc-spring-boot-starter-biz-operatelog/        # 操作日志
│   ├── stmc-spring-boot-starter-biz-tenant/            # 多租户（已禁用）
│   ├── stmc-spring-boot-starter-mybatis/               # MyBatis
│   ├── stmc-spring-boot-starter-redis/                 # Redis
│   ├── stmc-spring-boot-starter-security/              # 安全
│   └── stmc-spring-boot-starter-web/                   # Web
├── stmc-module-system/                 # 系统管理模块
│   ├── stmc-module-system-api/        # API接口定义
│   └── stmc-module-system-biz/        # 业务实现
│       ├── controller/admin/           # Controller层
│       │   ├── auth/                   # 认证授权
│       │   ├── dept/                   # 部门管理
│       │   ├── dict/                   # 字典管理
│       │   ├── logger/                 # 日志管理
│       │   ├── permission/             # 权限管理
│       │   └── user/                   # 用户管理
│       ├── service/                    # Service层
│       └── dal/mysql/                  # Mapper层
├── stmc-module-infra/                  # 基础设施模块
│   ├── stmc-module-infra-api/
│   └── stmc-module-infra-biz/
│       ├── controller/admin/
│       │   ├── config/                 # 配置管理
│       │   ├── file/                   # 文件管理
│       │   └── job/                    # 定时任务
│       ├── service/
│       └── dal/mysql/
├── stmc-module-erp/                    # ERP核心模块
│   ├── stmc-module-erp-api/
│   └── stmc-module-erp-biz/
│       ├── controller/admin/
│       │   ├── customer/               # 客户管理
│       │   ├── supplier/               # 供应商管理
│       │   ├── order/                  # 订单管理
│       │   │   ├── OrderController.java        # 订单CRUD、审核、成本填充/编辑
│       │   │   └── OrderItemController.java    # 订单明细
│       │   ├── payment/                # 付款管理
│       │   ├── paymentPlan/            # 付款计划
│       │   └── statistics/             # 统计报表
│       ├── service/
│       │   ├── order/
│       │   │   ├── OrderService.java
│       │   │   └── OrderServiceImpl.java       # 订单核心业务逻辑
│       │   ├── payment/
│       │   │   └── PaymentServiceImpl.java     # 付款单自动生成
│       │   └── paymentplan/
│       │       └── PaymentPlanServiceImpl.java # 付款计划自动拆分
│       ├── dal/mysql/
│       │   ├── order/
│       │   │   ├── OrderMapper.java
│       │   │   └── OrderItemMapper.java
│       │   ├── payment/
│       │   │   ├── PaymentMapper.java          # 新增deleteByOrderId()
│       │   │   └── PaymentPlanMapper.java      # 新增deleteByOrderId()
│       │   └── ...
│       └── enums/
│           ├── ErrorCodeConstants.java         # 错误码定义
│           └── order/
│               └── OrderStatusEnum.java        # 订单状态枚举
└── stmc-server/                        # 启动类
    ├── src/main/resources/
    │   ├── application.yaml            # 主配置
    │   ├── application-dev.yaml        # 开发环境
    │   └── application-prod.yaml       # 生产环境
    └── StmcServerApplication.java     # 启动类
```

---

## 🔑 核心业务逻辑

### 订单状态流转

```
待审核(0) → [Boss审核] → 待填充成本(10) → [填充成本] → 已完成(20)
                ↓                            ↓
            已驳回(-1)                  [编辑成本]
                                            ↓
                                       已完成(20)
```

### 成本填充流程

```java
// OrderServiceImpl.fillOrderCost()
1. 校验订单状态（必须为待填充成本）
2. 校验供应商付款一致性
3. 更新订单明细成本信息（采购单位、数量、单价、金额、供应商等）
4. 计算利润（毛利 = 销售金额 - 采购金额，净利 = 毛利 - 税额）
5. 更新订单汇总成本（总采购金额、总毛利、总税额、总净利）
6. 标记成本已填充（costFilled = true）
7. 按供应商分组聚合采购金额
8. 为每个供应商创建付款单（调用PaymentService）
```

### 成本编辑流程（新增）

```java
// OrderServiceImpl.editOrderCost()
1. 校验订单状态（必须为已完成）
2. 校验成本已填充
3. 校验供应商付款一致性
4. 更新订单明细成本信息（同填充逻辑）
5. 更新订单汇总成本
6. 物理删除该订单的所有付款计划（PaymentPlanMapper.deleteByOrderId）
7. 物理删除该订单的所有付款单（PaymentMapper.deleteByOrderId）
8. 按新的供应商分组重新生成付款单
```

### 付款单生成流程

```java
// PaymentServiceImpl.createPaymentFromCostFill()
1. 校验供应商存在
2. 生成付款单号（自动递增）
3. 创建付款单记录
   - 供应商ID
   - 订单ID
   - 付款金额（聚合该供应商所有商品采购金额）
   - 付款日期
   - 付款状态（已付款/未付款）
4. 创建单期付款计划（调用PaymentPlanService）
```

### 付款计划生成流程

```java
// PaymentPlanServiceImpl.createSinglePaymentPlan()
1. 生成付款计划编号
2. 创建付款计划记录
   - 付款单ID
   - 订单ID
   - 供应商ID
   - 阶段：stage = 1（单期）
   - 计划金额 = 付款单金额
   - 计划日期 = 付款日期
   - 状态 = 付款状态
```

---

## 🌐 环境配置

### 开发环境（application-dev.yaml）

```yaml
server:
  port: 48080

spring:
  # 数据库配置（本地）
  datasource:
    url: jdbc:mysql://localhost:3306/stmc_erp?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456

  # Redis配置（本地）
  redis:
    host: localhost
    port: 6379
    password:
```

### 测试环境（187服务器）

```yaml
spring:
  datasource:
    url: jdbc:mysql://192.168.1.187:3306/stmc_erp
    username: root
    password: 123456

  redis:
    host: 192.168.1.187
    port: 6379
```

### 生产环境（application-prod.yaml）

```yaml
server:
  port: 48080

spring:
  # 数据库配置（47服务器Docker）
  datasource:
    url: jdbc:mysql://localhost:3306/stmc_erp
    username: root
    password: Stmcerp666888.

  # Redis配置（47服务器Docker）
  redis:
    host: localhost
    port: 6379
```

**生产环境Docker部署**：
- MySQL容器：`stmc-mysql`
- Redis容器：`stmc-redis`
- 后端容器：`stmc-server`
- 前端容器：`stmc-ui`

---

## 📝 开发规范

### 代码规范

- 遵循《阿里巴巴Java开发手册》
- Controller层：仅处理HTTP请求，调用Service
- Service层：业务逻辑实现，事务控制
- Mapper层：数据访问，使用MyBatis-Plus
- VO/DTO：使用MapStruct进行Bean转换
- 异常处理：使用全局异常处理器
- 日志规范：使用Slf4j，区分info/warn/error

### 数据库规范

- 表名：`模块_表名`（如 `erp_order`）
- 字段命名：驼峰转下划线（如 `totalAmount` → `total_amount`）
- 主键：统一使用 `id` BIGINT
- 软删除：使用 `deleted` TINYINT（0=未删除，1=已删除）
- 审计字段：`creator`、`create_time`、`updater`、`update_time`
- 租户字段：`tenant_id`（当前未启用）

### Git提交规范

```
<type>(<scope>): <subject>

<body>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

**Type类型**：
- `feat`: 新功能
- `fix`: Bug修复
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `perf`: 性能优化
- `test`: 测试相关
- `chore`: 构建/工具链相关

---

## 🐛 已知问题

暂无

---

## 📅 更新日志

### v1.2.0 (2026-01-15)

**新增功能**：
- 🆕 订单成本编辑功能（`editOrderCost` API）
- 🆕 付款单物理删除+重新生成机制
- 🆕 Mapper层添加 `deleteByOrderId()` 方法
- 🆕 统计报表功能（客户销售、供应商采购、员工业绩）

**Bug修复**：
- 🐛 修复日期格式兼容性问题（支持数组和字符串格式）
- 🐛 修复付款日期默认值错误

**文案优化**：
- 🎨 "付款状态" → "收款状态"
- 🎨 状态枚举调整

**模块清理**：
- 🗑️ 删除AI、BPM、CRM、商城、支付、会员等未使用模块

### v1.1.0 (2026-01-13)

- 🆕 客户销售统计功能
- 🆕 供应商账期配置
- 🆕 付款计划自动拆分

### v1.0.0 (2026-01-11)

- 🎉 基于芋道框架完成初始开发
- ✅ 客户管理模块
- ✅ 供应商管理模块
- ✅ 订单管理模块
- ✅ 付款管理模块
- ✅ 系统管理模块

---

## 📞 技术支持

- 💬 问题反馈：[GitHub Issues](https://github.com/huangguobing/stmc-boot-mini/issues)
- 📧 联系邮箱：support@stmc.com

---

## 📄 开源协议

本项目基于 [Apache License 2.0](https://opensource.org/licenses/Apache-2.0) 开源协议

---

## 🙏 致谢

感谢 [芋道源码](https://gitee.com/zhijiantianya/ruoyi-vue-pro) 提供的优秀开源框架！

---

## ⭐ Star History

如果这个项目对你有帮助，请给个 Star ⭐ 支持一下！

---

## 🔗 相关项目

- **前端项目**：[stmc-ui-admin-vue3](https://github.com/huangguobing/stmc-ui-admin-vue3)
- **芋道源码**：[ruoyi-vue-pro](https://gitee.com/zhijiantianya/ruoyi-vue-pro)
