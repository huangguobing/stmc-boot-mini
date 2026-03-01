package cn.iocoder.stmc.module.erp.dal.dataobject.product;

import cn.iocoder.stmc.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("erp_product_spec")
@KeySequence("erp_product_spec_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSpecDO extends BaseDO {

    @TableId
    private Long id;
    private Long productId;
    private String spec;
    private String unit;
    private Integer status;
    private Integer sort;

}
