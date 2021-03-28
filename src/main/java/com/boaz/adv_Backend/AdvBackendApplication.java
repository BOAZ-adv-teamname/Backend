package com.boaz.adv_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class AdvBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvBackendApplication.class, args);
	}

}
