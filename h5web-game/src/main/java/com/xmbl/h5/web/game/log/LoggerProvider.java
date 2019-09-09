package com.xmbl.h5.web.game.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerProvider {
	public static class ExceptionLog{}
	public static final Logger exceptionLogger = LoggerFactory.getLogger(ExceptionLog.class);
	public static final void addExceptionLog(String msg, Throwable t) {
		exceptionLogger.error(msg, t);
	}
}
