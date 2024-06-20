package com.yl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author ：jerry
 * @date ：Created in 2024/6/19 20:47
 * @description：
 * @version: V1.1
 */
@Getter
@Setter
//@Configuration
@Component
@ConfigurationProperties(prefix = "ignore")
public class IgnoreWhitesConfig {

    /**放行接口路径*/
    private String[] whites;
}
