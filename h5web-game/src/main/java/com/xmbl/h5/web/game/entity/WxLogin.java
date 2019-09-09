package com.xmbl.h5.web.game.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  WxLogin 
 * @创建时间:  2018年8月2日 上午11:15:06
 * @修改时间:  2018年8月2日 上午11:15:06
 * @类说明:
 */
@Data
@Document(collection="wx_login")
public class WxLogin {
	@Id
	private String id;
	// 微信openid
	@Field("openid")
	private String openid;
	// 微信昵称
	@Field("nickname")
	private String nickname;
	// 微信 性别 sex
	@Field("sex")
	private int sex;
	// 微信头像
	@Field("headimgurl")
	private String headimgurl;
	// 国家
	@Field("country")
	private String country;
	// 省
	@Field("province")
	private String province;
	// 市
	@Field("city")
	private String city;
	// 语言
	@Field("language")
	private String language;
	
	@Field("lastlogintime")
	private Date lastLoginTime;
}
