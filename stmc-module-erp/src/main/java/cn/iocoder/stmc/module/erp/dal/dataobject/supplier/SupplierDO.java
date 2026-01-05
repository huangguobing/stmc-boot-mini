package cn.iocoder.stmc.module.erp.dal.dataobject.supplier;

import cn.iocoder.stmc.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * ERP 供应商 DO
 *
 * @author stmc
 */
@TableName("erp_supplier")
@KeySequence("erp_supplier_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierDO extends BaseDO {

    /**
     * 供应商编号
     */
    @TableId
    private Long id;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 供应商编码
     */
    private String code;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 传真
     */
    private String fax;

    /**
     * 地址
     */
    private String address;

    /**
     * 开户银行
     */
    private String bankName;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 税号
     */
    private String taxNo;

    /**
     * 账期天数
     */
    private Integer paymentDays;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

}
