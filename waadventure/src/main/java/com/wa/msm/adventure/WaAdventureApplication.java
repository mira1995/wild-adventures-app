package com.wa.msm.adventure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.wa.msm.adventure")
@EnableDiscoveryClient
public class WaAdventureApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaAdventureApplication.class, args);
	}
}
