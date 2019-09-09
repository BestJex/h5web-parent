package com.xmbl.h5.web.game.configure;

import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmbl.h5.web.game.consts.SystemConst;

public class H5Relad implements Reloadable {

	private static final Logger log = LoggerFactory.getLogger(H5Relad.class);

	private static final H5Relad instance = new H5Relad();
	private H5Relad() {}
	public static final H5Relad getInstance() {
		return instance;
	}

	private Properties properties;

	@Override
	public void reload() {
		properties = new Properties();
		FileInputStream inStream;
		try {
			inStream = new FileInputStream(SystemConst.user_dir + path());
			properties.load(inStream);
			
			log.info(instance.getProperty("h5b.statistic.receivers"));
			
		} catch (Exception e) {
			log.error("", e);
		}
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	@Override
	public String path() {
		return "/conf/h5_reload.properties";
	}
}
