package cn.iocoder.stmc.module.erp.dal.dataobject.paymentterm;

import cn.iocoder.stmc.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * ERP 采购账期分期配置 DO
 *
 * @author stmc
 */
@TableName("erp_payment_term_config")
@KeySequence("erp_payment_term_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentTermConfigDO extends BaseDO {

    /**
     * 配置编号
     */
    @TableId
    private Long id;

    /**
     * 供应商编号
     */
    private Long supplierId;

    /**
     * 期数（第几期）
     */
    private Integer stage;

    /**
     * 订单后天数
     */
    private Integer daysAfterOrder;

    /**
     * 付款比例(%)
     */
    private BigDecimal percentage;

    /**
     * 状态(0启用 1停用)
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}
