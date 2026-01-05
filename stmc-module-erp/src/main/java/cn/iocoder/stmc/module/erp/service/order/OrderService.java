package cn.iocoder.stmc.module.erp.service.order;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.OrderPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.OrderSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderDO;

import javax.validation.Valid;
import java.util.List;

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

}
