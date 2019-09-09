package com.xmbl.h5.web.common.context;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author  noseparte
 * @date  2019/9/9 11:25
 * @Description
 * 		<p>ApplicationContext</p>
 * 		<p>Spring IOC 获取上下文</p>
 */
public abstract class SpringContext {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected ApplicationContext context;

	public final <T> T getBean(String name, Class<T> type) {
		checkContext();
		return context.getBean(name, type);
	}
	
	public final <T> T getBean(Class<T> type) {
		checkContext();
		return context.getBean(type);
	}
	
	public final <T> T getBean(Class<T> type, Object ... args) {
		checkContext();
		return context.getBean(type, args);
	}

	public final String[] getProfile() {
		checkContext();
		return context.getEnvironment().getActiveProfiles();
	}

	public final List<String> getProfileList() {
		checkContext();
		return Arrays.asList(context.getEnvironment().getActiveProfiles());
	}

	public final String getProperties(String key) {
		checkContext();
		return context.getEnvironment().getProperty(key);
	}
	
	public final int getIntProperties(String key) {
		checkContext();
		String temp = context.getEnvironment().getProperty(key);
		int r = 0;
		try {
			r = Integer.parseInt(temp);
		}catch (Exception e) {
			log.error("获取配置key:{}报错", e);
		}
		return r;
	}
	
	private void checkContext() {
		if (Objects.isNull(context)) {
			throw new NullPointerException("applicationContext is null");
		}
	}
	
	public final ApplicationContext getParent() {
		return context.getParent();
	}
}
