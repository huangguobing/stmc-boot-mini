package cn.iocoder.stmc.module.erp.controller.admin.supplier.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - ERP 供应商 Response VO")
@Data
public class SupplierRespVO {

    @Schema(description = "供应商编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "供应商A")
    private String name;

    @Schema(description = "供应商编码", example = "S001")
    private String code;

    @Schema(description = "联系人", example = "张三")
    private String contact;

    @Schema(description = "联系电话", example = "13800138000")
    private String mobile;

    @Schema(description = "电子邮箱", example = "test@example.com")
    private String email;

    @Schema(description = "传真", example = "010-12345678")
    private String fax;

    @Schema(description = "地址", example = "北京市朝阳区")
    private String address;

    @Schema(description = "开户银行", example = "中国工商银行")
    private String bankName;

    @Schema(description = "银行账号", example = "6222021234567890123")
    private String bankAccount;

    @Schema(description = "税号", example = "91110000MA00ABCDEF")
    private String taxNo;

    @Schema(description = "账期天数", example = "30")
    private Integer paymentDays;

    @Schema(description = "账期分期配置列表")
    private List<PaymentTermConfigItem> paymentTermConfigs;

    @Schema(description = "账期分期摘要", example = "3期（0天50%/7天30%/15天20%）")
    private String paymentTermSummary;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "优质供应商")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    /**
     * 账期分期配置项（简化版）
     */
    @Data
    public static class PaymentTermConfigItem {
        @Schema(description = "期数", example = "1")
        private Integer stage;

        @Schema(description = "付款后天数", example = "0")
        private Integer daysAfterOrder;

        @Schema(description = "付款比例(%)", example = "50.00")
        private BigDecimal percentage;
    }

}
