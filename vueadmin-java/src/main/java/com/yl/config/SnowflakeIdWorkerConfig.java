package com.yl.config;

import com.yl.common.SnowflakeIdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：jerry
 * @date ：Created in 2022/1/18 14:07
 * @description：注入雪环算法Id的bean
 * @version: V1.1
 */

@Configuration
public class SnowflakeIdWorkerConfig {
    @Bean
    public SnowflakeIdWorker person() {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0,0);
        return idWorker;
    }

}
