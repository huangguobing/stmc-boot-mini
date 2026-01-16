package cn.iocoder.stmc.module.erp.controller.admin.home.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单统计 VO - 用于汇总查询结果
 */
@Data
public class OrderStatisticsVO {

    /**
     * 订单数量
     */
    private Long orderCount;

    /**
     * 销售额（应付金额）
     */
    private BigDecimal salesAmount;

    /**
     * 采购成本
     */
    private BigDecimal costAmount;

    /**
     * 毛利
     */
    private BigDecimal grossProfit;

    /**
     * 净利
     */
    private BigDecimal netProfit;

}
