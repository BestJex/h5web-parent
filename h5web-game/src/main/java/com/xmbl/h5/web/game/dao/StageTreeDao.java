package com.xmbl.h5.web.game.dao;

import java.util.List;

import javax.annotation.Resource;

import com.xmbl.h5.web.game.proto.StageTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.h5.web.game.entity.tree.base.StageTreeRes;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeShop;
import com.xmbl.h5.web.game.entity.tree.base.TreeCover;
import com.xmbl.h5.web.game.entity.tree.base.TreeDetail;
import com.xmbl.h5.web.game.entity.tree.local.NodeLocalInfo;
import com.xmbl.h5.web.game.entity.tree.local.PanelLocalInfo;
import com.xmbl.h5.web.game.entity.tree.local.TreeLocalInfo;

@Repository
public class StageTreeDao {
	private static final Logger logger = LoggerFactory.getLogger(StageTreeDao.class);
	
	@Resource
	@Qualifier("stageMongoTemplate")
	private MongoTemplate treeTemplate;
	
	@Resource
	@Qualifier("mongoTemplateLogin")
	public MongoTemplate mongoTemplateLogin;

	public List<TreeCover> geTreeCovers(int pageNumber,int pageSize) {
		Query query = new Query();
		query.skip((pageSize - 1) * pageNumber).limit(pageNumber);
		List<TreeCover> treeInfos = treeTemplate.find(query,TreeCover.class);
		return treeInfos;
	}

	public TreeDetail geTreeDetailById(long treeId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("treeId").is(treeId).and("isShow").is(1));
		TreeDetail detail = treeTemplate.findOne(query, TreeDetail.class);
		return detail;
	}

	public StageTreeShop getStageTreeById(long treeId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("treeId").is(treeId).and("isShow").is(1));
		StageTreeShop stageTree = treeTemplate.findOne(query, StageTreeShop.class);
		return stageTree;

	}

	public StageTreeRes getStageTreeRes(long treeId, long id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("treeId").is(treeId).and("id").is(id).and("isShow").is(1));
		StageTreeRes res = treeTemplate.findOne(query, StageTreeRes.class);
		return res;
	}
	
	public void incTreePlayerNum(long treeId) {
		try {
			mongoTemplateLogin.upsert(new Query(Criteria.where("treeId").is(treeId)), new Update().inc("numOfPlayer", 1), TreeLocalInfo.class);
		} catch (Exception e) {
			logger.info("",e);
		}
	}
	
	public void incTreePlayerPassed(long treeId) {
		try {
			mongoTemplateLogin.upsert(new Query(Criteria.where("treeId").is(treeId)), new Update().inc("numOfPassed", 1), TreeLocalInfo.class);
		} catch (Exception e) {
			logger.info("", e);
		}
	}
	
	public void incNodePlayerNum(long treeId, long nodeId) {
		try {
			mongoTemplateLogin.upsert(new Query(Criteria.where("treeId").is(treeId).and("nodeId").is(nodeId)), new Update().inc("playCount", 1), NodeLocalInfo.class);
		} catch (Exception e) {
			logger.info("", e);
		}
	}

	public void incNodePlayerPassed(long treeId, long nodeId, long index) {
		try {
			mongoTemplateLogin.upsert(new Query(Criteria.where("treeId").is(treeId).and("nodeId").is(nodeId).and("index").is(index)), new Update().inc("passCount", 1), NodeLocalInfo.class);
		} catch (Exception e) {
			logger.info("", e);
		}
	}
	
	public TreeLocalInfo getTreeLocalInfo(long treeId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("treeId").is(treeId));
		return mongoTemplateLogin.findOne(query, TreeLocalInfo.class);
	}

	public NodeLocalInfo getNodeLocalInfo(long treeId, long nodeId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("treeId").is(treeId).and("nodeId").is(nodeId));
		return mongoTemplateLogin.findOne(query, NodeLocalInfo.class);
	}

	public PanelLocalInfo getPanelLocalInfo(long treeId, long nodeId, int index) {
		Query query = new Query();
		query.addCriteria(Criteria.where("treeId").is(treeId).and("nodeId").is(nodeId).and("index").is(index));
		return mongoTemplateLogin.findOne(query, PanelLocalInfo.class);
	}
	
	public void save(Object data) {
		mongoTemplateLogin.save(data);
	}
}
