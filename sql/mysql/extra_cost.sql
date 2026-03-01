-- 订单其他费用字段
ALTER TABLE `erp_order` ADD COLUMN `extra_cost` decimal(24,2) NULL DEFAULT NULL COMMENT '其他费用金额' AFTER `total_net_profit`;
ALTER TABLE `erp_order` ADD COLUMN `extra_cost_remark` varchar(500) NULL DEFAULT NULL COMMENT '其他费用备注' AFTER `extra_cost`;
