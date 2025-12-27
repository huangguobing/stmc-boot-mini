# 尚泰铭成ERP - 项目状态与工作计划

**文档版本**：V7.0
**最后更新**：2025年12月26日
**项目代号**：STMC-ERP
**目的**：本文档用于跟踪项目进度，确保新的 Claude CLI 会话可以无缝继续任务

---

## 一、项目概述

### 1.1 项目信息

| 项目 | 说明 |
|------|------|
| 项目名称 | 尚泰铭成ERP管理系统 |
| 品牌标识 | STMC (ShangtaiMingchen) |
| 基础框架 | 芋道 ruoyi-vue-pro 精简版 |
| 后端技术栈 | Spring Boot 2.7.18 + MyBatis-Plus + Redis |
| 前端技术栈 | Vue 3 + TypeScript + Vite + Element Plus |
| Java版本 | JDK 8 |

### 1.2 项目路径

```
G:/code/shangtaimingchen_erp/
├── stmc-boot-mini/          # 后端项目（已完成品牌重塑）
├── stmc-ui-admin-vue3/      # 前端项目
└── yudao-boot-mini/         # 空目录（可删除）
```

### 1.3 GitHub 仓库配置

| 项目 | 仓库地址 | 分支 |
|------|----------|------|
| 后端 | https://github.com/huangguobing/stmc-boot-mini | master |
| 前端 | https://github.com/huangguobing/stmc-ui-admin-vue3 | master |

---

## 二、已完成工作

### 阶段一：项目初始化 ✅

| 步骤 | 状态 | 说明 |
|------|------|------|
| 1. 下载后端框架 | ✅ | yudao-boot-mini 精简版 |
| 2. 下载前端项目 | ✅ | yudao-ui-admin-vue3 |
| 3. 禁用多租户 | ✅ | stmc.tenant.enable: false |
| 4. Maven插件降级 | ✅ | 兼容Maven 3.6.0 |
| 5. 验证编译 | ✅ | BUILD SUCCESS |

**Maven插件版本调整（重要）**：
```xml
flatten-maven-plugin: 1.6.0 → 1.5.0
maven-compiler-plugin: 3.14.0 → 3.11.0
maven-surefire-plugin: 3.5.3 → 3.0.0
```

### 阶段二：品牌重塑 (yudao → stmc) ✅

| 步骤 | 状态 | 影响范围 | 说明 |
|------|------|----------|------|
| Step 1: 目录重命名 | ✅ | 20+目录 | yudao-* → stmc-* |
| Step 2: pom.xml替换 | ✅ | 20个文件 | artifactId 全部替换 |
| Step 3: Java包名重构 | ✅ | 956+文件 | cn.iocoder.yudao → cn.iocoder.stmc |
| Step 4: Java类名重命名 | ✅ | 36个文件 | YudaoXxx.java → StmcXxx.java |
| Step 5: 配置文件修改 | ✅ | 2个文件 | application*.yaml |
| Step 6: META-INF修改 | ✅ | 14个文件 | AutoConfiguration.imports |
| Step 7: 验证编译 | ✅ | 20个模块 | BUILD SUCCESS (52.9s) |

**品牌重塑详细改动**：

1. **目录结构**：
   ```
   stmc-boot-mini/
   ├── stmc-dependencies/
   ├── stmc-framework/
   │   ├── stmc-common/
   │   ├── stmc-spring-boot-starter-web/
   │   ├── stmc-spring-boot-starter-security/
   │   ├── stmc-spring-boot-starter-mybatis/
   │   ├── stmc-spring-boot-starter-redis/
   │   ├── stmc-spring-boot-starter-mq/
   │   ├── stmc-spring-boot-starter-job/
   │   ├── stmc-spring-boot-starter-biz-tenant/
   │   ├── stmc-spring-boot-starter-websocket/
   │   ├── stmc-spring-boot-starter-monitor/
   │   ├── stmc-spring-boot-starter-protection/
   │   ├── stmc-spring-boot-starter-biz-ip/
   │   ├── stmc-spring-boot-starter-excel/
   │   └── stmc-spring-boot-starter-biz-data-permission/
   ├── stmc-module-infra/
   ├── stmc-module-system/
   └── stmc-server/
   ```

2. **配置前缀**：
   - `yudao.*` → `stmc.*`
   - `${yudao.info.base-package}` → `${stmc.info.base-package}`
   - `@ConfigurationProperties("yudao.xxx")` → `@ConfigurationProperties("stmc.xxx")`

3. **关键类名映射**：
   ```
   StmcServerApplication (启动类)
   StmcWebAutoConfiguration
   StmcSecurityAutoConfiguration
   StmcMybatisAutoConfiguration
   StmcCacheAutoConfiguration
   StmcRedisAutoConfiguration
   StmcTenantAutoConfiguration
   StmcWebSocketAutoConfiguration
   ... (共36个类)
   ```

### 阶段三：代码提交 GitHub ✅

| 步骤 | 状态 | 说明 |
|------|------|------|
| 1. 创建业务状态文件 | ✅ | PROJECT_STATUS.md |
| 2. 后端代码提交 | ✅ | stmc-boot-mini → GitHub |
| 3. 前端代码提交 | ✅ | stmc-ui-admin-vue3 → GitHub |

---

## 三、待完成工作

### 阶段四：创建 ERP 业务模块 ⏳

| 步骤 | 状态 | 说明 |
|------|------|------|
| 1. 创建 stmc-module-erp | ⏳ 待执行 | 后端业务模块 |
| 2. 数据库表设计 | ⏳ 待执行 | customers, suppliers, orders, payments |
| 3. 创建实体类 | ⏳ 待执行 | DO/VO/DTO |
| 4. 创建 Mapper | ⏳ 待执行 | MyBatis-Plus |
| 5. 创建 Service | ⏳ 待执行 | 业务逻辑层 |
| 6. 创建 Controller | ⏳ 待执行 | REST API |

**ERP模块结构规划**：
```
stmc-module-erp/
├── src/main/java/cn/iocoder/stmc/module/erp/
│   ├── controller/admin/
│   │   ├── customer/     # 客户管理 API
│   │   ├── supplier/     # 供应商管理 API
│   │   ├── order/        # 订单管理 API
│   │   └── payment/      # 付款管理 API
│   ├── service/
│   │   ├── customer/
│   │   ├── supplier/
│   │   ├── order/
│   │   └── payment/
│   ├── dal/
│   │   ├── dataobject/   # 实体类
│   │   └── mysql/        # Mapper
│   └── convert/          # 对象转换器
└── pom.xml
```

### 阶段五：创建前端业务页面 ⏳

| 步骤 | 状态 | 说明 |
|------|------|------|
| 1. 客户管理页面 | ⏳ 待执行 | CRUD + 搜索 |
| 2. 供应商管理页面 | ⏳ 待执行 | CRUD + 搜索 |
| 3. 订单管理页面 | ⏳ 待执行 | CRUD + 状态流转 |
| 4. 付款管理页面 | ⏳ 待执行 | CRUD + 审批 |
| 5. 统计报表页面 | ⏳ 待执行 | 图表展示 |

**前端页面路径规划**：
```
stmc-ui-admin-vue3/src/views/erp/
├── customer/
│   ├── index.vue         # 客户列表
│   └── CustomerForm.vue  # 客户表单
├── supplier/
│   ├── index.vue
│   └── SupplierForm.vue
├── order/
│   ├── index.vue
│   └── OrderForm.vue
└── payment/
    ├── index.vue
    └── PaymentForm.vue
```

### 阶段六：菜单与权限配置 ⏳

| 步骤 | 状态 | 说明 |
|------|------|------|
| 1. 创建菜单数据 | ⏳ 待执行 | system_menu 表 |
| 2. 配置权限 | ⏳ 待执行 | 三种角色权限 |
| 3. 测试验证 | ⏳ 待执行 | 完整功能测试 |

**角色权限规划**：
- **超级管理员**：全部权限
- **销售人员**：客户管理、订单管理（只读供应商）
- **采购人员**：供应商管理、付款管理（只读客户）

---

## 四、技术参考

### 4.1 关键配置文件路径

| 文件 | 路径 | 说明 |
|------|------|------|
| 主配置 | stmc-server/src/main/resources/application.yaml | 应用配置 |
| 本地配置 | stmc-server/src/main/resources/application-local.yaml | 本地环境 |
| 依赖管理 | stmc-dependencies/pom.xml | Maven BOM |
| 启动类 | stmc-server/src/main/java/cn/iocoder/stmc/server/StmcServerApplication.java | 主入口 |

### 4.2 数据库配置

```yaml
# application-local.yaml
spring:
  datasource:
    dynamic:
      datasource:
        master:
          url: jdbc:mysql://127.0.0.1:3306/ruoyi-vue-pro
          username: root
          password: 123456
```

### 4.3 禁用的功能

```yaml
stmc:
  tenant:
    enable: false  # 多租户已禁用
```

---

## 五、注意事项

1. **Maven 版本**：需要 Maven 3.6.0+，插件已降级兼容
2. **JDK 版本**：使用 JDK 8
3. **数据库**：需要 MySQL 数据库，默认库名 ruoyi-vue-pro

---

## 六、新会话继续指南

如果在新的 Claude CLI 会话中继续工作：

1. **读取状态文件**：
   ```
   请读取 G:/code/shangtaimingchen_erp/stmc-boot-mini/PROJECT_STATUS.md 了解项目状态
   ```

2. **继续当前任务**：
   - 如果阶段三未完成：继续提交代码到 GitHub
   - 如果阶段三已完成：开始阶段四创建 ERP 业务模块

3. **验证项目状态**：
   ```bash
   cd G:/code/shangtaimingchen_erp/stmc-boot-mini
   mvn clean compile -DskipTests
   ```

---

**当前状态**：阶段三已完成，准备执行阶段四
