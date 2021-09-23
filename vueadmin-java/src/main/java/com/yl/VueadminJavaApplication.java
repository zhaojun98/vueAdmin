package com.yl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.yl.mapper"})
@SpringBootApplication
public class VueadminJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VueadminJavaApplication.class, args);
	}

}
