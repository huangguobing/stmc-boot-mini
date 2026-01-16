package cn.iocoder.stmc.module.erp.controller.admin.paymentterm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 账期分期配置 Response VO")
@Data
public class PaymentTermConfigRespVO {

    @Schema(description = "配置编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "供应商编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long supplierId;

    @Schema(description = "期数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer stage;

    @Schema(description = "订单后天数", requiredMode = Schema.RequiredMode.REQUIRED, example = "7")
    private Integer daysAfterOrder;

    @Schema(description = "付款比例(%)", requiredMode = Schema.RequiredMode.REQUIRED, example = "50.00")
    private BigDecimal percentage;

    @Schema(description = "状态(0启用 1停用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "备注", example = "第一期付款")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
