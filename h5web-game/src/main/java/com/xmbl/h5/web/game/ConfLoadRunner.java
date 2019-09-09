package com.xmbl.h5.web.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.xmbl.h5.web.game.configure.H5Relad;
import com.xmbl.h5.web.game.cron.task.ConfReloadTask;

@Component
public class ConfLoadRunner implements CommandLineRunner {
	@Autowired private ConfReloadTask confReloadTask;
	
	@Override
	public void run(String... args) throws Exception {
		confReloadTask.register(H5Relad.getInstance());
		confReloadTask.init();
	}
}
