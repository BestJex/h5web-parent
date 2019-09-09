package com.xmbl.h5.web.game.cron.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xmbl.h5.web.game.service.H5BStaticService;

@Component
public class H5BStaticTask {

	@Autowired
	private H5BStaticService h5BStaticService;
	
	public void sendStatisticInfo() {
		h5BStaticService.sendStatisticInfo();
	}
	
	public void statistic() {
		h5BStaticService.statistic();
	}
}
