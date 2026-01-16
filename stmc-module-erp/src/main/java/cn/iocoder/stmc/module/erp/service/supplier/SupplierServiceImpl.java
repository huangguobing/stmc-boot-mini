package cn.iocoder.stmc.module.erp.service.supplier;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.module.erp.controller.admin.supplier.vo.SupplierPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.supplier.vo.SupplierSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.supplier.SupplierDO;
import cn.iocoder.stmc.module.erp.dal.mysql.supplier.SupplierMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.stmc.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.stmc.module.erp.enums.ErrorCodeConstants.SUPPLIER_NAME_EXISTS;
import static cn.iocoder.stmc.module.erp.enums.ErrorCodeConstants.SUPPLIER_NOT_EXISTS;

/**
 * ERP 供应商 Service 实现类
 *
 * @author stmc
 */
@Service
@Validated
public class SupplierServiceImpl implements SupplierService {

    @Resource
    private SupplierMapper supplierMapper;

    @Override
    public Long createSupplier(SupplierSaveReqVO createReqVO) {
        // 校验供应商名称是否唯一
        validateSupplierNameUnique(null, createReqVO.getName());
        // 插入
        SupplierDO supplier = BeanUtils.toBean(createReqVO, SupplierDO.class);
        supplierMapper.insert(supplier);
        return supplier.getId();
    }

    @Override
    public void updateSupplier(SupplierSaveReqVO updateReqVO) {
        // 校验存在
        validateSupplierExists(updateReqVO.getId());
        // 校验供应商名称是否唯一
        validateSupplierNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // 更新
        SupplierDO updateObj = BeanUtils.toBean(updateReqVO, SupplierDO.class);
        supplierMapper.updateById(updateObj);
    }

    @Override
    public void deleteSupplier(Long id) {
        // 校验存在
        validateSupplierExists(id);
        // 删除
        supplierMapper.deleteById(id);
    }

    @Override
    public void deleteSupplierList(List<Long> ids) {
        // 校验存在
        ids.forEach(this::validateSupplierExists);
        // 删除
        supplierMapper.deleteBatchIds(ids);
    }

    private void validateSupplierExists(Long id) {
        if (supplierMapper.selectById(id) == null) {
            throw exception(SUPPLIER_NOT_EXISTS);
        }
    }

    private void validateSupplierNameUnique(Long id, String name) {
        SupplierDO supplier = supplierMapper.selectByName(name);
        if (supplier == null) {
            return;
        }
        if (id == null) {
            throw exception(SUPPLIER_NAME_EXISTS);
        }
        if (!supplier.getId().equals(id)) {
            throw exception(SUPPLIER_NAME_EXISTS);
        }
    }

    @Override
    public SupplierDO getSupplier(Long id) {
        return supplierMapper.selectById(id);
    }

    @Override
    public PageResult<SupplierDO> getSupplierPage(SupplierPageReqVO pageReqVO) {
        return supplierMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SupplierDO> getSupplierListByStatus(Integer status) {
        return supplierMapper.selectList(SupplierDO::getStatus, status);
    }

    @Override
    public Map<Long, SupplierDO> getSupplierMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<SupplierDO> list = supplierMapper.selectBatchIds(ids);
        return list.stream().collect(Collectors.toMap(SupplierDO::getId, s -> s));
    }

}
