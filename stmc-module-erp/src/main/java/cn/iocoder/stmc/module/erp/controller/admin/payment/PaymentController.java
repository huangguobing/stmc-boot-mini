package cn.iocoder.stmc.module.erp.controller.admin.payment;

import cn.iocoder.stmc.framework.common.pojo.CommonResult;
import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.module.erp.controller.admin.payment.vo.PaymentPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.payment.vo.PaymentRespVO;
import cn.iocoder.stmc.module.erp.controller.admin.payment.vo.PaymentSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.payment.PaymentDO;
import cn.iocoder.stmc.module.erp.service.payment.PaymentService;
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

@Tag(name = "管理后台 - ERP 付款")
@RestController
@RequestMapping("/erp/payment")
@Validated
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @PostMapping("/create")
    @Operation(summary = "创建付款")
    @PreAuthorize("@ss.hasPermission('erp:payment:create')")
    public CommonResult<Long> createPayment(@Valid @RequestBody PaymentSaveReqVO createReqVO) {
        return success(paymentService.createPayment(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新付款")
    @PreAuthorize("@ss.hasPermission('erp:payment:update')")
    public CommonResult<Boolean> updatePayment(@Valid @RequestBody PaymentSaveReqVO updateReqVO) {
        paymentService.updatePayment(updateReqVO);
        return success(true);
    }

    @PutMapping("/approve")
    @Operation(summary = "审批付款")
    @Parameters({
            @Parameter(name = "id", description = "付款编号", required = true),
            @Parameter(name = "approved", description = "是否通过", required = true),
            @Parameter(name = "remark", description = "审批意见")
    })
    @PreAuthorize("@ss.hasPermission('erp:payment:approve')")
    public CommonResult<Boolean> approvePayment(@RequestParam("id") Long id,
                                                 @RequestParam("approved") Boolean approved,
                                                 @RequestParam(value = "remark", required = false) String remark) {
        paymentService.approvePayment(id, approved, remark);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除付款")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:payment:delete')")
    public CommonResult<Boolean> deletePayment(@RequestParam("id") Long id) {
        paymentService.deletePayment(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除付款")
    @Parameter(name = "ids", description = "编号列表", required = true)
    @PreAuthorize("@ss.hasPermission('erp:payment:delete')")
    public CommonResult<Boolean> deletePaymentList(@RequestParam("ids") List<Long> ids) {
        paymentService.deletePaymentList(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得付款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:payment:query')")
    public CommonResult<PaymentRespVO> getPayment(@RequestParam("id") Long id) {
        PaymentDO payment = paymentService.getPayment(id);
        return success(BeanUtils.toBean(payment, PaymentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得付款分页")
    @PreAuthorize("@ss.hasPermission('erp:payment:query')")
    public CommonResult<PageResult<PaymentRespVO>> getPaymentPage(@Valid PaymentPageReqVO pageVO) {
        PageResult<PaymentDO> pageResult = paymentService.getPaymentPage(pageVO);
        return success(BeanUtils.toBean(pageResult, PaymentRespVO.class));
    }

}
