package cn.iocoder.stmc.module.erp.dal.dataobject.product;

import cn.iocoder.stmc.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("erp_product")
@KeySequence("erp_product_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDO extends BaseDO {

    @TableId
    private Long id;
    private String name;
    private Integer status;
    private Integer sort;
    private String remark;

}
