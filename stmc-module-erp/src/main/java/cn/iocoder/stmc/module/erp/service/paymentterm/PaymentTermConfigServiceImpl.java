package cn.iocoder.stmc.module.erp.service.paymentterm;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.stmc.module.erp.controller.admin.paymentterm.vo.PaymentTermConfigSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.paymentterm.PaymentTermConfigDO;
import cn.iocoder.stmc.module.erp.dal.mysql.paymentterm.PaymentTermConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.stmc.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.stmc.module.erp.enums.ErrorCodeConstants.PAYMENT_TERM_CONFIG_PERCENTAGE_NOT_100;

/**
 * ERP 采购账期分期配置 Service 实现类
 *
 * @author stmc
 */
@Service
@Validated
public class PaymentTermConfigServiceImpl implements PaymentTermConfigService {

    @Resource
    private PaymentTermConfigMapper paymentTermConfigMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveConfigs(Long supplierId, List<PaymentTermConfigSaveReqVO> configs) {
        // 1. 如果配置为空，直接删除旧配置
        if (CollUtil.isEmpty(configs)) {
            paymentTermConfigMapper.deleteBySupplierId(supplierId);
            return;
        }

        // 2. 校验比例总和为100%
        BigDecimal totalPercentage = configs.stream()
                .map(PaymentTermConfigSaveReqVO::getPercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalPercentage.compareTo(new BigDecimal("100")) != 0) {
            throw exception(PAYMENT_TERM_CONFIG_PERCENTAGE_NOT_100);
        }

        // 3. 删除旧配置
        paymentTermConfigMapper.deleteBySupplierId(supplierId);

        // 4. 插入新配置
        int stage = 1;
        for (PaymentTermConfigSaveReqVO vo : configs) {
            PaymentTermConfigDO config = new PaymentTermConfigDO();
            config.setSupplierId(supplierId);
            config.setStage(stage++);
            config.setDaysAfterOrder(vo.getDaysAfterOrder());
            config.setPercentage(vo.getPercentage());
            config.setStatus(0); // 默认启用
            config.setRemark(vo.getRemark());
            paymentTermConfigMapper.insert(config);
        }
    }

    @Override
    public List<PaymentTermConfigDO> getConfigsBySupplierId(Long supplierId) {
        return paymentTermConfigMapper.selectListBySupplierId(supplierId);
    }

    @Override
    public List<PaymentTermConfigDO> getEnabledConfigsBySupplierId(Long supplierId) {
        return paymentTermConfigMapper.selectListBySupplierIdAndStatus(supplierId, 0);
    }

    @Override
    public Map<Long, List<PaymentTermConfigDO>> getEnabledConfigsBySupplierIds(List<Long> supplierIds) {
        if (CollUtil.isEmpty(supplierIds)) {
            return Collections.emptyMap();
        }
        // 批量查询所有启用的配置
        List<PaymentTermConfigDO> allConfigs = paymentTermConfigMapper.selectListBySupplierIdsAndStatus(supplierIds, 0);
        // 按供应商ID分组
        return allConfigs.stream()
                .collect(Collectors.groupingBy(PaymentTermConfigDO::getSupplierId));
    }

}
