package com.wa.msm.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.wa.msm.category")
@EnableEurekaClient
public class WaCategoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaCategoryApplication.class, args);
	}
}
