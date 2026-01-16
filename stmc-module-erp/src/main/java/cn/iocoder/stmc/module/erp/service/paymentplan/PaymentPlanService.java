package cn.iocoder.stmc.module.erp.service.paymentplan;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.module.erp.controller.admin.paymentplan.vo.PaymentPlanPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.paymentplan.vo.PaymentPlanPreviewVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.paymentplan.PaymentPlanDO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * ERP 付款计划 Service 接口
 *
 * @author stmc
 */
public interface PaymentPlanService {

    /**
     * 预览付款计划（根据供应商账期配置预计算分期）
     *
     * @param supplierId 供应商编号
     * @param totalAmount 付款总金额
     * @param paymentDate 付款日期（作为计算账期的起始日期）
     * @return 付款计划预览列表
     */
    List<PaymentPlanPreviewVO> previewPaymentPlans(Long supplierId, BigDecimal totalAmount, LocalDate paymentDate);

    /**
     * 根据付款单生成付款计划
     *
     * @param paymentId 付款单编号
     * @param supplierId 供应商编号
     * @param totalAmount 付款总金额
     * @param paymentDate 付款日期（作为计算账期的起始日期）
     * @param paymentNo 付款单号
     * @param paidStages 创建时标记为已付款的期数列表（可选）
     */
    void generatePaymentPlansForPayment(Long paymentId, Long supplierId, BigDecimal totalAmount,
                                         LocalDate paymentDate, String paymentNo, List<Integer> paidStages);

    /**
     * 获得付款计划
     *
     * @param id 编号
     * @return 付款计划
     */
    PaymentPlanDO getPaymentPlan(Long id);

    /**
     * 获取付款单的付款计划列表
     *
     * @param paymentId 付款单编号
     * @return 付款计划列表
     */
    List<PaymentPlanDO> getPaymentPlansByPaymentId(Long paymentId);

    /**
     * 分页查询付款计划
     *
     * @param pageReqVO 分页查询条件
     * @return 付款计划分页
     */
    PageResult<PaymentPlanDO> getPaymentPlanPage(PaymentPlanPageReqVO pageReqVO);

    /**
     * 标记付款计划为已付款
     *
     * @param id 付款计划编号
     */
    void markAsPaid(Long id);

    /**
     * 取消付款单的付款计划
     *
     * @param paymentId 付款单编号
     */
    void cancelByPaymentId(Long paymentId);

    /**
     * 检查付款单是否存在已付款的计划
     *
     * @param paymentId 付款单编号
     * @return 是否存在已付款的计划
     */
    boolean hasPaidPlansByPaymentId(Long paymentId);

    /**
     * 处理付款计划通知
     * 包括：即将到期（提前3天）、当日到期、已逾期
     */
    void processPaymentPlanNotifications();

    /**
     * 创建单期付款计划（用于成本填充场景，不使用供应商账期配置）
     *
     * @param paymentId 付款单编号
     * @param supplierId 供应商编号
     * @param orderId 订单编号
     * @param amount 付款金额
     * @param planDate 计划付款日期
     * @param paymentNo 付款单号
     * @param isPaid 是否已付款
     * @param remark 备注
     */
    void createSinglePaymentPlan(Long paymentId, Long supplierId, Long orderId, BigDecimal amount,
                                  LocalDate planDate, String paymentNo, Boolean isPaid, String remark);

    /**
     * 取消付款单的所有付款计划（成本编辑时供应商被移除）
     *
     * @param paymentId 付款单编号
     */
    void cancelPaymentPlansByPaymentId(Long paymentId);

    /**
     * 更新付款计划（成本编辑时）
     *
     * @param paymentId 付款单编号
     * @param newAmount 新金额
     * @param newPlanDate 新计划日期
     * @param newIsPaid 新付款状态
     */
    void updatePaymentPlanFromCostEdit(Long paymentId, BigDecimal newAmount,
                                        LocalDate newPlanDate, Boolean newIsPaid);
}
