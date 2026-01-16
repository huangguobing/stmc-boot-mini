package cn.iocoder.stmc.module.erp.controller.admin.paymentplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 付款计划预览 VO")
@Data
public class PaymentPlanPreviewVO {

    @Schema(description = "期数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer stage;

    @Schema(description = "付款比例（%）", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private BigDecimal percentage;

    @Schema(description = "计划付款金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "5000.00")
    private BigDecimal planAmount;

    @Schema(description = "计划付款日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01-08")
    private LocalDate planDate;

    @Schema(description = "是否当天到期", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean isToday;

}
