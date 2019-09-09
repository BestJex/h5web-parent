package com.xmbl.h5.web.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = { MongoAutoConfiguration.class })
public class H5WebGameApplication {
	public static void main(String[] args) {
		SpringApplication.run(H5WebGameApplication.class, args);
	}
}
