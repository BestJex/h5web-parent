package com.xmbl.h5.web.game.dao;


import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.h5.web.game.entity.WxLogin;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  WxLoginDao 
 * @创建时间:  2018年8月2日 上午11:12:33
 * @修改时间:  2018年8月2日 上午11:12:33
 * @类说明:
 */
@Repository
public class WxLoginDao  extends EntityMongoLoginDaoImpl<WxLogin> {

	/**
	 * 通过openid 查询 微信登录信息
	 * @param openid
	 * @return
	 */
	public WxLogin findByOpenid(String openid) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("openid").is(openid));
			List<WxLogin> wxLoginLst = mongoTemplateLogin.find(query, entityClass);
			if (wxLoginLst.size() >0) {
				return wxLoginLst.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 修改微信用户信息, 返回1 表示成功 0 表示失败
	 * @param wxLogin
	 */
	public int updByOpenid(WxLogin wxLogin) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("openid").is(wxLogin.getOpenid()));
			Update update = new Update();
			update.set("nickname", wxLogin.getNickname());
			update.set("sex", wxLogin.getSex());
			update.set("headimgurl", wxLogin.getHeadimgurl());
			update.set("country", wxLogin.getCountry());
			update.set("province", wxLogin.getProvince());
			update.set("city", wxLogin.getCity());
			update.set("language", wxLogin.getLanguage());
			update.set("lastlogintime", wxLogin.getLastLoginTime());
			mongoTemplateLogin.updateFirst(query, update, entityClass);
			return 1;
		} catch (Exception e) {
			return 0;
		}
		
	}

	/**
	 * 新增微信用户信息,返回1表示成功 0 表示失败
	 * @param wxLogin
	 */
	public int insertOne(WxLogin wxLogin) {
		try {
			mongoTemplateLogin.insert(wxLogin);
			return 1;
		} catch (Exception e) {
			return 0;
		}
		
	}

	public WxLogin findById(String wxUserId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(wxUserId));
			List<WxLogin> wxLoginLst = mongoTemplateLogin.find(query, entityClass);
			if (wxLoginLst.size() >0) {
				return wxLoginLst.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	

}
