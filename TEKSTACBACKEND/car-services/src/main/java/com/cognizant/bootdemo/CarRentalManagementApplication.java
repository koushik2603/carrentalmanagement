package com.cognizant.bootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.cognizant.bootdemo.models.repositories")
public class CarRentalManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentalManagementApplication.class, args);
	}

}
