package cn.iocoder.stmc.module.erp.service.customer;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.module.erp.controller.admin.customer.vo.CustomerPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.customer.vo.CustomerSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.customer.CustomerDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ERP 客户 Service 接口
 *
 * @author stmc
 */
public interface CustomerService {

    /**
     * 创建客户
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCustomer(@Valid CustomerSaveReqVO createReqVO);

    /**
     * 更新客户
     *
     * @param updateReqVO 更新信息
     */
    void updateCustomer(@Valid CustomerSaveReqVO updateReqVO);

    /**
     * 删除客户
     *
     * @param id 编号
     */
    void deleteCustomer(Long id);

    /**
     * 批量删除客户
     *
     * @param ids 编号列表
     */
    void deleteCustomerList(List<Long> ids);

    /**
     * 获得客户
     *
     * @param id 编号
     * @return 客户
     */
    CustomerDO getCustomer(Long id);

    /**
     * 获得客户分页
     *
     * @param pageReqVO 分页查询
     * @return 客户分页
     */
    PageResult<CustomerDO> getCustomerPage(CustomerPageReqVO pageReqVO);

    /**
     * 获得客户列表
     *
     * @param status 状态
     * @return 客户列表
     */
    List<CustomerDO> getCustomerListByStatus(Integer status);

    /**
     * 获得客户 Map
     *
     * @param ids 编号列表
     * @return 客户 Map
     */
    Map<Long, CustomerDO> getCustomerMap(Collection<Long> ids);

}
