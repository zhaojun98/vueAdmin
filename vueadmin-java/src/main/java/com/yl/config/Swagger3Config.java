package com.yl.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author ：jerry
 * @date ：Created in 2022/10/20 17:53
 * @description：Swagger3配置
 * @version: V1.1
 * knife4j文档地址(端口号根据自己项目配置)： http://localhost:9081/doc.html#
 * swagger文档地址(端口号根据自己项目配置)：http://localhost:9081/swagger-ui/index.html#/
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
public class Swagger3Config {


    @Value("${swagger.apiProfile.path}")
    private String path;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
//                .groupName("webApi")
                .apiInfo(apiInfo())
                .select()
                //不显示错误的接口地址
                .apis(RequestHandlerSelectors.basePackage("com.szkj.controller"))
                .paths(PathSelectors.any())
                .build().pathMapping(path);
    }


    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("遂宁中院门户")
                .description("遂宁中院门户接口文档")
                .version("1.0")
                .license("jerry")
                .build();
    }
}