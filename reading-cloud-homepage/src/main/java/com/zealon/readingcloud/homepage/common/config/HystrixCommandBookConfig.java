package com.zealon.readingcloud.homepage.common.config;

import com.netflix.hystrix.*;
import com.zealon.readingcloud.book.feign.client.BookClient;
import feign.Feign;
import feign.hystrix.HystrixFeign;
import org.springframework.context.annotation.Configuration;

/**
 * 图书资源中心 图书服务熔断配置
 * @author hasee
 */
@Configuration
public class HystrixCommandBookConfig {

    public Feign.Builder bookFeignHystrixBuilder(){
        return HystrixFeign.builder().setterFactory(((target, method) -> HystrixCommand.Setter
                //组
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(BookClient.class.getSimpleName()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(BookClient.class.getSimpleName()))
                .andCommandPropertiesDefaults(
                        // 超时配置，超时后进行熔断并服务降级
                        HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(500)
                )
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                .withAllowMaximumSizeToDivergeFromCoreSize(true)
                                .withMaximumSize(5)
                                .withCoreSize(5)
                                .withCoreSize(3)
                                .withMaxQueueSize(30)
                )
        ));
    }


}
