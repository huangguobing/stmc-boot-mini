package cn.iocoder.stmc.module.erp.service.payment;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.stmc.module.erp.controller.admin.payment.vo.PaymentPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.payment.vo.PaymentSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.payment.PaymentDO;
import cn.iocoder.stmc.module.erp.dal.mysql.payment.PaymentMapper;
import cn.iocoder.stmc.module.erp.enums.PaymentStatusEnum;
import cn.iocoder.stmc.module.erp.service.order.OrderService;
import cn.iocoder.stmc.module.erp.service.supplier.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cn.iocoder.stmc.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.stmc.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 付款 Service 实现类
 *
 * @author stmc
 */
@Service
@Validated
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentMapper paymentMapper;

    @Resource
    private SupplierService supplierService;

    @Resource
    private OrderService orderService;

    @Override
    public Long createPayment(PaymentSaveReqVO createReqVO) {
        // 校验供应商是否存在
        validateSupplierExists(createReqVO.getSupplierId());
        // 校验订单是否存在（如有）
        validateOrderExists(createReqVO.getOrderId());
        // 生成付款单号
        String paymentNo = generatePaymentNo();
        // 插入
        PaymentDO payment = BeanUtils.toBean(createReqVO, PaymentDO.class);
        payment.setPaymentNo(paymentNo);
        payment.setStatus(PaymentStatusEnum.PENDING.getStatus());
        paymentMapper.insert(payment);
        return payment.getId();
    }

    @Override
    public void updatePayment(PaymentSaveReqVO updateReqVO) {
        // 校验存在
        PaymentDO payment = validatePaymentExists(updateReqVO.getId());
        // 校验状态（只有待审批的付款可以修改）
        if (!PaymentStatusEnum.PENDING.getStatus().equals(payment.getStatus())) {
            throw exception(PAYMENT_STATUS_NOT_ALLOW_UPDATE);
        }
        // 更新
        PaymentDO updateObj = BeanUtils.toBean(updateReqVO, PaymentDO.class);
        paymentMapper.updateById(updateObj);
    }

    @Override
    public void approvePayment(Long id, Boolean approved, String remark) {
        // 校验存在
        PaymentDO payment = validatePaymentExists(id);
        // 校验状态
        if (!PaymentStatusEnum.PENDING.getStatus().equals(payment.getStatus())) {
            throw exception(PAYMENT_STATUS_NOT_ALLOW_UPDATE);
        }
        // 更新状态
        PaymentDO updateObj = new PaymentDO();
        updateObj.setId(id);
        updateObj.setStatus(approved ? PaymentStatusEnum.APPROVED.getStatus() : PaymentStatusEnum.REJECTED.getStatus());
        updateObj.setApprover(SecurityFrameworkUtils.getLoginUserId());
        updateObj.setApproveTime(LocalDateTime.now());
        updateObj.setApproveRemark(remark);
        paymentMapper.updateById(updateObj);

        // 如果审批通过且关联了订单，更新订单已付金额
        if (approved && payment.getOrderId() != null) {
            orderService.updateOrderPaidAmount(payment.getOrderId(), payment.getAmount());
        }
    }

    @Override
    public void deletePayment(Long id) {
        // 校验存在
        PaymentDO payment = validatePaymentExists(id);
        // 校验状态（只有待审批和已拒绝的付款可以删除）
        if (!PaymentStatusEnum.PENDING.getStatus().equals(payment.getStatus())
                && !PaymentStatusEnum.REJECTED.getStatus().equals(payment.getStatus())) {
            throw exception(PAYMENT_STATUS_NOT_ALLOW_DELETE);
        }
        // 删除
        paymentMapper.deleteById(id);
    }

    @Override
    public void deletePaymentList(List<Long> ids) {
        ids.forEach(this::deletePayment);
    }

    private PaymentDO validatePaymentExists(Long id) {
        PaymentDO payment = paymentMapper.selectById(id);
        if (payment == null) {
            throw exception(PAYMENT_NOT_EXISTS);
        }
        return payment;
    }

    @Override
    public PaymentDO getPayment(Long id) {
        return paymentMapper.selectById(id);
    }

    @Override
    public PageResult<PaymentDO> getPaymentPage(PaymentPageReqVO pageReqVO) {
        return paymentMapper.selectPage(pageReqVO);
    }

    /**
     * 生成付款单号
     *
     * @return 付款单号
     */
    private String generatePaymentNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%04d", IdUtil.getSnowflakeNextId() % 10000);
        return "PAY" + dateStr + randomStr;
    }

    /**
     * 校验供应商是否存在
     *
     * @param supplierId 供应商编号
     */
    private void validateSupplierExists(Long supplierId) {
        if (supplierId == null) {
            throw exception(PAYMENT_SUPPLIER_NOT_FOUND);
        }
        if (supplierService.getSupplier(supplierId) == null) {
            throw exception(PAYMENT_SUPPLIER_NOT_FOUND);
        }
    }

    /**
     * 校验订单是否存在
     *
     * @param orderId 订单编号
     */
    private void validateOrderExists(Long orderId) {
        if (orderId == null) {
            return; // 订单不是必填项
        }
        if (orderService.getOrder(orderId) == null) {
            throw exception(PAYMENT_ORDER_NOT_FOUND);
        }
    }

}
