package cn.iocoder.stmc.module.erp.controller.admin.paymentterm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP 账期分期配置保存 Request VO")
@Data
public class PaymentTermConfigSaveReqVO {

    @Schema(description = "订单后天数", requiredMode = Schema.RequiredMode.REQUIRED, example = "7")
    @NotNull(message = "订单后天数不能为空")
    private Integer daysAfterOrder;

    @Schema(description = "付款比例(%)", requiredMode = Schema.RequiredMode.REQUIRED, example = "50.00")
    @NotNull(message = "付款比例不能为空")
    private BigDecimal percentage;

    @Schema(description = "备注", example = "第一期付款")
    private String remark;

}
