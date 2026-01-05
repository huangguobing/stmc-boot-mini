package cn.iocoder.stmc.module.erp.dal.mysql.supplier;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.stmc.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.stmc.module.erp.controller.admin.supplier.vo.SupplierPageReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.supplier.SupplierDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * ERP 供应商 Mapper
 *
 * @author stmc
 */
@Mapper
public interface SupplierMapper extends BaseMapperX<SupplierDO> {

    default PageResult<SupplierDO> selectPage(SupplierPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SupplierDO>()
                .likeIfPresent(SupplierDO::getName, reqVO.getName())
                .likeIfPresent(SupplierDO::getCode, reqVO.getCode())
                .likeIfPresent(SupplierDO::getContact, reqVO.getContact())
                .likeIfPresent(SupplierDO::getMobile, reqVO.getMobile())
                .eqIfPresent(SupplierDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(SupplierDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SupplierDO::getId));
    }

    default SupplierDO selectByName(String name) {
        return selectOne(SupplierDO::getName, name);
    }

}
