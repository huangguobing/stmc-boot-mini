package cn.iocoder.stmc.framework.banner.core;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.ClassUtils;

import java.util.concurrent.TimeUnit;

/**
 * 项目启动成功后，提供文档相关的地址
 *
 * @author bsl
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        ThreadUtil.execute(() -> {
            ThreadUtil.sleep(1, TimeUnit.SECONDS); // 延迟 1 秒，保证输出到结尾
            log.info("\n----------------------------------------------------------\n\t" +
                            "项目启动成功！\n\t" );

            // 数据报表
            if (isNotPresent("cn.iocoder.stmc.module.report.framework.security.config.SecurityConfiguration")) {
                System.out.println("[报表模块 stmc-module-report - 已禁用]");
            }
            // 工作流
            if (isNotPresent("cn.iocoder.stmc.module.bpm.framework.flowable.config.BpmFlowableConfiguration")) {
                System.out.println("[工作流模块 stmc-module-bpm - 已禁用]");
            }
            // 商城系统
            if (isNotPresent("cn.iocoder.stmc.module.trade.framework.web.config.TradeWebConfiguration")) {
                System.out.println("[商城系统 stmc-module-mall - 已禁用]");
            }
            // ERP 系统
            if (isNotPresent("cn.iocoder.stmc.module.erp.framework.web.config.ErpWebConfiguration")) {
                System.out.println("[ERP 系统 stmc-module-erp - 已禁用]");
            }
            // CRM 系统
            if (isNotPresent("cn.iocoder.stmc.module.crm.framework.web.config.CrmWebConfiguration")) {
                System.out.println("[CRM 系统 stmc-module-crm - 已禁用]");
            }
            // 微信公众号
            if (isNotPresent("cn.iocoder.stmc.module.mp.framework.mp.config.MpConfiguration")) {
                System.out.println("[微信公众号 stmc-module-mp - 已禁用]");
            }
            // 支付平台
            if (isNotPresent("cn.iocoder.stmc.module.pay.framework.pay.config.PayConfiguration")) {
                System.out.println("[支付系统 stmc-module-pay - 已禁用]");
            }
            // AI 大模型
            if (isNotPresent("cn.iocoder.stmc.module.ai.framework.web.config.AiWebConfiguration")) {
                System.out.println("[AI 大模型 stmc-module-ai - 已禁用]");
            }
            // IoT 物联网
            if (isNotPresent("cn.iocoder.stmc.module.iot.framework.web.config.IotWebConfiguration")) {
                System.out.println("[IoT 物联网 stmc-module-iot - 已禁用]");
            }
        });
    }

    private static boolean isNotPresent(String className) {
        return !ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader());
    }

}
