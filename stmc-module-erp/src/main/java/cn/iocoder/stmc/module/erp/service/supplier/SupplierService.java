package cn.iocoder.stmc.module.erp.service.supplier;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.module.erp.controller.admin.supplier.vo.SupplierPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.supplier.vo.SupplierSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.supplier.SupplierDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ERP 供应商 Service 接口
 *
 * @author stmc
 */
public interface SupplierService {

    /**
     * 创建供应商
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSupplier(@Valid SupplierSaveReqVO createReqVO);

    /**
     * 更新供应商
     *
     * @param updateReqVO 更新信息
     */
    void updateSupplier(@Valid SupplierSaveReqVO updateReqVO);

    /**
     * 删除供应商
     *
     * @param id 编号
     */
    void deleteSupplier(Long id);

    /**
     * 批量删除供应商
     *
     * @param ids 编号列表
     */
    void deleteSupplierList(List<Long> ids);

    /**
     * 获得供应商
     *
     * @param id 编号
     * @return 供应商
     */
    SupplierDO getSupplier(Long id);

    /**
     * 获得供应商分页
     *
     * @param pageReqVO 分页查询
     * @return 供应商分页
     */
    PageResult<SupplierDO> getSupplierPage(SupplierPageReqVO pageReqVO);

    /**
     * 获得供应商列表
     *
     * @param status 状态
     * @return 供应商列表
     */
    List<SupplierDO> getSupplierListByStatus(Integer status);

    /**
     * 批量获取供应商Map
     *
     * @param ids 供应商编号集合
     * @return 供应商Map
     */
    Map<Long, SupplierDO> getSupplierMap(Collection<Long> ids);

}
