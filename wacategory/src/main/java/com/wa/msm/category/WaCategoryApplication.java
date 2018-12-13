package com.wa.msm.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients("com.wa.msm.category")
@EnableEurekaClient
@EnableSwagger2
public class WaCategoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaCategoryApplication.class, args);
	}
}
