package cn.iocoder.stmc.module.erp.service.payment;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.module.erp.controller.admin.payment.vo.PaymentPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.payment.vo.PaymentSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.payment.PaymentDO;

import javax.validation.Valid;
import java.util.List;

/**
 * ERP 付款 Service 接口
 *
 * @author stmc
 */
public interface PaymentService {

    /**
     * 创建付款
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPayment(@Valid PaymentSaveReqVO createReqVO);

    /**
     * 更新付款
     *
     * @param updateReqVO 更新信息
     */
    void updatePayment(@Valid PaymentSaveReqVO updateReqVO);

    /**
     * 审批付款
     *
     * @param id 付款编号
     * @param approved 是否通过
     * @param remark 审批意见
     */
    void approvePayment(Long id, Boolean approved, String remark);

    /**
     * 删除付款
     *
     * @param id 编号
     */
    void deletePayment(Long id);

    /**
     * 批量删除付款
     *
     * @param ids 编号列表
     */
    void deletePaymentList(List<Long> ids);

    /**
     * 获得付款
     *
     * @param id 编号
     * @return 付款
     */
    PaymentDO getPayment(Long id);

    /**
     * 获得付款分页
     *
     * @param pageReqVO 分页查询
     * @return 付款分页
     */
    PageResult<PaymentDO> getPaymentPage(PaymentPageReqVO pageReqVO);

}
