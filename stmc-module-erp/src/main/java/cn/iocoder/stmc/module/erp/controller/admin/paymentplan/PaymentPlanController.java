package cn.iocoder.stmc.module.erp.controller.admin.paymentplan;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.stmc.framework.common.pojo.CommonResult;
import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.module.erp.controller.admin.paymentplan.vo.PaymentPlanPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.paymentplan.vo.PaymentPlanPreviewVO;
import cn.iocoder.stmc.module.erp.controller.admin.paymentplan.vo.PaymentPlanRespVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.customer.CustomerDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.payment.PaymentDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.paymentplan.PaymentPlanDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.supplier.SupplierDO;
import cn.iocoder.stmc.module.erp.service.customer.CustomerService;
import cn.iocoder.stmc.module.erp.service.order.OrderService;
import cn.iocoder.stmc.module.erp.service.payment.PaymentService;
import cn.iocoder.stmc.module.erp.service.paymentplan.PaymentPlanService;
import cn.iocoder.stmc.module.erp.service.supplier.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.stmc.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - ERP 付款计划
 *
 * @author stmc
 */
@Tag(name = "管理后台 - ERP 付款计划")
@RestController
@RequestMapping("/erp/payment-plan")
@Validated
public class PaymentPlanController {

    @Resource
    private PaymentPlanService paymentPlanService;
    @Resource
    private SupplierService supplierService;
    @Resource
    private PaymentService paymentService;
    @Resource
    private OrderService orderService;
    @Resource
    private CustomerService customerService;

    @GetMapping("/preview")
    @Operation(summary = "预览付款计划")
    @PreAuthorize("@ss.hasPermission('erp:payment:create')")
    public CommonResult<List<PaymentPlanPreviewVO>> previewPaymentPlans(
            @RequestParam("supplierId") @Parameter(description = "供应商编号", required = true) Long supplierId,
            @RequestParam("amount") @Parameter(description = "付款金额", required = true) BigDecimal amount,
            @RequestParam(value = "paymentDate", required = false) @Parameter(description = "付款日期")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate paymentDate) {
        List<PaymentPlanPreviewVO> previewList = paymentPlanService.previewPaymentPlans(supplierId, amount, paymentDate);
        return success(previewList);
    }

    @GetMapping("/page")
    @Operation(summary = "获取付款计划分页")
    @PreAuthorize("@ss.hasPermission('erp:payment-plan:query')")
    public CommonResult<PageResult<PaymentPlanRespVO>> getPaymentPlanPage(@Validated PaymentPlanPageReqVO pageReqVO) {
        PageResult<PaymentPlanDO> pageResult = paymentPlanService.getPaymentPlanPage(pageReqVO);
        // 转换为VO并填充关联信息
        PageResult<PaymentPlanRespVO> voPageResult = BeanUtils.toBean(pageResult, PaymentPlanRespVO.class);
        fillPaymentPlanInfo(voPageResult.getList());
        return success(voPageResult);
    }

    @GetMapping("/list-by-payment")
    @Operation(summary = "获取付款单的付款计划列表")
    @Parameter(name = "paymentId", description = "付款单编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:payment-plan:query')")
    public CommonResult<List<PaymentPlanRespVO>> getPaymentPlansByPaymentId(@RequestParam("paymentId") Long paymentId) {
        List<PaymentPlanDO> list = paymentPlanService.getPaymentPlansByPaymentId(paymentId);
        List<PaymentPlanRespVO> voList = BeanUtils.toBean(list, PaymentPlanRespVO.class);
        fillPaymentPlanInfo(voList);
        return success(voList);
    }

    @PostMapping("/mark-paid")
    @Operation(summary = "标记付款计划为已付款")
    @PreAuthorize("@ss.hasPermission('erp:payment-plan:pay')")
    public CommonResult<Boolean> markAsPaid(@RequestParam("id") @Parameter(description = "付款计划编号") Long id) {
        paymentPlanService.markAsPaid(id);
        return success(true);
    }

    /**
     * 填充付款计划关联信息（供应商名称、订单ID、客户名称、业务员）
     */
    private void fillPaymentPlanInfo(List<PaymentPlanRespVO> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        // 1. 获取所有供应商ID
        Set<Long> supplierIds = list.stream()
                .map(PaymentPlanRespVO::getSupplierId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        // 2. 获取所有付款单ID
        Set<Long> paymentIds = list.stream()
                .map(PaymentPlanRespVO::getPaymentId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());

        // 3. 批量查询供应商
        Map<Long, SupplierDO> supplierMap = CollUtil.isEmpty(supplierIds) ?
                Collections.emptyMap() :
                supplierService.getSupplierMap(supplierIds);

        // 4. 批量查询付款单（获取订单ID）
        Map<Long, PaymentDO> paymentMap = new HashMap<>();
        Set<Long> orderIds = new HashSet<>();

        // 先从 PaymentPlanRespVO 收集 orderId（直接关联）
        for (PaymentPlanRespVO vo : list) {
            if (vo.getOrderId() != null) {
                orderIds.add(vo.getOrderId());
            }
        }

        // 再从 PaymentDO 收集 orderId（间接关联）
        if (CollUtil.isNotEmpty(paymentIds)) {
            for (Long paymentId : paymentIds) {
                PaymentDO payment = paymentService.getPayment(paymentId);
                if (payment != null) {
                    paymentMap.put(paymentId, payment);
                    if (payment.getOrderId() != null) {
                        orderIds.add(payment.getOrderId());
                    }
                }
            }
        }

        // 5. 批量查询订单
        Map<Long, OrderDO> orderMap = CollUtil.isEmpty(orderIds) ?
                Collections.emptyMap() :
                orderService.getOrderMap(orderIds);

        // 6. 从订单中提取客户ID
        Set<Long> customerIds = orderMap.values().stream()
                .map(OrderDO::getCustomerId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());

        // 7. 批量查询客户
        Map<Long, CustomerDO> customerMap = CollUtil.isEmpty(customerIds) ?
                Collections.emptyMap() :
                customerService.getCustomerMap(customerIds);

        // 8. 填充数据
        for (PaymentPlanRespVO vo : list) {
            // 填充供应商名称
            if (vo.getSupplierId() != null) {
                SupplierDO supplier = supplierMap.get(vo.getSupplierId());
                vo.setSupplierName(supplier != null ? supplier.getName() : null);
            }

            // 优先使用 PaymentPlanDO 中直接关联的 orderId
            Long orderId = vo.getOrderId();

            // 如果 PaymentPlanDO 没有 orderId，则从 PaymentDO 获取
            if (orderId == null && vo.getPaymentId() != null) {
                PaymentDO payment = paymentMap.get(vo.getPaymentId());
                if (payment != null && payment.getOrderId() != null) {
                    orderId = payment.getOrderId();
                    vo.setOrderId(orderId);
                }
            }

            // 填充订单相关信息
            if (orderId != null) {
                OrderDO order = orderMap.get(orderId);
                if (order != null) {
                    vo.setOrderNo(order.getOrderNo());
                    vo.setSalesmanName(order.getSalesmanName());
                    // 从客户表获取客户名称
                    if (order.getCustomerId() != null) {
                        CustomerDO customer = customerMap.get(order.getCustomerId());
                        vo.setCustomerName(customer != null ? customer.getName() : null);
                    }
                }
            }
        }
    }

}
