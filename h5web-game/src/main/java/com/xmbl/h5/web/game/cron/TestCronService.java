package com.xmbl.h5.web.game.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile({ "dev" })
public class TestCronService {
	static final Logger log = LoggerFactory.getLogger(TestCronService.class);
	
	@Scheduled(cron = "0 0/1 * * * ? ")
	public void evetyMinuteTask() {
	}

	@Scheduled(initialDelay = 1000, fixedRate = 5000)
	public void test() {
	}

	@Scheduled(initialDelay = 1000, fixedRate = 1000*60*60*24)
	public void test2() {
	}
}
