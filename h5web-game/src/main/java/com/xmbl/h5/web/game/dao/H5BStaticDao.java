package com.xmbl.h5.web.game.dao;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.h5.web.game.entity.H5BStatic;

@Repository
public class H5BStaticDao extends EntityMongoLoginDaoImpl<H5BStatic> {
	private static Logger LOGGER = LoggerFactory.getLogger(H5BStaticDao.class);

	public void addH5BStatic(H5BStatic h5bStatic) {
		try {
			mongoTemplateLogin.upsert(new Query(Criteria.where("type").is(h5bStatic.getType())), new Update().inc("hit", 1).set("last_login_time", new Date()), H5BStatic.class);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
	}
	
	public List<H5BStatic> getH5BStatic() {
		List<H5BStatic> h5bStatic = mongoTemplateLogin.findAll(entityClass);
		return h5bStatic;
	}
	
	public void statistic() {
		List<H5BStatic> h5bStatics = mongoTemplateLogin.findAll(entityClass);
		for (H5BStatic h5bStatic : h5bStatics) {
			h5bStatic.setHit2(h5bStatic.getHit());
			h5bStatic.setHit(0);
			mongoTemplateLogin.save(h5bStatic);
		}
	}
}
