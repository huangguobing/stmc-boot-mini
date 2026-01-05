package cn.iocoder.stmc.module.erp.enums;

import cn.iocoder.stmc.framework.common.exception.ErrorCode;

/**
 * ERP 模块错误码枚举类
 *
 * erp 系统，使用 1-020-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 客户管理 1-020-001-000 ==========
    ErrorCode CUSTOMER_NOT_EXISTS = new ErrorCode(1_020_001_000, "客户不存在");
    ErrorCode CUSTOMER_NAME_EXISTS = new ErrorCode(1_020_001_001, "客户名称已存在");
    ErrorCode CUSTOMER_CODE_EXISTS = new ErrorCode(1_020_001_002, "客户编码已存在");

    // ========== 供应商管理 1-020-002-000 ==========
    ErrorCode SUPPLIER_NOT_EXISTS = new ErrorCode(1_020_002_000, "供应商不存在");
    ErrorCode SUPPLIER_NAME_EXISTS = new ErrorCode(1_020_002_001, "供应商名称已存在");
    ErrorCode SUPPLIER_CODE_EXISTS = new ErrorCode(1_020_002_002, "供应商编码已存在");

    // ========== 订单管理 1-020-003-000 ==========
    ErrorCode ORDER_NOT_EXISTS = new ErrorCode(1_020_003_000, "订单不存在");
    ErrorCode ORDER_NO_EXISTS = new ErrorCode(1_020_003_001, "订单编号已存在");
    ErrorCode ORDER_STATUS_NOT_ALLOW_UPDATE = new ErrorCode(1_020_003_002, "订单状态不允许修改");
    ErrorCode ORDER_STATUS_NOT_ALLOW_DELETE = new ErrorCode(1_020_003_003, "订单状态不允许删除");
    ErrorCode ORDER_CUSTOMER_NOT_FOUND = new ErrorCode(1_020_003_004, "销售订单必须关联客户");
    ErrorCode ORDER_SUPPLIER_NOT_FOUND = new ErrorCode(1_020_003_005, "采购订单必须关联供应商");
    ErrorCode ORDER_STATUS_INVALID_TRANSITION = new ErrorCode(1_020_003_006, "订单状态转移无效");

    // ========== 付款管理 1-020-004-000 ==========
    ErrorCode PAYMENT_NOT_EXISTS = new ErrorCode(1_020_004_000, "付款记录不存在");
    ErrorCode PAYMENT_STATUS_NOT_ALLOW_UPDATE = new ErrorCode(1_020_004_001, "付款状态不允许修改");
    ErrorCode PAYMENT_STATUS_NOT_ALLOW_DELETE = new ErrorCode(1_020_004_002, "付款状态不允许删除");
    ErrorCode PAYMENT_SUPPLIER_NOT_FOUND = new ErrorCode(1_020_004_003, "付款供应商不存在");
    ErrorCode PAYMENT_ORDER_NOT_FOUND = new ErrorCode(1_020_004_004, "付款订单不存在");
    ErrorCode PAYMENT_AMOUNT_EXCEEDS = new ErrorCode(1_020_004_005, "付款金额超过应付金额");

}
