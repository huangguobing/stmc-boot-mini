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

    PENDING(0, "待审批"),
    APPROVED(10, "已审批"),
    PAID(20, "已付款"),
    REJECTED(30, "已拒绝"),
    CANCELLED(40, "已取消");

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

}
