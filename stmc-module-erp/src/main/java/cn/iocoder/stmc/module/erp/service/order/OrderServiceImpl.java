package cn.iocoder.stmc.module.erp.service.order;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.OrderPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.OrderSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderDO;
import cn.iocoder.stmc.module.erp.dal.mysql.order.OrderMapper;
import cn.iocoder.stmc.module.erp.enums.OrderStatusEnum;
import cn.iocoder.stmc.module.erp.service.customer.CustomerService;
import cn.iocoder.stmc.module.erp.service.supplier.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.iocoder.stmc.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.stmc.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 订单 Service 实现类
 *
 * @author stmc
 */
@Service
@Validated
public class OrderServiceImpl implements OrderService {

    /**
     * 订单状态流转规则
     * key: 当前状态, value: 允许转移到的状态集合
     */
    private static final Map<Integer, Set<Integer>> VALID_STATUS_TRANSITIONS = new HashMap<>();
    static {
        VALID_STATUS_TRANSITIONS.put(0, new HashSet<>(Arrays.asList(10, 50)));   // 草稿 -> 待确认、已取消
        VALID_STATUS_TRANSITIONS.put(10, new HashSet<>(Arrays.asList(20, 50))); // 待确认 -> 已确认、已取消
        VALID_STATUS_TRANSITIONS.put(20, new HashSet<>(Arrays.asList(30, 50))); // 已确认 -> 处理中、已取消
        VALID_STATUS_TRANSITIONS.put(30, new HashSet<>(Arrays.asList(40, 50))); // 处理中 -> 已完成、已取消
        VALID_STATUS_TRANSITIONS.put(40, Collections.emptySet());                // 已完成 -> 无
        VALID_STATUS_TRANSITIONS.put(50, Collections.emptySet());                // 已取消 -> 无
    }

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CustomerService customerService;

    @Resource
    private SupplierService supplierService;

    @Override
    public Long createOrder(OrderSaveReqVO createReqVO) {
        // 校验客户/供应商是否存在
        validateCustomerOrSupplier(createReqVO.getOrderType(), createReqVO.getCustomerId(), createReqVO.getSupplierId());
        // 生成订单号
        String orderNo = generateOrderNo(createReqVO.getOrderType());
        // 插入
        OrderDO order = BeanUtils.toBean(createReqVO, OrderDO.class);
        order.setOrderNo(orderNo);
        order.setStatus(OrderStatusEnum.DRAFT.getStatus());
        // 计算应付金额
        calculatePayableAmount(order);
        order.setPaidAmount(BigDecimal.ZERO);
        orderMapper.insert(order);
        return order.getId();
    }

    @Override
    public void updateOrder(OrderSaveReqVO updateReqVO) {
        // 校验存在
        OrderDO order = validateOrderExists(updateReqVO.getId());
        // 校验状态
        if (!OrderStatusEnum.DRAFT.getStatus().equals(order.getStatus())) {
            throw exception(ORDER_STATUS_NOT_ALLOW_UPDATE);
        }
        // 校验客户/供应商是否存在
        validateCustomerOrSupplier(updateReqVO.getOrderType(), updateReqVO.getCustomerId(), updateReqVO.getSupplierId());
        // 更新
        OrderDO updateObj = BeanUtils.toBean(updateReqVO, OrderDO.class);
        calculatePayableAmount(updateObj);
        orderMapper.updateById(updateObj);
    }

    @Override
    public void updateOrderStatus(Long id, Integer status) {
        // 校验存在
        OrderDO order = validateOrderExists(id);
        // 校验状态流转是否合法
        validateStatusTransition(order.getStatus(), status);
        // 更新状态
        OrderDO updateObj = new OrderDO();
        updateObj.setId(id);
        updateObj.setStatus(status);
        orderMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrder(Long id) {
        // 校验存在
        OrderDO order = validateOrderExists(id);
        // 校验状态（只有草稿和已取消的订单可以删除）
        if (!OrderStatusEnum.DRAFT.getStatus().equals(order.getStatus())
                && !OrderStatusEnum.CANCELLED.getStatus().equals(order.getStatus())) {
            throw exception(ORDER_STATUS_NOT_ALLOW_DELETE);
        }
        // 删除
        orderMapper.deleteById(id);
    }

    @Override
    public void deleteOrderList(List<Long> ids) {
        ids.forEach(this::deleteOrder);
    }

    private OrderDO validateOrderExists(Long id) {
        OrderDO order = orderMapper.selectById(id);
        if (order == null) {
            throw exception(ORDER_NOT_EXISTS);
        }
        return order;
    }

    @Override
    public OrderDO getOrder(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public PageResult<OrderDO> getOrderPage(OrderPageReqVO pageReqVO) {
        return orderMapper.selectPage(pageReqVO);
    }

    /**
     * 生成订单号
     *
     * @param orderType 订单类型
     * @return 订单号
     */
    private String generateOrderNo(Integer orderType) {
        String prefix = orderType == 1 ? "SO" : "PO"; // 销售订单/采购订单
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%04d", IdUtil.getSnowflakeNextId() % 10000);
        return prefix + dateStr + randomStr;
    }

    /**
     * 计算应付金额
     *
     * @param order 订单
     */
    private void calculatePayableAmount(OrderDO order) {
        BigDecimal totalAmount = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal discountAmount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;
        order.setPayableAmount(totalAmount.subtract(discountAmount));
    }

    /**
     * 校验客户/供应商是否存在
     *
     * @param orderType 订单类型（1销售 2采购）
     * @param customerId 客户编号
     * @param supplierId 供应商编号
     */
    private void validateCustomerOrSupplier(Integer orderType, Long customerId, Long supplierId) {
        if (orderType == 1) {
            // 销售订单必须关联客户
            if (customerId == null) {
                throw exception(ORDER_CUSTOMER_NOT_FOUND);
            }
            if (customerService.getCustomer(customerId) == null) {
                throw exception(ORDER_CUSTOMER_NOT_FOUND);
            }
        } else if (orderType == 2) {
            // 采购订单必须关联供应商
            if (supplierId == null) {
                throw exception(ORDER_SUPPLIER_NOT_FOUND);
            }
            if (supplierService.getSupplier(supplierId) == null) {
                throw exception(ORDER_SUPPLIER_NOT_FOUND);
            }
        }
    }

    /**
     * 校验状态流转是否合法
     *
     * @param currentStatus 当前状态
     * @param targetStatus 目标状态
     */
    private void validateStatusTransition(Integer currentStatus, Integer targetStatus) {
        Set<Integer> allowedStatuses = VALID_STATUS_TRANSITIONS.get(currentStatus);
        if (allowedStatuses == null || !allowedStatuses.contains(targetStatus)) {
            throw exception(ORDER_STATUS_INVALID_TRANSITION);
        }
    }

    @Override
    public List<OrderDO> getOrderSimpleList() {
        return orderMapper.selectList();
    }

    @Override
    public List<OrderDO> getOrderListBySupplierId(Long supplierId) {
        return orderMapper.selectList(OrderDO::getSupplierId, supplierId);
    }

    @Override
    public void updateOrderPaidAmount(Long id, BigDecimal paidAmountDelta) {
        OrderDO order = validateOrderExists(id);
        BigDecimal newPaidAmount = (order.getPaidAmount() != null ? order.getPaidAmount() : BigDecimal.ZERO)
                .add(paidAmountDelta);
        OrderDO updateObj = new OrderDO();
        updateObj.setId(id);
        updateObj.setPaidAmount(newPaidAmount);
        orderMapper.updateById(updateObj);
    }

}
