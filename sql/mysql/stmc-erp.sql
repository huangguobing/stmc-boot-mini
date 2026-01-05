-- =============================================
-- 尚泰铭成 ERP 模块数据库脚本
-- 包含：客户、供应商、订单、付款表
-- 日期：2025-12-27
-- =============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for erp_customer (客户表)
-- ----------------------------
DROP TABLE IF EXISTS `erp_customer`;
CREATE TABLE `erp_customer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户编号',
  `name` varchar(100) NOT NULL COMMENT '客户名称',
  `code` varchar(50) DEFAULT NULL COMMENT '客户编码',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `fax` varchar(50) DEFAULT NULL COMMENT '传真',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户银行',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '银行账号',
  `tax_no` varchar(50) DEFAULT NULL COMMENT '税号',
  `credit_limit` decimal(12,2) DEFAULT '0.00' COMMENT '信用额度',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(0正常 1停用)',
  `sort` int DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP客户表';

-- ----------------------------
-- Table structure for erp_supplier (供应商表)
-- ----------------------------
DROP TABLE IF EXISTS `erp_supplier`;
CREATE TABLE `erp_supplier` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '供应商编号',
  `name` varchar(100) NOT NULL COMMENT '供应商名称',
  `code` varchar(50) DEFAULT NULL COMMENT '供应商编码',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `fax` varchar(50) DEFAULT NULL COMMENT '传真',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户银行',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '银行账号',
  `tax_no` varchar(50) DEFAULT NULL COMMENT '税号',
  `payment_days` int DEFAULT 0 COMMENT '账期天数',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(0正常 1停用)',
  `sort` int DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP供应商表';

-- ----------------------------
-- Table structure for erp_order (订单表)
-- ----------------------------
DROP TABLE IF EXISTS `erp_order`;
CREATE TABLE `erp_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单编号',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `customer_id` bigint DEFAULT NULL COMMENT '客户编号(销售订单)',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商编号(采购订单)',
  `order_type` tinyint NOT NULL COMMENT '订单类型(1销售 2采购)',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态(0草稿 10待确认 20已确认 30处理中 40已完成 50已取消)',
  `order_date` datetime DEFAULT NULL COMMENT '订单日期',
  `delivery_date` datetime DEFAULT NULL COMMENT '交货日期',
  `total_quantity` decimal(12,2) DEFAULT '0.00' COMMENT '商品总数量',
  `total_amount` decimal(12,2) DEFAULT '0.00' COMMENT '商品总金额',
  `discount_amount` decimal(12,2) DEFAULT '0.00' COMMENT '折扣金额',
  `payable_amount` decimal(12,2) DEFAULT '0.00' COMMENT '应付金额',
  `paid_amount` decimal(12,2) DEFAULT '0.00' COMMENT '已付金额',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) DEFAULT NULL COMMENT '收货地址',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP订单表';

-- ----------------------------
-- Table structure for erp_payment (付款表)
-- ----------------------------
DROP TABLE IF EXISTS `erp_payment`;
CREATE TABLE `erp_payment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '付款编号',
  `payment_no` varchar(50) NOT NULL COMMENT '付款单号',
  `supplier_id` bigint NOT NULL COMMENT '供应商编号',
  `order_id` bigint DEFAULT NULL COMMENT '订单编号',
  `payment_type` tinyint NOT NULL COMMENT '付款类型(1采购付款 2费用付款)',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '付款状态(0待审批 10已通过 20已付款 30已拒绝 40已取消)',
  `amount` decimal(12,2) NOT NULL COMMENT '付款金额',
  `payment_method` tinyint DEFAULT NULL COMMENT '付款方式(1银行转账 2现金 3支票)',
  `payment_account` varchar(50) DEFAULT NULL COMMENT '付款账户',
  `payment_date` datetime DEFAULT NULL COMMENT '付款日期',
  `approver` bigint DEFAULT NULL COMMENT '审批人',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_remark` varchar(500) DEFAULT NULL COMMENT '审批意见',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_no` (`payment_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP付款表';

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================
-- ERP 菜单权限配置
-- =============================================

-- ERP管理一级菜单
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5100, 'ERP管理', '', 1, 50, 0, '/erp', 'ep:office-building', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 客户管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5110, '客户管理', '', 2, 1, 5100, 'customer', 'ep:user', 'erp/customer/index', 'ErpCustomer', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5111, '客户查询', 'erp:customer:query', 3, 1, 5110, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5112, '客户新增', 'erp:customer:create', 3, 2, 5110, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5113, '客户修改', 'erp:customer:update', 3, 3, 5110, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5114, '客户删除', 'erp:customer:delete', 3, 4, 5110, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 供应商管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5120, '供应商管理', '', 2, 2, 5100, 'supplier', 'ep:shop', 'erp/supplier/index', 'ErpSupplier', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5121, '供应商查询', 'erp:supplier:query', 3, 1, 5120, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5122, '供应商新增', 'erp:supplier:create', 3, 2, 5120, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5123, '供应商修改', 'erp:supplier:update', 3, 3, 5120, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5124, '供应商删除', 'erp:supplier:delete', 3, 4, 5120, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 订单管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5130, '订单管理', '', 2, 3, 5100, 'order', 'ep:document', 'erp/order/index', 'ErpOrder', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5131, '订单查询', 'erp:order:query', 3, 1, 5130, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5132, '订单新增', 'erp:order:create', 3, 2, 5130, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5133, '订单修改', 'erp:order:update', 3, 3, 5130, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5134, '订单删除', 'erp:order:delete', 3, 4, 5130, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 付款管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5140, '付款管理', '', 2, 4, 5100, 'payment', 'ep:money', 'erp/payment/index', 'ErpPayment', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5141, '付款查询', 'erp:payment:query', 3, 1, 5140, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5142, '付款新增', 'erp:payment:create', 3, 2, 5140, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5143, '付款审批', 'erp:payment:approve', 3, 3, 5140, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(5144, '付款删除', 'erp:payment:delete', 3, 4, 5140, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
