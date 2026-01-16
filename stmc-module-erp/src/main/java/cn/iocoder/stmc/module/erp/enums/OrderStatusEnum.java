package cn.iocoder.stmc.module.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ERP 订单状态枚举
 *
 * @author stmc
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    /**
     * 待审核 - 业务员提交后的初始状态
     */
    PENDING_REVIEW(0, "待审核"),
    /**
     * 待填充成本 - 管理员审核通过后，等待填充采购成本
     */
    PENDING_COST(10, "待填充成本"),
    /**
     * 已完成 - 成本填充完成
     */
    COMPLETED(20, "已完成"),
    /**
     * 已取消
     */
    CANCELLED(50, "已取消");

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

}
