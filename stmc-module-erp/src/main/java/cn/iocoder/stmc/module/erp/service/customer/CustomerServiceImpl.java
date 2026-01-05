package cn.iocoder.stmc.module.erp.service.customer;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.module.erp.controller.admin.customer.vo.CustomerPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.customer.vo.CustomerSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.customer.CustomerDO;
import cn.iocoder.stmc.module.erp.dal.mysql.customer.CustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.stmc.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.stmc.module.erp.enums.ErrorCodeConstants.CUSTOMER_NAME_EXISTS;
import static cn.iocoder.stmc.module.erp.enums.ErrorCodeConstants.CUSTOMER_NOT_EXISTS;

/**
 * ERP 客户 Service 实现类
 *
 * @author stmc
 */
@Service
@Validated
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public Long createCustomer(CustomerSaveReqVO createReqVO) {
        // 校验客户名称是否唯一
        validateCustomerNameUnique(null, createReqVO.getName());
        // 插入
        CustomerDO customer = BeanUtils.toBean(createReqVO, CustomerDO.class);
        customerMapper.insert(customer);
        return customer.getId();
    }

    @Override
    public void updateCustomer(CustomerSaveReqVO updateReqVO) {
        // 校验存在
        validateCustomerExists(updateReqVO.getId());
        // 校验客户名称是否唯一
        validateCustomerNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // 更新
        CustomerDO updateObj = BeanUtils.toBean(updateReqVO, CustomerDO.class);
        customerMapper.updateById(updateObj);
    }

    @Override
    public void deleteCustomer(Long id) {
        // 校验存在
        validateCustomerExists(id);
        // 删除
        customerMapper.deleteById(id);
    }

    @Override
    public void deleteCustomerList(List<Long> ids) {
        // 校验存在
        ids.forEach(this::validateCustomerExists);
        // 删除
        customerMapper.deleteBatchIds(ids);
    }

    private void validateCustomerExists(Long id) {
        if (customerMapper.selectById(id) == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }
    }

    private void validateCustomerNameUnique(Long id, String name) {
        CustomerDO customer = customerMapper.selectByName(name);
        if (customer == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的客户
        if (id == null) {
            throw exception(CUSTOMER_NAME_EXISTS);
        }
        if (!customer.getId().equals(id)) {
            throw exception(CUSTOMER_NAME_EXISTS);
        }
    }

    @Override
    public CustomerDO getCustomer(Long id) {
        return customerMapper.selectById(id);
    }

    @Override
    public PageResult<CustomerDO> getCustomerPage(CustomerPageReqVO pageReqVO) {
        return customerMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CustomerDO> getCustomerListByStatus(Integer status) {
        return customerMapper.selectList(CustomerDO::getStatus, status);
    }

}
