package cn.iocoder.stmc.module.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ERP 付款状态枚举
 *
 * @author stmc
 */
@Getter
@AllArgsConstructor
public enum PaymentStatusEnum {

    PENDING(0, "待付款"),
    PARTIAL(10, "部分付款"),
    COMPLETED(20, "已完成"),
    CANCELLED(30, "已取消");

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

}
