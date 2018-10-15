package com.wa.msm.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.wa.msm.image")
@EnableDiscoveryClient
public class WaImageApplication {
	public static void main(String[] args) {
		SpringApplication.run(WaImageApplication.class, args);
	}
}
