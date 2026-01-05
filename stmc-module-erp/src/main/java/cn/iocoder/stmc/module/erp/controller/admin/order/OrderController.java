package cn.iocoder.stmc.module.erp.controller.admin.order;

import cn.iocoder.stmc.framework.common.pojo.CommonResult;
import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.OrderPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.OrderRespVO;
import cn.iocoder.stmc.module.erp.controller.admin.order.vo.OrderSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderDO;
import cn.iocoder.stmc.module.erp.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.stmc.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP 订单")
@RestController
@RequestMapping("/erp/order")
@Validated
public class OrderController {

    @Resource
    private OrderService orderService;

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
    @Operation(summary = "获得订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:order:query')")
    public CommonResult<OrderRespVO> getOrder(@RequestParam("id") Long id) {
        OrderDO order = orderService.getOrder(id);
        return success(BeanUtils.toBean(order, OrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得订单分页")
    @PreAuthorize("@ss.hasPermission('erp:order:query')")
    public CommonResult<PageResult<OrderRespVO>> getOrderPage(@Valid OrderPageReqVO pageVO) {
        PageResult<OrderDO> pageResult = orderService.getOrderPage(pageVO);
        return success(BeanUtils.toBean(pageResult, OrderRespVO.class));
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

}
