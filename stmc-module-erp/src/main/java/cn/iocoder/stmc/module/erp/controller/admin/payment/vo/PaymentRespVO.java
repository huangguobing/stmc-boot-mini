package cn.iocoder.stmc.module.erp.controller.admin.payment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 付款 Response VO")
@Data
public class PaymentRespVO {

    @Schema(description = "付款编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "付款单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PAY202312250001")
    private String paymentNo;

    @Schema(description = "供应商编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long supplierId;

    @Schema(description = "供应商名称", example = "供应商A")
    private String supplierName;

    @Schema(description = "订单编号", example = "1")
    private Long orderId;

    @Schema(description = "订单号", example = "PO202312250001")
    private String orderNo;

    @Schema(description = "客户名称", example = "客户A")
    private String customerName;

    @Schema(description = "开单业务员", example = "张三")
    private String salesmanName;

    @Schema(description = "付款类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer paymentType;

    @Schema(description = "付款状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "付款金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "10000.00")
    private BigDecimal amount;

    @Schema(description = "付款方式", example = "1")
    private Integer paymentMethod;

    @Schema(description = "付款账户", example = "6222021234567890123")
    private String paymentAccount;

    @Schema(description = "付款日期", example = "2023-12-25 10:00:00")
    private LocalDateTime paymentDate;

    @Schema(description = "审批人", example = "1")
    private Long approver;

    @Schema(description = "审批时间", example = "2023-12-25 12:00:00")
    private LocalDateTime approveTime;

    @Schema(description = "审批意见", example = "同意")
    private String approveRemark;

    @Schema(description = "备注", example = "采购付款")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
