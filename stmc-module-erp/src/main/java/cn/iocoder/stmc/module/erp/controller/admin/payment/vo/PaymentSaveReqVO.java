package cn.iocoder.stmc.module.erp.controller.admin.payment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static cn.iocoder.stmc.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - ERP 付款新增/修改 Request VO")
@Data
public class PaymentSaveReqVO {

    @Schema(description = "付款编号", requiredMode = Schema.RequiredMode.AUTO, example = "1")
    private Long id;

    @Schema(description = "供应商编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "供应商不能为空")
    private Long supplierId;

    @Schema(description = "订单编号", example = "1")
    private Long orderId;

    @Schema(description = "付款类型（默认1采购付款）", example = "1")
    private Integer paymentType;

    @Schema(description = "付款金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "10000.00")
    @NotNull(message = "付款金额不能为空")
    private BigDecimal amount;

    @Schema(description = "付款方式", example = "1")
    private Integer paymentMethod;

    @Schema(description = "付款账户", example = "6222021234567890123")
    private String paymentAccount;

    @Schema(description = "付款日期", example = "2023-12-25")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate paymentDate;

    @Schema(description = "备注", example = "采购付款")
    private String remark;

    @Schema(description = "创建时标记为已付款的期数列表", example = "[1]")
    private List<Integer> paidStages;

}
