package com.xmbl.h5.web.h5Res;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class,MongoDataAutoConfiguration.class})
public class H5ResApplication {
	public static void main(String[] args) {
		SpringApplication.run(H5ResApplication.class, args);
	}
}

