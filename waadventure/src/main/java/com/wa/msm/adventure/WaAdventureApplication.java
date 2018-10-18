package com.wa.msm.adventure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.wa.msm.adventure")
@EnableEurekaClient
public class WaAdventureApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaAdventureApplication.class, args);
	}
}
