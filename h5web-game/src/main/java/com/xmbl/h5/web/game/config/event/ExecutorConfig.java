package com.xmbl.h5.web.game.config.event;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

@Configuration
public class ExecutorConfig {
	
	@Bean("eventBusExecutor")
	@ConfigurationProperties(prefix = "event.pool")
	public ThreadPoolExecutorFactoryBean createThreadPoolTaskExecutor() {
		ThreadPoolExecutorFactoryBean threadPoolTaskExecutor = new ThreadPoolExecutorFactoryBean();
		return threadPoolTaskExecutor;
	}
}
