package cn.iocoder.stmc.module.erp.dal.mysql.order;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.stmc.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.OrderPageReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * ERP 订单 Mapper
 *
 * @author stmc
 */
@Mapper
public interface OrderMapper extends BaseMapperX<OrderDO> {

    default PageResult<OrderDO> selectPage(OrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderDO>()
                .likeIfPresent(OrderDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(OrderDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(OrderDO::getOrderType, reqVO.getOrderType())
                .eqIfPresent(OrderDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(OrderDO::getOrderDate, reqVO.getOrderDate())
                .betweenIfPresent(OrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrderDO::getId));
    }

    default OrderDO selectByOrderNo(String orderNo) {
        return selectOne(OrderDO::getOrderNo, orderNo);
    }

}
