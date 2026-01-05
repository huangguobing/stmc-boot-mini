package cn.iocoder.stmc.module.erp.dal.mysql.payment;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.stmc.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.stmc.module.erp.controller.admin.payment.vo.PaymentPageReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.payment.PaymentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * ERP 付款 Mapper
 *
 * @author stmc
 */
@Mapper
public interface PaymentMapper extends BaseMapperX<PaymentDO> {

    default PageResult<PaymentDO> selectPage(PaymentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PaymentDO>()
                .likeIfPresent(PaymentDO::getPaymentNo, reqVO.getPaymentNo())
                .eqIfPresent(PaymentDO::getSupplierId, reqVO.getSupplierId())
                .eqIfPresent(PaymentDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(PaymentDO::getPaymentType, reqVO.getPaymentType())
                .eqIfPresent(PaymentDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(PaymentDO::getPaymentDate, reqVO.getPaymentDate())
                .betweenIfPresent(PaymentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PaymentDO::getId));
    }

    default PaymentDO selectByPaymentNo(String paymentNo) {
        return selectOne(PaymentDO::getPaymentNo, paymentNo);
    }

}
