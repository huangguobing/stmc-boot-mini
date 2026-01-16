package cn.iocoder.stmc.module.erp.controller.admin.supplier;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.stmc.framework.common.enums.CommonStatusEnum;
import cn.iocoder.stmc.framework.common.pojo.CommonResult;
import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.module.erp.controller.admin.supplier.vo.SupplierPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.supplier.vo.SupplierRespVO;
import cn.iocoder.stmc.module.erp.controller.admin.supplier.vo.SupplierSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.paymentterm.PaymentTermConfigDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.supplier.SupplierDO;
import cn.iocoder.stmc.module.erp.service.paymentterm.PaymentTermConfigService;
import cn.iocoder.stmc.module.erp.service.supplier.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.stmc.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP 供应商")
@RestController
@RequestMapping("/erp/supplier")
@Validated
public class SupplierController {

    @Resource
    private SupplierService supplierService;

    @Resource
    private PaymentTermConfigService paymentTermConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建供应商")
    @PreAuthorize("@ss.hasPermission('erp:supplier:create')")
    public CommonResult<Long> createSupplier(@Valid @RequestBody SupplierSaveReqVO createReqVO) {
        return success(supplierService.createSupplier(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新供应商")
    @PreAuthorize("@ss.hasPermission('erp:supplier:update')")
    public CommonResult<Boolean> updateSupplier(@Valid @RequestBody SupplierSaveReqVO updateReqVO) {
        supplierService.updateSupplier(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除供应商")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:supplier:delete')")
    public CommonResult<Boolean> deleteSupplier(@RequestParam("id") Long id) {
        supplierService.deleteSupplier(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除供应商")
    @Parameter(name = "ids", description = "编号列表", required = true)
    @PreAuthorize("@ss.hasPermission('erp:supplier:delete')")
    public CommonResult<Boolean> deleteSupplierList(@RequestParam("ids") List<Long> ids) {
        supplierService.deleteSupplierList(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得供应商")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:supplier:query')")
    public CommonResult<SupplierRespVO> getSupplier(@RequestParam("id") Long id) {
        SupplierDO supplier = supplierService.getSupplier(id);
        return success(BeanUtils.toBean(supplier, SupplierRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得供应商分页")
    @PreAuthorize("@ss.hasPermission('erp:supplier:query')")
    public CommonResult<PageResult<SupplierRespVO>> getSupplierPage(@Valid SupplierPageReqVO pageVO) {
        PageResult<SupplierDO> pageResult = supplierService.getSupplierPage(pageVO);
        PageResult<SupplierRespVO> voPageResult = BeanUtils.toBean(pageResult, SupplierRespVO.class);
        // 填充账期配置信息
        fillPaymentTermConfigs(voPageResult.getList());
        return success(voPageResult);
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得供应商精简列表", description = "用于下拉选择")
    public CommonResult<List<SupplierRespVO>> getSupplierSimpleList() {
        List<SupplierDO> list = supplierService.getSupplierListByStatus(CommonStatusEnum.ENABLE.getStatus());
        return success(BeanUtils.toBean(list, SupplierRespVO.class));
    }

    /**
     * 填充账期分期配置信息
     */
    private void fillPaymentTermConfigs(List<SupplierRespVO> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        // 获取所有供应商ID
        List<Long> supplierIds = list.stream()
                .map(SupplierRespVO::getId)
                .collect(Collectors.toList());
        // 批量查询账期配置
        Map<Long, List<PaymentTermConfigDO>> configMap = paymentTermConfigService.getEnabledConfigsBySupplierIds(supplierIds);
        // 填充到每个供应商
        for (SupplierRespVO vo : list) {
            List<PaymentTermConfigDO> configs = configMap.get(vo.getId());
            if (CollUtil.isEmpty(configs)) {
                vo.setPaymentTermSummary("-");
                continue;
            }
            // 转换为配置项列表
            List<SupplierRespVO.PaymentTermConfigItem> items = new ArrayList<>();
            StringBuilder summary = new StringBuilder();
            summary.append(configs.size()).append("期（");
            for (int i = 0; i < configs.size(); i++) {
                PaymentTermConfigDO config = configs.get(i);
                // 配置项
                SupplierRespVO.PaymentTermConfigItem item = new SupplierRespVO.PaymentTermConfigItem();
                item.setStage(config.getStage());
                item.setDaysAfterOrder(config.getDaysAfterOrder());
                item.setPercentage(config.getPercentage());
                items.add(item);
                // 摘要
                if (i > 0) {
                    summary.append("/");
                }
                // 0天显示为"当天"，其他显示"X天"
                if (config.getDaysAfterOrder() == 0) {
                    summary.append("当天");
                } else {
                    summary.append(config.getDaysAfterOrder()).append("天");
                }
                summary.append(config.getPercentage().stripTrailingZeros().toPlainString()).append("%");
            }
            summary.append("）");
            vo.setPaymentTermConfigs(items);
            vo.setPaymentTermSummary(summary.toString());
        }
    }

}
