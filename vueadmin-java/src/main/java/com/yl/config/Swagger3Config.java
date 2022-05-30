package com.yl.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
/**
 * @author ：jerry
 * @date ：Created in 2022/5/28 10:30
 * @description：
 * @version: V1.1
 *  *Swagger3API文档的配置
 *  knife4j文档地址(端口号根据自己项目配置)： http://localhost:8081/doc.html#
 *  swagger文档地址(端口号根据自己项目配置)：http://localhost:8081/swagger-ui/index.html#/
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
public class Swagger3Config {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
//                .groupName("webApi")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yl.controller"))
                .paths(PathSelectors.any())
                .build();
    }


    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SwaggerUI接口文档")
                .description("接口文档Swagger-Bootstrap版")
                .termsOfServiceUrl("http://localhost:8081/swagger-ui/index.html#/")
                .contact(new Contact("jerry","http://localhost:8081/doc.html#", "13258239832@163.com"))
                .version("1.0")
                .license("jerry")
                .build();
    }
}
