package com.breno.marketplace_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.breno.marketplace_test.repositories")
public class MarketplaceTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketplaceTestApplication.class, args);
	}
}
