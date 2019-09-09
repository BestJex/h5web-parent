package com.xmbl.h5.web.game.cron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xmbl.h5.web.game.cron.task.ConfReloadTask;
import com.xmbl.h5.web.game.cron.task.H5BStaticTask;

@Component
@Profile("pro")
public class ProCronService {
	@Autowired
	private H5BStaticTask h5BStaticTask;
	@Autowired
	private ConfReloadTask confReloadTask;

	@Scheduled(cron = "0 0 12 * * ?")
	public void clock12Task() {
		h5BStaticTask.sendStatisticInfo();
	}

	@Scheduled(cron = "0 0 0 * * ? ")
	public void clock0Task() {
		h5BStaticTask.statistic();
	}

	@Scheduled(cron = "0 0/1 * * * ? ")
	public void evetyMinuteTask() {
		confReloadTask.testReload();
	}
}
