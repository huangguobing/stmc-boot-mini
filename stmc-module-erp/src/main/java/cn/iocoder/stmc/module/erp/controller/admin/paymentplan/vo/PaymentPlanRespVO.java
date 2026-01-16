package cn.iocoder.stmc.module.erp.controller.admin.paymentplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 付款计划 Response VO")
@Data
public class PaymentPlanRespVO {

    @Schema(description = "计划编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "计划单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PP202401010001")
    private String planNo;

    @Schema(description = "付款单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long paymentId;

    @Schema(description = "付款单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PAY202401010001")
    private String paymentNo;

    @Schema(description = "供应商编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long supplierId;

    @Schema(description = "供应商名称", example = "供应商A")
    private String supplierName;

    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    @Schema(description = "订单号", example = "SO202401010001")
    private String orderNo;

    @Schema(description = "客户名称", example = "客户A")
    private String customerName;

    @Schema(description = "开单业务员", example = "张三")
    private String salesmanName;

    @Schema(description = "期数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer stage;

    @Schema(description = "计划付款金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "5000.00")
    private BigDecimal planAmount;

    @Schema(description = "计划付款日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01-08")
    private LocalDate planDate;

    @Schema(description = "实际付款金额", example = "5000.00")
    private BigDecimal actualAmount;

    @Schema(description = "实际付款日期")
    private LocalDateTime actualDate;

    @Schema(description = "状态(0待付款 10已付款 20已逾期 30已取消)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "通知状态(0未通知 1已通知即将到期 2已通知当日到期 3已通知逾期)", example = "0")
    private Integer notifyStatus;

    @Schema(description = "备注", example = "第一期付款")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
