package cn.iocoder.stmc.module.erp.controller.admin.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 订单 Response VO")
@Data
public class OrderRespVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "SO202312250001")
    private String orderNo;

    @Schema(description = "客户编号（销售订单使用）", example = "1")
    private Long customerId;

    @Schema(description = "客户名称", example = "张三")
    private String customerName;

    @Schema(description = "供应商编号（采购订单使用）", example = "1")
    private Long supplierId;

    @Schema(description = "供应商名称", example = "供应商A")
    private String supplierName;

    @Schema(description = "订单类型（1销售 2采购）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer orderType;

    @Schema(description = "订单状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "订单日期", example = "2023-12-25 10:00:00")
    private LocalDateTime orderDate;

    @Schema(description = "交货日期", example = "2023-12-30 10:00:00")
    private LocalDateTime deliveryDate;

    @Schema(description = "商品总数量", example = "100")
    private BigDecimal totalQuantity;

    @Schema(description = "商品总金额", example = "10000.00")
    private BigDecimal totalAmount;

    @Schema(description = "折扣金额", example = "500.00")
    private BigDecimal discountAmount;

    @Schema(description = "应付金额", example = "9500.00")
    private BigDecimal payableAmount;

    @Schema(description = "已付金额", example = "5000.00")
    private BigDecimal paidAmount;

    @Schema(description = "联系人", example = "张三")
    private String contact;

    @Schema(description = "联系电话", example = "13800138000")
    private String mobile;

    @Schema(description = "收货地址", example = "北京市朝阳区")
    private String address;

    @Schema(description = "备注", example = "加急订单")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
