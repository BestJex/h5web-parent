package com.xmbl.h5.web.game.dao;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.xmbl.h5.web.game.entity.FirstPassInfo;
import com.xmbl.h5.web.game.log.LoggerProvider;

@Repository
public class FirstPassDao {
	@Resource
	@Qualifier("mongoTemplateLogin")
	public MongoTemplate mongoTemplateLogin;
	
	public FirstPassInfo getFirstPassInfo(long stageId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("stageId").is(stageId));
		return mongoTemplateLogin.findOne(query, FirstPassInfo.class);
	}
	
	public void addFirstPassInfo(FirstPassInfo info) {
		try {
			mongoTemplateLogin.save(info);
		} catch (Exception e) {
			LoggerProvider.addExceptionLog("插入首次通关信息出错", e);
		}
	}
}
