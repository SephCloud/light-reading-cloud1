package com.zealon.readingcloud.homepage.common.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置类
 * @author hasee
 */
@Configuration
@EnableSwagger2
public class HomepageSwaggerConfig {

    /**
     * swagger 生成
     * @return
     */
    public Docket customDocket(){

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zealon.readingcloud.homepage.controller"))
                .build()
                .apiInfo(apiInfo());

        return docket;
    }

    /**
     * swagger 基础信息
     * @return ApiInfo swagger信息
     */
    private ApiInfo apiInfo(){

        return new ApiInfoBuilder()
                .title("阅读APP接口")
                .description("图书、用户、搜索相关")
                .termsOfServiceUrl("")
                .contact(new Contact("","", ""))
                .license("")
                .licenseUrl("")
                .version("0.0.1")
                .build();

    }


}
