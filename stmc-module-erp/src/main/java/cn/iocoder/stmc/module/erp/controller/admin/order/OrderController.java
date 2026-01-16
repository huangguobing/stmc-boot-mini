package cn.iocoder.stmc.module.erp.controller.admin.order;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.stmc.framework.common.biz.system.permission.PermissionCommonApi;
import cn.iocoder.stmc.framework.common.pojo.CommonResult;
import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.*;
import cn.iocoder.stmc.module.erp.dal.dataobject.customer.CustomerDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderItemDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.supplier.SupplierDO;
import cn.iocoder.stmc.module.erp.service.customer.CustomerService;
import cn.iocoder.stmc.module.erp.enums.OrderStatusEnum;
import cn.iocoder.stmc.module.erp.service.order.OrderService;
import cn.iocoder.stmc.module.erp.service.supplier.SupplierService;
import cn.iocoder.stmc.module.system.api.user.AdminUserApi;
import cn.iocoder.stmc.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.stmc.framework.common.pojo.CommonResult.success;
import static cn.iocoder.stmc.module.erp.enums.ErrorCodeConstants.ORDER_NOT_EXISTS;

@Tag(name = "管理后台 - ERP 订单")
@RestController
@RequestMapping("/erp/order")
@Validated
public class OrderController {

    /** 可以查看全部订单的角色编码：超级管理员、租户管理员、老板 */
    private static final String[] ADMIN_ROLES = {"super_admin", "tenant_admin", "boss"};

    @Resource
    private OrderService orderService;

    @Resource
    private CustomerService customerService;

    @Resource
    private SupplierService supplierService;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private PermissionCommonApi permissionApi;

    @PostMapping("/create")
    @Operation(summary = "创建订单")
    @PreAuthorize("@ss.hasPermission('erp:order:create')")
    public CommonResult<Long> createOrder(@Valid @RequestBody OrderSaveReqVO createReqVO) {
        return success(orderService.createOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新订单")
    @PreAuthorize("@ss.hasPermission('erp:order:update')")
    public CommonResult<Boolean> updateOrder(@Valid @RequestBody OrderSaveReqVO updateReqVO) {
        orderService.updateOrder(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新订单状态")
    @Parameters({
            @Parameter(name = "id", description = "订单编号", required = true),
            @Parameter(name = "status", description = "状态", required = true)
    })
    @PreAuthorize("@ss.hasPermission('erp:order:update')")
    public CommonResult<Boolean> updateOrderStatus(@RequestParam("id") Long id,
                                                    @RequestParam("status") Integer status) {
        orderService.updateOrderStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:order:delete')")
    public CommonResult<Boolean> deleteOrder(@RequestParam("id") Long id) {
        orderService.deleteOrder(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除订单")
    @Parameter(name = "ids", description = "编号列表", required = true)
    @PreAuthorize("@ss.hasPermission('erp:order:delete')")
    public CommonResult<Boolean> deleteOrderList(@RequestParam("ids") List<Long> ids) {
        orderService.deleteOrderList(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得订单（含明细）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:order:query')")
    public CommonResult<OrderRespVO> getOrder(@RequestParam("id") Long id) {
        OrderDO order = orderService.getOrder(id);
        if (order == null) {
            return success(null);
        }

        // 构建响应
        OrderRespVO respVO = BeanUtils.toBean(order, OrderRespVO.class);

        // 获取明细列表
        List<OrderItemDO> items = orderService.getOrderItemList(id);
        List<OrderItemRespVO> itemRespVOs = BeanUtils.toBean(items, OrderItemRespVO.class);

        // 填充供应商名称
        if (CollUtil.isNotEmpty(items)) {
            List<Long> supplierIds = items.stream()
                    .map(OrderItemDO::getSupplierId)
                    .filter(sid -> sid != null)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(supplierIds)) {
                Map<Long, SupplierDO> supplierMap = supplierService.getSupplierMap(supplierIds);
                for (OrderItemRespVO itemVO : itemRespVOs) {
                    if (itemVO.getSupplierId() != null) {
                        SupplierDO supplier = supplierMap.get(itemVO.getSupplierId());
                        if (supplier != null) {
                            itemVO.setSupplierName(supplier.getName());
                        }
                    }
                }
            }
        }

        respVO.setItems(itemRespVOs);

        // 填充客户名称
        if (respVO.getCustomerId() != null) {
            CustomerDO customer = customerService.getCustomer(respVO.getCustomerId());
            if (customer != null) {
                respVO.setCustomerName(customer.getName());
            }
        }

        // 填充成本填充人姓名
        if (respVO.getCostFilledBy() != null) {
            AdminUserRespDTO user = adminUserApi.getUser(respVO.getCostFilledBy());
            if (user != null) {
                respVO.setCostFilledByName(user.getNickname());
            }
        }

        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得订单分页")
    @PreAuthorize("@ss.hasPermission('erp:order:query')")
    public CommonResult<PageResult<OrderRespVO>> getOrderPage(@Valid OrderPageReqVO pageVO) {
        // 数据权限过滤：非管理员只能查看自己的订单
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        if (!isAdmin(userId)) {
            pageVO.setSalesmanId(userId);
        }

        PageResult<OrderDO> pageResult = orderService.getOrderPage(pageVO);
        PageResult<OrderRespVO> voPageResult = BeanUtils.toBean(pageResult, OrderRespVO.class);

        // 填充客户名称
        if (CollUtil.isNotEmpty(voPageResult.getList())) {
            List<Long> customerIds = voPageResult.getList().stream()
                    .map(OrderRespVO::getCustomerId)
                    .filter(cid -> cid != null)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(customerIds)) {
                Map<Long, CustomerDO> customerMap = customerService.getCustomerMap(customerIds);
                for (OrderRespVO vo : voPageResult.getList()) {
                    if (vo.getCustomerId() != null) {
                        CustomerDO customer = customerMap.get(vo.getCustomerId());
                        if (customer != null) {
                            vo.setCustomerName(customer.getName());
                        }
                    }
                }
            }
        }

        return success(voPageResult);
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得订单精简列表")
    @PreAuthorize("@ss.hasPermission('erp:order:query')")
    public CommonResult<List<OrderRespVO>> getOrderSimpleList() {
        List<OrderDO> list = orderService.getOrderSimpleList();
        return success(BeanUtils.toBean(list, OrderRespVO.class));
    }

    @GetMapping("/simple-list-by-supplier")
    @Operation(summary = "根据供应商获得采购订单精简列表")
    @Parameter(name = "supplierId", description = "供应商编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:order:query')")
    public CommonResult<List<OrderRespVO>> getOrderSimpleListBySupplierId(@RequestParam("supplierId") Long supplierId) {
        List<OrderDO> list = orderService.getOrderListBySupplierId(supplierId);
        return success(BeanUtils.toBean(list, OrderRespVO.class));
    }

    // ========== 审核相关接口 ==========

    @PutMapping("/approve")
    @Operation(summary = "审核通过订单")
    @Parameter(name = "id", description = "订单编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:order:approve')")
    public CommonResult<Boolean> approveOrder(@RequestParam("id") Long id) {
        orderService.approveOrder(id);
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "审核拒绝订单")
    @Parameters({
            @Parameter(name = "id", description = "订单编号", required = true),
            @Parameter(name = "reason", description = "拒绝原因")
    })
    @PreAuthorize("@ss.hasPermission('erp:order:approve')")
    public CommonResult<Boolean> rejectOrder(@RequestParam("id") Long id,
                                              @RequestParam(value = "reason", required = false) String reason) {
        orderService.rejectOrder(id, reason);
        return success(true);
    }

    // ========== 成本填充相关接口 ==========

    @PutMapping("/fill-cost")
    @Operation(summary = "填充订单成本")
    @PreAuthorize("@ss.hasPermission('erp:order:fill-cost')")
    public CommonResult<Boolean> fillOrderCost(@Valid @RequestBody OrderCostFillReqVO fillReqVO) {
        orderService.fillOrderCost(fillReqVO);
        return success(true);
    }

    @PutMapping("/edit-cost")
    @Operation(summary = "编辑订单成本（管理员）")
    @PreAuthorize("@ss.hasPermission('erp:order:edit-cost')")
    public CommonResult<Boolean> editOrderCost(@Valid @RequestBody OrderCostFillReqVO editReqVO) {
        orderService.editOrderCost(editReqVO);
        return success(true);
    }

    // ========== 打印导出相关接口 ==========

    @GetMapping("/print-export")
    @Operation(summary = "导出订单打印数据（客户联开单）")
    @Parameter(name = "id", description = "订单编号", required = true)
    public void printOrderExport(@RequestParam("id") Long id,
                                 HttpServletResponse response) throws Exception {
        // 1. 获取订单信息
        OrderDO order = orderService.getOrder(id);
        if (order == null) {
            throw new Exception(String.valueOf(ORDER_NOT_EXISTS));
        }

        // 2. 获取客户信息
        CustomerDO customer = null;
        if (order.getCustomerId() != null) {
            customer = customerService.getCustomer(order.getCustomerId());
        }

        // 3. 获取订单明细
        List<OrderItemDO> items = orderService.getOrderItemList(id);

        // 4. 获取销售员姓名
        String salesmanName = "";
        if (order.getSalesmanId() != null) {
            AdminUserRespDTO user = adminUserApi.getUser(order.getSalesmanId());
            if (user != null) {
                salesmanName = user.getNickname();
            }
        }

        // 5. 生成Excel并写入响应流
        orderService.generatePrintExcel(order, customer, items, salesmanName, response);
    }

    // ========== 付款相关接口 ==========

    @PutMapping("/mark-paid")
    @Operation(summary = "标注订单为已付款")
    @Parameter(name = "id", description = "订单编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:order:update')")
    public CommonResult<Boolean> markOrderAsPaid(@RequestParam("id") Long id) {
        orderService.markOrderAsPaid(id);
        return success(true);
    }

    /**
     * 判断用户是否为管理员（可查看全部数据）
     */
    private boolean isAdmin(Long userId) {
        if (userId == null) {
            return false;
        }
        return permissionApi.hasAnyRoles(userId, ADMIN_ROLES);
    }

}
