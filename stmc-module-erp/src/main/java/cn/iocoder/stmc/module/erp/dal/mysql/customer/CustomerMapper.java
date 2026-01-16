package cn.iocoder.stmc.module.erp.dal.mysql.customer;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.stmc.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.stmc.module.erp.controller.admin.customer.vo.CustomerPageReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.customer.CustomerDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * ERP 客户 Mapper
 *
 * @author stmc
 */
@Mapper
public interface CustomerMapper extends BaseMapperX<CustomerDO> {

    default PageResult<CustomerDO> selectPage(CustomerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CustomerDO>()
                .likeIfPresent(CustomerDO::getName, reqVO.getName())
                .likeIfPresent(CustomerDO::getCode, reqVO.getCode())
                .likeIfPresent(CustomerDO::getContact, reqVO.getContact())
                .likeIfPresent(CustomerDO::getMobile, reqVO.getMobile())
                .eqIfPresent(CustomerDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CustomerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CustomerDO::getCreateTime));
    }

    default CustomerDO selectByName(String name) {
        return selectOne(CustomerDO::getName, name);
    }

}
