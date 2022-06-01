package com.zealon.readingcloud.gateway.common.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 限流配置
 * @author hasee
 */
@Configuration
public class RedisRateLimiterConfig {


    /**
     * 按客户端ip限流
     * @return
     */
    @Bean
    public KeyResolver ipKeyResolver(){
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
}
