package com.zealon.readingcloud.account.common.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 书架线程池配置
 * @author hasee
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "spring.thread-pool.bookshelf")
public class BookshelfThreadPoolConfig {

    private int corePoolSize;
    private int maximumPoolSize;
    private Long keepAliveTime;
    private int queueCapacity;

    public ExecutorService buildUserBookshelfQueueThreadPool(){

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("user-bookshelf-queue-thread-%d").build();

        ThreadPoolExecutor pool = new ThreadPoolExecutor(this.getCorePoolSize(), this.getMaximumPoolSize(), this.getKeepAliveTime(), TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(this.queueCapacity),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        return pool;
    }

}
