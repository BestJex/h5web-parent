package com.xmbl.h5.web.game.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.h5.web.game.dao.WxLoginDao;
import com.xmbl.h5.web.game.entity.WxLogin;
import com.xmbl.h5.web.game.service.WxLoginService;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  WxLoginServiceImpl 
 * @创建时间:  2018年7月31日 下午9:14:55
 * @修改时间:  2018年7月31日 下午9:14:55
 * @类说明:
 */
@Service
public class WxLoginServiceImpl implements WxLoginService{
	
	private static Logger LOGGER = LoggerFactory.getLogger(WxLoginServiceImpl.class);
	
	@Autowired
	private WxLoginDao wxLoginDao;
	
	@Override
	public void addOrUpd(JSONObject userInfo) {
		LOGGER.info(JSONObject.toJSONString(userInfo));
		//		String status = userInfo.getString("status");
		//		String msg = userInfo.getString("msg");
		String country = userInfo.getString("country");
		String province = userInfo.getString("province");
		String city = userInfo.getString("city");
		String nickname = userInfo.getString("nickname");
		int sex = userInfo.getInteger("sex");
		String headimgurl = userInfo.getString("headimgurl");
		String openid = userInfo.getString("openid");
		String language = userInfo.getString("language");
//		String[] privilege = userInfo.getString("privilege");
		// ###
		//		1.查询openid是否存在表中
		//		2.如果不存在新增表中信息
		//		3.如果存在修改表中信息
		WxLogin wxLogin = wxLoginDao.findByOpenid(openid);
		if (wxLogin==null) {//新增
			wxLogin = new WxLogin();
			wxLogin.setOpenid(openid);
			wxLogin.setNickname(nickname);
			wxLogin.setSex(sex);
			wxLogin.setHeadimgurl(headimgurl);
			wxLogin.setCountry(country);
			wxLogin.setProvince(province);
			wxLogin.setCity(city);
			wxLogin.setLanguage(language);
			wxLogin.setLastLoginTime(new Date());
			int count = wxLoginDao.insertOne(wxLogin);
			if (count == 1) {
				LOGGER.info("微信用户登录成功,新增信息成功");
			} else {
				LOGGER.error("微信用户登录失败,新增信息失败");
			}
			
		} else {// 修改
			wxLogin = new WxLogin();
			wxLogin.setOpenid(openid);
			wxLogin.setNickname(nickname);
			wxLogin.setSex(sex);
			wxLogin.setHeadimgurl(headimgurl);
			wxLogin.setCountry(country);
			wxLogin.setProvince(province);
			wxLogin.setCity(city);
			wxLogin.setLanguage(language);
			wxLogin.setLastLoginTime(new Date());
			int count = wxLoginDao.updByOpenid(wxLogin);
			if (count == 1) {
				LOGGER.info("微信用户登录成功,修改信息成功");
			} else {
				LOGGER.info("微信用户登录失败,修改信息失败");
			}
		}
		
		
	}

}
