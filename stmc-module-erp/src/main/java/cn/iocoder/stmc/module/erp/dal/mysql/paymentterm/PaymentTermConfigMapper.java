package cn.iocoder.stmc.module.erp.dal.mysql.paymentterm;

import cn.iocoder.stmc.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.stmc.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.stmc.module.erp.dal.dataobject.paymentterm.PaymentTermConfigDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ERP 采购账期分期配置 Mapper
 *
 * @author stmc
 */
@Mapper
public interface PaymentTermConfigMapper extends BaseMapperX<PaymentTermConfigDO> {

    default List<PaymentTermConfigDO> selectListBySupplierId(Long supplierId) {
        return selectList(PaymentTermConfigDO::getSupplierId, supplierId);
    }

    default List<PaymentTermConfigDO> selectListBySupplierIdAndStatus(Long supplierId, Integer status) {
        return selectList(PaymentTermConfigDO::getSupplierId, supplierId,
                PaymentTermConfigDO::getStatus, status);
    }

    default List<PaymentTermConfigDO> selectListBySupplierIdsAndStatus(List<Long> supplierIds, Integer status) {
        return selectList(new LambdaQueryWrapperX<PaymentTermConfigDO>()
                .in(PaymentTermConfigDO::getSupplierId, supplierIds)
                .eq(PaymentTermConfigDO::getStatus, status)
                .orderByAsc(PaymentTermConfigDO::getSupplierId)
                .orderByAsc(PaymentTermConfigDO::getStage));
    }

    default void deleteBySupplierId(Long supplierId) {
        delete(PaymentTermConfigDO::getSupplierId, supplierId);
    }

}
