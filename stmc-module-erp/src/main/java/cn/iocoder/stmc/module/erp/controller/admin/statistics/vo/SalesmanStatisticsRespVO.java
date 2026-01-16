package cn.iocoder.stmc.module.erp.controller.admin.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 员工销售统计 Response VO")
@Data
public class SalesmanStatisticsRespVO {

    @Schema(description = "业务员ID", example = "1")
    private Long salesmanId;

    @Schema(description = "业务员姓名", example = "张三")
    private String salesmanName;

    @Schema(description = "部门名称", example = "销售部")
    private String deptName;

    @Schema(description = "手机号", example = "13800138000")
    private String mobile;

    @Schema(description = "订单数", example = "100")
    private Long orderCount;

    @Schema(description = "销售额", example = "500000.00")
    private BigDecimal salesAmount;

    @Schema(description = "成本", example = "300000.00")
    private BigDecimal costAmount;

    @Schema(description = "毛利", example = "200000.00")
    private BigDecimal grossProfit;

    @Schema(description = "净利润", example = "150000.00")
    private BigDecimal netProfit;

}
