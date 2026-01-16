# 订单成本编辑功能 + Boss角色权限修复 - 生产环境部署文档

**部署日期**: 2026-01-15
**功能**:
1. 允许Boss和超级管理员在成本填充后编辑订单成本，并自动同步付款单和付款计划
2. 修复Boss角色统计报表菜单权限问题（包含之前的修改）

---

## 📦 一、修改内容清单

### 后端修改（Java）

#### 1. 新增功能
- **OrderController.java** - 新增 `editOrderCost` API（第253行）
- **OrderService.java** - 新增 `editOrderCost` 接口方法
- **OrderServiceImpl.java** - 实现 `editOrderCost` 方法（第447-566行）
  - 简化版策略：物理删除旧付款记录 + 重新生成
  - 不修改订单的 `paidAmount` 字段（保持客户付款金额）
- **PaymentMapper.java** - 新增 `deleteByOrderId()` 方法（第54-57行）
- **PaymentPlanMapper.java** - 新增 `deleteByOrderId()` 方法（第85-88行）
- **ErrorCodeConstants.java** - 新增2个错误码（1_020_003_010 ~ 1_020_003_011）

#### 2. Bug修复
- **PaymentServiceImpl.java** - 修复 `createPaymentFromCostFill` 方法（第205行）
  - 当 `paymentDate` 为 null 时，使用当前日期
  - 避免数据库 NOT NULL 约束错误

### 前端修改（Vue 3）

#### 1. 新增功能
- **src/views/erp/order/index.vue**
  - 添加"编辑"按钮（已完成且已填充成本的订单）（第178-186行）
  - 新增 `openEditCostForm` 方法（第359-361行）
- **src/views/erp/order/CostForm.vue**
  - 支持编辑模式（`isEditMode`）（第236、243-245行）
  - 提交时调用不同API（第398-404行）
- **src/api/erp/order/index.ts** - 新增 `editOrderCost` API方法
- **src/router/modules/remaining.ts** - 新增订单路由（第100-133行）
  - `/order/create` - 销售开单
  - `/order/edit/:id` - 编辑订单

#### 2. 文案优化
- **src/views/erp/order/index.vue**
  - "付款状态" → "收款状态"（第75行）
  - "已付款/未付款" → "已收款/未收款"（第294-295行）
  - "标注已付款" → "标注已收款"（第136行）
  - 函数重命名：`getPaymentStatusText` → 保持名称但返回"收款"文案

#### 3. Bug修复
- **src/views/erp/order/CostForm.vue** - 修复日期格式处理（第346-357行）
  - 支持数组格式：`[2026, 1, 15]` → `"2026-01-15"`
  - 支持字符串格式：`"2026-01-15 10:30:00"` → `"2026-01-15"`

### 数据库脚本

#### 1. 完整部署脚本（推荐使用）

- **sql/deployment_187_20260115.sql**（187测试环境）
  - 成本编辑权限（复用 `menu_id=5047`）
  - 统计报表权限（menu_id: 5063-5066）
  - 自动为超级管理员和Boss角色分配权限

- **sql/deployment_47_20260115.sql**（47生产环境）
  - 成本编辑权限（复用 `menu_id=5047`）
  - 统计报表权限（menu_id: 5063, 5065, 5066, **5075**）
  - ⚠️ 注意：47生产环境的客户销售统计menu_id是5075（与187不同）

#### 2. 历史SQL脚本（仅供参考）

- **sql/update_order_edit_cost_permission.sql** - 仅包含成本编辑权限（不包含统计报表）
- **sql/fix_boss_role_permissions.sql** - 187统计报表权限修复
- **sql/fix_boss_role_permissions_for_47.sql** - 47客户销售统计权限修复

---

## 🔧 二、部署步骤

### A. 187测试环境部署

#### 1. 备份数据库（可选）
```bash
mysqldump -h 192.168.1.187 -u root -p stmc_erp > backup_187_20260115.sql
```

#### 2. 执行SQL脚本
```bash
# 连接到187数据库
mysql -h 192.168.1.187 -u root -p stmc_erp

# 执行完整部署脚本（包含成本编辑 + 统计报表权限）
source sql/deployment_187_20260115.sql;

# 或直接在Navicat中执行
```

**预期结果：**
```sql
-- 验证成本编辑权限
SELECT id, name, permission FROM system_menu WHERE id = 5047;
-- 结果：5047 | 成本编辑 | erp:order:edit-cost

-- 验证统计报表菜单
SELECT id, name, parent_id FROM system_menu WHERE id BETWEEN 5063 AND 5066;
-- 结果：4条记录（统计报表、客户销售统计、供应商采购统计、员工销售统计）

-- 验证Boss角色权限
SELECT COUNT(*) FROM system_role_menu
WHERE role_id = 2 AND menu_id IN (5047, 5063, 5064, 5065, 5066);
-- 结果：5（所有权限都已分配）
```

#### 3. 上传后端jar包
```bash
# 本地编译
cd G:\code\shangtaimingchen_erp\stmc-boot-mini
mvn clean package -DskipTests

# 上传到187服务器
scp stmc-server/target/stmc-server.jar root@192.168.1.187:/app/erp/
```

#### 4. 重启187后端服务
```bash
# SSH连接到187
ssh root@192.168.1.187

# 停止服务
systemctl stop stmc-erp
# 或
kill -9 $(ps -ef | grep stmc-server.jar | grep -v grep | awk '{print $2}')

# 清空Redis缓存
redis-cli FLUSHALL

# 启动服务
systemctl start stmc-erp
# 或
nohup java -jar /app/erp/stmc-server.jar > /app/erp/logs/app.log 2>&1 &

# 查看日志确认启动成功
tail -f /app/erp/logs/app.log
```

#### 5. 上传前端dist包（如需要）
```bash
# 本地打包
cd G:\code\shangtaimingchen_erp\stmc-ui-admin-vue3
npm run build:prod

# 上传到187服务器
scp -r dist/* root@192.168.1.187:/var/www/html/erp/
```

#### 6. 功能测试
- [ ] 使用Boss账号登录
- [ ] 创建订单并审核通过
- [ ] 填充成本（勾选"已付款"）
- [ ] 验证订单列表显示"收款状态"列
- [ ] 点击"编辑"按钮，修改成本
- [ ] 验证付款单和付款计划已更新
- [ ] 测试日期选择器不报错

---

### B. 47生产环境部署

**⚠️ 重要提醒：生产环境部署需要在业务低峰期进行（建议夜间22:00-次日6:00）**

#### 1. 备份47生产数据库（必须！）
```bash
# 完整备份
mysqldump -h 192.168.1.47 -u root -p stmc_erp > backup_47_prod_20260115_$(date +%H%M%S).sql

# 备份重要表
mysqldump -h 192.168.1.47 -u root -p stmc_erp \
  system_menu system_role_menu erp_order erp_payment erp_payment_plan \
  > backup_47_prod_tables_20260115.sql
```

#### 2. 执行SQL脚本
```bash
# 连接到47生产数据库
mysql -h 192.168.1.47 -u root -p stmc_erp

# 执行完整部署脚本（包含成本编辑 + 统计报表权限）
# ⚠️ 注意：47生产环境的客户销售统计menu_id是5075（与187不同）
source sql/deployment_47_20260115.sql;

# 或直接在Navicat中执行
```

**预期结果：**
```sql
-- 验证成本编辑权限
SELECT id, name, permission FROM system_menu WHERE id = 5047;
-- 结果：5047 | 成本编辑 | erp:order:edit-cost

-- 验证统计报表菜单
SELECT id, name, parent_id FROM system_menu
WHERE id IN (5063, 5065, 5066, 5075) AND deleted = 0;
-- 结果：4条记录
--   5063 | 统计报表 | 0
--   5065 | 供应商采购统计 | 5063
--   5066 | 员工销售统计 | 5063
--   5075 | 客户销售统计 | 5063

-- 验证Boss角色权限（包含成本编辑 + 统计报表）
SELECT rm.role_id, r.name AS role_name, m.id AS menu_id, m.name AS menu_name
FROM system_role_menu rm
JOIN system_role r ON rm.role_id = r.id
JOIN system_menu m ON rm.menu_id = m.id
WHERE rm.role_id = 2 AND m.id IN (5047, 5063, 5065, 5066, 5075) AND rm.deleted = 0;
-- 结果：5条记录（所有权限都已分配）
```

#### 3. 上传后端jar包
```bash
# 上传到47服务器
scp stmc-server/target/stmc-server.jar root@192.168.1.47:/app/erp/stmc-server-new.jar

# 备份旧版本
ssh root@192.168.1.47
cp /app/erp/stmc-server.jar /app/erp/stmc-server.jar.backup.$(date +%Y%m%d)
mv /app/erp/stmc-server-new.jar /app/erp/stmc-server.jar
```

#### 4. 重启47后端服务
```bash
# 停止服务
systemctl stop stmc-erp

# 清空Redis缓存
redis-cli FLUSHALL

# 启动服务
systemctl start stmc-erp

# 查看日志
tail -f /app/erp/logs/app.log
```

#### 5. 上传前端dist包
```bash
# 备份旧版本
ssh root@192.168.1.47
mv /var/www/html/erp /var/www/html/erp.backup.$(date +%Y%m%d)

# 上传新版本
scp -r dist/* root@192.168.1.47:/var/www/html/erp/
```

#### 6. 生产验证
- [ ] 使用Boss账号登录（清除浏览器缓存）
- [ ] 验证现有订单数据正常显示
- [ ] 测试新功能：编辑已填充成本的订单
- [ ] 验证付款单同步更新
- [ ] 通知用户新功能上线

---

## 🔍 三、验证清单

### 功能验证

#### 1. 权限验证
- [ ] Boss账号可以看到"编辑"按钮（已完成且已填充成本的订单）
- [ ] 普通业务员看不到"编辑"按钮
- [ ] 超级管理员可以看到"编辑"按钮

#### 2. 成本编辑验证
- [ ] 点击"编辑"按钮，成本编辑表单正常打开
- [ ] 修改采购金额，提交成功
- [ ] 订单成本汇总字段已更新（`totalPurchaseAmount`, `totalGrossProfit` 等）
- [ ] 订单的 `paidAmount` 字段未被修改（保持客户付款金额）

#### 3. 付款单同步验证
- [ ] 编辑成本后，旧的付款单已被物理删除
- [ ] 按新的供应商分组重新生成付款单
- [ ] 付款单的 `amount` 字段等于新的采购金额
- [ ] 付款计划同步更新

#### 4. 日期选择器验证
- [ ] 点击付款日期选择器，不再报错
- [ ] 可以正常选择日期
- [ ] 日期回显正确（格式：YYYY-MM-DD）

#### 5. 文案验证
- [ ] 订单列表显示"收款状态"列（而非"付款状态"）
- [ ] 状态显示"已收款"或"未收款"
- [ ] 按钮显示"标注已收款"

### 数据一致性验证

```sql
-- 验证订单成本汇总
SELECT
    o.id,
    o.order_no,
    o.total_purchase_amount,
    SUM(oi.purchase_amount) AS calc_total_purchase_amount,
    o.total_purchase_amount = SUM(oi.purchase_amount) AS is_match
FROM erp_order o
JOIN erp_order_item oi ON o.id = oi.order_id
WHERE o.cost_filled = 1
GROUP BY o.id
HAVING is_match = 0;

-- 应该返回空结果（所有订单成本汇总正确）

-- 验证付款单金额
SELECT
    p.id,
    p.payment_no,
    p.amount AS payment_amount,
    SUM(oi.purchase_amount) AS calc_payment_amount,
    p.amount = SUM(oi.purchase_amount) AS is_match
FROM erp_payment p
JOIN erp_order_item oi ON p.order_id = oi.order_id AND p.supplier_id = oi.supplier_id
GROUP BY p.id
HAVING is_match = 0;

-- 应该返回空结果（所有付款单金额正确）
```

---

## ⚠️ 四、回滚方案

### 如果生产环境出现问题，立即回滚：

#### 1. 回滚后端
```bash
ssh root@192.168.1.47
systemctl stop stmc-erp
cp /app/erp/stmc-server.jar.backup.$(date +%Y%m%d) /app/erp/stmc-server.jar
systemctl start stmc-erp
```

#### 2. 回滚前端
```bash
ssh root@192.168.1.47
rm -rf /var/www/html/erp
mv /var/www/html/erp.backup.$(date +%Y%m%d) /var/www/html/erp
```

#### 3. 回滚数据库
```sql
-- 删除新增的权限
DELETE FROM system_role_menu WHERE menu_id = 5047;
UPDATE system_menu SET name = '补充采购信息', permission = 'erp:order:add-purchase' WHERE id = 5047;

-- 或恢复完整备份（如果数据库有问题）
mysql -h 192.168.1.47 -u root -p stmc_erp < backup_47_prod_20260115_XXXXXX.sql
```

#### 4. 清空Redis缓存
```bash
redis-cli FLUSHALL
```

---

## 📊 五、关键文件清单

### 后端文件
- `stmc-module-erp/src/main/java/cn/iocoder/stmc/module/erp/controller/admin/order/OrderController.java`
- `stmc-module-erp/src/main/java/cn/iocoder/stmc/module/erp/service/order/OrderService.java`
- `stmc-module-erp/src/main/java/cn/iocoder/stmc/module/erp/service/order/OrderServiceImpl.java`
- `stmc-module-erp/src/main/java/cn/iocoder/stmc/module/erp/dal/mysql/payment/PaymentMapper.java`
- `stmc-module-erp/src/main/java/cn/iocoder/stmc/module/erp/dal/mysql/paymentplan/PaymentPlanMapper.java`
- `stmc-module-erp/src/main/java/cn/iocoder/stmc/module/erp/service/payment/PaymentServiceImpl.java`
- `stmc-module-erp/src/main/java/cn/iocoder/stmc/module/erp/enums/ErrorCodeConstants.java`

### 前端文件
- `src/views/erp/order/index.vue`
- `src/views/erp/order/CostForm.vue`
- `src/api/erp/order/index.ts`
- `src/router/modules/remaining.ts`

### SQL脚本
- `sql/update_order_edit_cost_permission.sql`

---

## 📞 六、联系方式

如遇到问题，请联系：
- **技术支持**: [您的联系方式]
- **紧急联系**: [紧急联系方式]

---

## ✅ 七、部署签名

| 环境 | 部署时间 | 部署人 | 验证人 | 备注 |
|------|----------|--------|--------|------|
| 187测试 | | | | |
| 47生产 | | | | |
