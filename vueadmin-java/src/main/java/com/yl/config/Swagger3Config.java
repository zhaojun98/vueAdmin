package com.yl.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
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
public class Swagger3Config implements WebMvcConfigurer {


    @Value("${swagger.apiProfile.path}")
    private String path;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
//                .groupName("webApi")
                .apiInfo(apiInfo())
                .select()
                //不显示错误的接口地址
                .apis(RequestHandlerSelectors.basePackage("com.yl.controller"))
                .paths(PathSelectors.any())
                .build().pathMapping(path);
    }


    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("vueAdmin管理系统")
                .description("vueAdmin管理系统接口文档")
                .version("1.0")
                .license("jerry")
                .build();
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
    }
}