package cn.iocoder.stmc.module.erp.controller.admin.paymentterm;

import cn.iocoder.stmc.framework.common.pojo.CommonResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.module.erp.controller.admin.paymentterm.vo.PaymentTermConfigRespVO;
import cn.iocoder.stmc.module.erp.controller.admin.paymentterm.vo.PaymentTermConfigSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.paymentterm.PaymentTermConfigDO;
import cn.iocoder.stmc.module.erp.service.paymentterm.PaymentTermConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.stmc.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - ERP 账期分期配置
 *
 * @author stmc
 */
@Tag(name = "管理后台 - ERP 账期分期配置")
@RestController
@RequestMapping("/erp/payment-term-config")
@Validated
public class PaymentTermConfigController {

    @Resource
    private PaymentTermConfigService paymentTermConfigService;

    @PostMapping("/save")
    @Operation(summary = "保存供应商账期配置")
    @PreAuthorize("@ss.hasPermission('erp:supplier:update')")
    public CommonResult<Boolean> saveConfigs(@RequestParam("supplierId") @Parameter(description = "供应商编号") Long supplierId,
                                              @RequestBody @Valid List<PaymentTermConfigSaveReqVO> configs) {
        paymentTermConfigService.saveConfigs(supplierId, configs);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获取供应商账期配置列表")
    @Parameter(name = "supplierId", description = "供应商编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:supplier:query')")
    public CommonResult<List<PaymentTermConfigRespVO>> getConfigList(@RequestParam("supplierId") Long supplierId) {
        List<PaymentTermConfigDO> list = paymentTermConfigService.getConfigsBySupplierId(supplierId);
        return success(BeanUtils.toBean(list, PaymentTermConfigRespVO.class));
    }

}
