package cn.iocoder.stmc.module.erp.controller.admin.statistics;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.stmc.framework.common.pojo.CommonResult;
import cn.iocoder.stmc.framework.common.pojo.PageParam;
import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.module.erp.controller.admin.statistics.vo.CustomerStatisticsRespVO;
import cn.iocoder.stmc.module.erp.controller.admin.statistics.vo.OrderItemStatisticsRespVO;
import cn.iocoder.stmc.module.erp.controller.admin.statistics.vo.SalesmanStatisticsRespVO;
import cn.iocoder.stmc.module.erp.controller.admin.statistics.vo.SupplierStatisticsRespVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.order.OrderItemDO;
import cn.iocoder.stmc.module.erp.dal.mysql.order.OrderItemMapper;
import cn.iocoder.stmc.module.erp.dal.mysql.order.OrderMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.stmc.framework.common.pojo.CommonResult.success;
import static cn.iocoder.stmc.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

/**
 * ERP 统计报表 Controller
 *
 * @author stmc
 */
@Tag(name = "管理后台 - ERP 统计报表")
@RestController
@RequestMapping("/erp/statistics")
@Validated
public class StatisticsController {

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private OrderMapper orderMapper;

    @GetMapping("/supplier-purchase")
    @Operation(summary = "获取供应商采购统计")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<SupplierStatisticsRespVO>> getSupplierPurchaseStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY) LocalDate endDate,
            @RequestParam(required = false) String supplierName) {
        // 转换日期为时间范围
        LocalDateTime startTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endTime = endDate != null ? endDate.plusDays(1).atStartOfDay() : null;
        // 查询统计数据
        List<SupplierStatisticsRespVO> list = orderItemMapper.selectSupplierPurchaseStatistics(startTime, endTime, supplierName);
        return success(list);
    }

    @GetMapping("/supplier-purchase-detail")
    @Operation(summary = "获取供应商采购明细")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<PageResult<OrderItemStatisticsRespVO>> getSupplierPurchaseDetail(
            @RequestParam @Parameter(description = "供应商ID") Long supplierId,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY) LocalDate endDate,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        // 转换日期为时间范围
        LocalDateTime startTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endTime = endDate != null ? endDate.plusDays(1).atStartOfDay() : null;

        // 构建查询条件
        LambdaQueryWrapper<OrderItemDO> queryWrapper = new LambdaQueryWrapper<OrderItemDO>()
                .eq(OrderItemDO::getSupplierId, supplierId)
                .ge(startTime != null, OrderItemDO::getCreateTime, startTime)
                .lt(endTime != null, OrderItemDO::getCreateTime, endTime)
                .orderByDesc(OrderItemDO::getCreateTime);

        // 分页查询
        Page<OrderItemDO> page = new Page<>(pageNo, pageSize);
        Page<OrderItemDO> pageResult = orderItemMapper.selectPage(page, queryWrapper);

        // 转换为VO并填充订单号
        List<OrderItemStatisticsRespVO> voList = BeanUtils.toBean(pageResult.getRecords(), OrderItemStatisticsRespVO.class);
        fillOrderNo(voList, pageResult.getRecords());

        return success(new PageResult<>(voList, pageResult.getTotal()));
    }

    @GetMapping("/salesman-sales")
    @Operation(summary = "获取员工销售统计")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<SalesmanStatisticsRespVO>> getSalesmanSalesStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY) LocalDate endDate,
            @RequestParam(required = false) String salesmanName,
            @RequestParam(required = false) String mobile) {
        // 转换日期为时间范围
        LocalDateTime startTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endTime = endDate != null ? endDate.plusDays(1).atStartOfDay() : null;
        // 查询统计数据
        List<SalesmanStatisticsRespVO> list = orderMapper.selectSalesmanStatistics(startTime, endTime, salesmanName, mobile);
        return success(list);
    }

    @GetMapping("/customer-sales")
    @Operation(summary = "获取客户销售统计")
    @PreAuthorize("@ss.hasPermission('erp:statistics:query')")
    public CommonResult<List<CustomerStatisticsRespVO>> getCustomerSalesStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY) LocalDate endDate,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String mobile) {
        // 转换日期为时间范围
        LocalDateTime startTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endTime = endDate != null ? endDate.plusDays(1).atStartOfDay() : null;
        // 查询统计数据
        List<CustomerStatisticsRespVO> list = orderMapper.selectCustomerStatistics(startTime, endTime, customerName, mobile);
        return success(list);
    }

    /**
     * 填充订单号到明细VO
     */
    private void fillOrderNo(List<OrderItemStatisticsRespVO> voList, List<OrderItemDO> items) {
        if (CollUtil.isEmpty(items)) {
            return;
        }
        // 获取所有订单ID
        Set<Long> orderIds = items.stream()
                .map(OrderItemDO::getOrderId)
                .collect(Collectors.toSet());
        // 批量查询订单
        List<OrderDO> orders = orderMapper.selectBatchIds(orderIds);
        Map<Long, String> orderNoMap = orders.stream()
                .collect(Collectors.toMap(OrderDO::getId, OrderDO::getOrderNo));
        // 填充订单号
        for (int i = 0; i < voList.size(); i++) {
            OrderItemDO item = items.get(i);
            voList.get(i).setOrderNo(orderNoMap.get(item.getOrderId()));
        }
    }

}
