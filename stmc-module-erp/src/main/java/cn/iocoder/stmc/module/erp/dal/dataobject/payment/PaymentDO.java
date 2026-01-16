package cn.iocoder.stmc.module.erp.dal.dataobject.payment;

import cn.iocoder.stmc.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.supplier.SupplierDO;
import cn.iocoder.stmc.module.erp.enums.PaymentStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ERP 付款 DO
 *
 * @author stmc
 */
@TableName("erp_payment")
@KeySequence("erp_payment_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentDO extends BaseDO {

    /**
     * 付款编号
     */
    @TableId
    private Long id;

    /**
     * 付款单号
     */
    private String paymentNo;

    /**
     * 供应商编号
     *
     * 关联 {@link SupplierDO#getId()}
     */
    private Long supplierId;

    /**
     * 订单编号
     *
     * 关联 {@link OrderDO#getId()}
     */
    private Long orderId;

    /**
     * 付款类型（1采购付款 2费用付款）
     */
    private Integer paymentType;

    /**
     * 付款状态
     *
     * 枚举 {@link PaymentStatusEnum}
     */
    private Integer status;

    /**
     * 付款金额
     */
    private BigDecimal amount;

    /**
     * 付款方式（1银行转账 2现金 3支票 4其他）
     */
    private Integer paymentMethod;

    /**
     * 付款账户
     */
    private String paymentAccount;

    /**
     * 付款日期
     */
    private LocalDate paymentDate;

    /**
     * 审批人
     */
    private Long approver;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 审批意见
     */
    private String approveRemark;

    /**
     * 备注
     */
    private String remark;

}
