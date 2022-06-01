package com.zealon.readingcloud.account.common.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 共用线程池配置
 * @author hasee
 */
@Configuration
@ConfigurationProperties(prefix = "spring.thread-pool.common")
@Data
public class ThreadPoolConfig {

    /** 核心线程数 */
    private int corePoolSize;

    /** 最大线程数 */
    private int maximumPoolSize;

    /** 线程存活时间 */
    private Long keepAliveTime;

    /** 队列容量 */
    private int queueCapacity;


    /**
     * 书架数据消费线程池
     * @return
     */
    @Bean(value = "commonQueueThreadPool")
    public ExecutorService buildCommonQueueThreadBuilder(){

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("common-queue-thread-%d").build();

        ThreadPoolExecutor pool = new ThreadPoolExecutor(this.getCorePoolSize(), this.getMaximumPoolSize(), this.getKeepAliveTime(), TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(this.getQueueCapacity()), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


        return pool;

    }


}
