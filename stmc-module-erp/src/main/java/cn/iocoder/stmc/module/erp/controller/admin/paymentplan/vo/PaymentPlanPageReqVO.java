package cn.iocoder.stmc.module.erp.controller.admin.paymentplan.vo;

import cn.iocoder.stmc.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static cn.iocoder.stmc.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - ERP 付款计划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentPlanPageReqVO extends PageParam {

    @Schema(description = "付款单号", example = "PAY202401010001")
    private String paymentNo;

    @Schema(description = "供应商编号", example = "1")
    private Long supplierId;

    @Schema(description = "状态(0待付款 10已付款 20已逾期 30已取消)", example = "0")
    private Integer status;

    @Schema(description = "计划日期开始", example = "2024-01-01")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate planDateStart;

    @Schema(description = "计划日期结束", example = "2024-01-31")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate planDateEnd;

}
