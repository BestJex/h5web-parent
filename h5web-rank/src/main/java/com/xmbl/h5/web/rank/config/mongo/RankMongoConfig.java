package com.xmbl.h5.web.rank.config.mongo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.xmbl.h5.web.common.db.mongo.AbstractMongoConfig;
import org.springframework.stereotype.Component;

@Configuration
@Component
@ConfigurationProperties(prefix = "spring.data.mongo.rank")
public class RankMongoConfig extends AbstractMongoConfig {

	@Override
	@Bean(name = "h5_web_rank")
	public MongoTemplate getMongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory());
	}
}
