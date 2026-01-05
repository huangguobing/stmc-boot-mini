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

    DRAFT(0, "草稿"),
    PENDING(10, "待确认"),
    CONFIRMED(20, "已确认"),
    PROCESSING(30, "执行中"),
    COMPLETED(40, "已完成"),
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
