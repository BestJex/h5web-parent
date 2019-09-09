package com.xmbl.h5.web.game.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection ="h5_browser_static")
public class H5BStatic {
	@Id
	private String type;//浏览器类型
	
	@Field("hit")
	private long hit;//当前值
	
	@Field("hit2")
	private long hit2;//前一天的统计结果
}
