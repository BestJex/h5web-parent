package com.xmbl.h5.web.rank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class H5WebRankApplication {
	public static void main(String[] args) {
		SpringApplication.run(H5WebRankApplication.class, args);
	}
}
