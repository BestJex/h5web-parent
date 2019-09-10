package com.xmbl.h5.web.rank;

import java.lang.Thread.UncaughtExceptionHandler;

import org.springframework.boot.CommandLineRunner;

import com.xmbl.h5.web.rank.log.LoggerProvider;

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
