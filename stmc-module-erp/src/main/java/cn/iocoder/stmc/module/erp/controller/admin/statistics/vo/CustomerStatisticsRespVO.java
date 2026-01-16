package cn.iocoder.stmc.module.erp.controller.admin.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户销售统计 Response VO
 *
 * @author stmc
 */
@Schema(description = "管理后台 - 客户销售统计 Response VO")
@Data
public class CustomerStatisticsRespVO {

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long customerId;

    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerName;

    @Schema(description = "客户编号")
    private String customerCode;

    @Schema(description = "联系人")
    private String contact;

    @Schema(description = "联系电话")
    private String mobile;

    @Schema(description = "订单数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long orderCount;

    @Schema(description = "销售额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal salesAmount;

    @Schema(description = "成本", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal costAmount;

    @Schema(description = "毛利", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal grossProfit;

    @Schema(description = "净利润", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal netProfit;

}
