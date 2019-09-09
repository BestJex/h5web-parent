package com.xmbl.h5.web.game;

import java.lang.Thread.UncaughtExceptionHandler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.xmbl.h5.web.game.log.LoggerProvider;

@Component
public class ExceptionHandlerRunner implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				LoggerProvider.addExceptionLog("未捕获异常:", e);
			}
		});
	}
}
