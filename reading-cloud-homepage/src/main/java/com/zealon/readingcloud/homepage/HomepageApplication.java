package com.zealon.readingcloud.homepage;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *
 * @author hasee
 */

@EnableFeignClients(basePackages = {"com.zealon.readingcloud.book.feign","com.zealon.readingcloud.account.feign"})
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.zealon.readingcloud.homepage", "com.zealon.readingcloud.common", "com.zealon.readingcloud.book.feign" ,"com.zealon.readingcloud.account.feign"})
@EnableHystrix
@EnableHystrixDashboard
public class HomepageApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(HomepageApplication.class, args);
    }

}
