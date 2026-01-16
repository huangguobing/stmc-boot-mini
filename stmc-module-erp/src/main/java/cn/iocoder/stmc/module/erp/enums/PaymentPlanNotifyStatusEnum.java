package cn.iocoder.stmc.module.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ERP 付款计划通知状态枚举
 *
 * @author stmc
 */
@Getter
@AllArgsConstructor
public enum PaymentPlanNotifyStatusEnum {

    NOT_NOTIFIED(0, "未通知"),
    NOTIFIED_UPCOMING(1, "已通知即将到期"),
    NOTIFIED_DUE_TODAY(2, "已通知当日到期"),
    NOTIFIED_OVERDUE(3, "已通知逾期");

    private final Integer status;
    private final String name;

}
