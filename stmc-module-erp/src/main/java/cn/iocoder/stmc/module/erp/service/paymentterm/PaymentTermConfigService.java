package cn.iocoder.stmc.module.erp.service.paymentterm;

import cn.iocoder.stmc.module.erp.controller.admin.paymentterm.vo.PaymentTermConfigSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.paymentterm.PaymentTermConfigDO;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * ERP 采购账期分期配置 Service 接口
 *
 * @author stmc
 */
public interface PaymentTermConfigService {

    /**
     * 批量保存供应商的账期配置
     *
     * @param supplierId 供应商编号
     * @param configs 配置列表
     */
    void saveConfigs(Long supplierId, @Valid List<PaymentTermConfigSaveReqVO> configs);

    /**
     * 获取供应商的账期配置列表
     *
     * @param supplierId 供应商编号
     * @return 配置列表
     */
    List<PaymentTermConfigDO> getConfigsBySupplierId(Long supplierId);

    /**
     * 获取供应商启用的账期配置列表
     *
     * @param supplierId 供应商编号
     * @return 启用的配置列表
     */
    List<PaymentTermConfigDO> getEnabledConfigsBySupplierId(Long supplierId);

    /**
     * 批量获取多个供应商启用的账期配置
     *
     * @param supplierIds 供应商编号列表
     * @return Map<供应商编号, 配置列表>
     */
    Map<Long, List<PaymentTermConfigDO>> getEnabledConfigsBySupplierIds(List<Long> supplierIds);

}
