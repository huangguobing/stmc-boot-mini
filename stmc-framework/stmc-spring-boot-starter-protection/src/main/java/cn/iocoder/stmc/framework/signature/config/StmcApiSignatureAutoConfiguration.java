package cn.iocoder.stmc.framework.signature.config;

import cn.iocoder.stmc.framework.redis.config.StmcRedisAutoConfiguration;
import cn.iocoder.stmc.framework.signature.core.aop.ApiSignatureAspect;
import cn.iocoder.stmc.framework.signature.core.redis.ApiSignatureRedisDAO;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * HTTP API 签名的自动配置类
 *
 * @author Zhougang
 */
@AutoConfiguration(after = StmcRedisAutoConfiguration.class)
public class StmcApiSignatureAutoConfiguration {

    @Bean
    public ApiSignatureAspect signatureAspect(ApiSignatureRedisDAO signatureRedisDAO) {
        return new ApiSignatureAspect(signatureRedisDAO);
    }

    @Bean
    public ApiSignatureRedisDAO signatureRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new ApiSignatureRedisDAO(stringRedisTemplate);
    }

}
