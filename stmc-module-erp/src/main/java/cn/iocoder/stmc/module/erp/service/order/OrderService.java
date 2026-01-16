package cn.iocoder.stmc.module.erp.service.order;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.OrderCostFillReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.OrderPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.OrderSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.customer.CustomerDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderItemDO;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ERP 订单 Service 接口
 *
 * @author stmc
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOrder(@Valid OrderSaveReqVO createReqVO);

    /**
     * 更新订单
     *
     * @param updateReqVO 更新信息
     */
    void updateOrder(@Valid OrderSaveReqVO updateReqVO);

    /**
     * 更新订单状态
     *
     * @param id 订单编号
     * @param status 状态
     */
    void updateOrderStatus(Long id, Integer status);

    /**
     * 删除订单
     *
     * @param id 编号
     */
    void deleteOrder(Long id);

    /**
     * 批量删除订单
     *
     * @param ids 编号列表
     */
    void deleteOrderList(List<Long> ids);

    /**
     * 获得订单
     *
     * @param id 编号
     * @return 订单
     */
    OrderDO getOrder(Long id);

    /**
     * 获得订单分页
     *
     * @param pageReqVO 分页查询
     * @return 订单分页
     */
    PageResult<OrderDO> getOrderPage(OrderPageReqVO pageReqVO);

    /**
     * 获得订单精简列表（供下拉选择使用）
     *
     * @return 订单列表
     */
    List<OrderDO> getOrderSimpleList();

    /**
     * 根据供应商获得采购订单列表
     *
     * @param supplierId 供应商编号
     * @return 订单列表
     */
    List<OrderDO> getOrderListBySupplierId(Long supplierId);

    /**
     * 更新订单已付金额
     *
     * @param id 订单编号
     * @param paidAmount 已付金额增量
     */
    void updateOrderPaidAmount(Long id, java.math.BigDecimal paidAmount);

    /**
     * 批量获取订单Map
     *
     * @param ids 订单编号集合
     * @return 订单Map
     */
    Map<Long, OrderDO> getOrderMap(Collection<Long> ids);

    // ========== 订单明细相关 ==========

    /**
     * 获取订单明细列表
     *
     * @param orderId 订单ID
     * @return 明细列表
     */
    List<OrderItemDO> getOrderItemList(Long orderId);

    /**
     * 批量获取订单明细列表
     *
     * @param orderIds 订单ID列表
     * @return 明细列表
     */
    List<OrderItemDO> getOrderItemListByOrderIds(List<Long> orderIds);

    // ========== 成本填充相关 ==========

    /**
     * 填充订单成本（管理员操作）
     *
     * @param fillReqVO 成本填充信息
     */
    void fillOrderCost(@Valid OrderCostFillReqVO fillReqVO);

    /**
     * 编辑订单成本（已完成状态）
     *
     * @param editReqVO 成本编辑请求
     */
    void editOrderCost(@Valid OrderCostFillReqVO editReqVO);

    /**
     * 审核订单（通过）
     *
     * @param id 订单ID
     */
    void approveOrder(Long id);

    /**
     * 审核订单（拒绝）
     *
     * @param id 订单ID
     * @param reason 拒绝原因
     */
    void rejectOrder(Long id, String reason);

    // ========== 打印导出相关 ==========

    /**
     * 生成订单打印Excel（客户联开单）
     *
     * @param order 订单信息
     * @param customer 客户信息
     * @param items 订单明细列表
     * @param salesmanName 销售员姓名
     * @param response HTTP响应对象
     */
    void generatePrintExcel(OrderDO order, CustomerDO customer,
                           List<OrderItemDO> items, String salesmanName,
                           HttpServletResponse response) throws IOException;

    // ========== 付款相关 ==========

    /**
     * 标注订单为已付款
     *
     * @param id 订单编号
     */
    void markOrderAsPaid(Long id);

}
