package com.zealon.readingcloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心
 * Hello world!
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ConfigApplication.class, args);
    }
}
