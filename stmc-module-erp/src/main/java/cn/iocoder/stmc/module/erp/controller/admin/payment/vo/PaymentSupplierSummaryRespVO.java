package cn.iocoder.stmc.module.erp.controller.admin.payment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP 付款供应商未付款汇总 Response VO")
@Data
public class PaymentSupplierSummaryRespVO {

    @Schema(description = "供应商编号", example = "1")
    private Long supplierId;

    @Schema(description = "供应商名称", example = "供应商A")
    private String supplierName;

    @Schema(description = "未付款笔数", example = "3")
    private Integer unpaidCount;

    @Schema(description = "未付款总额", example = "35200.00")
    private BigDecimal unpaidAmount;

}
