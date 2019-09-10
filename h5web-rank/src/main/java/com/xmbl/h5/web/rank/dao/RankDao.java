package com.xmbl.h5.web.rank.dao;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;
import com.xmbl.h5.web.rank.consts.RankConsts;
import com.xmbl.h5.web.rank.logic.entity.NodeRankRecord;
import com.xmbl.h5.web.rank.logic.entity.StageRankRecord;
import com.xmbl.h5.web.rank.logic.entity.RankCollectionList;
import com.xmbl.h5.web.rank.logic.entity.TreeRankRecord;

@Repository
public class RankDao {
	@Autowired
	@Qualifier("h5_web_rank")
	private MongoTemplate h5WebRankTemplate;

	public void addTreeRank(TreeRankRecord record) {
		h5WebRankTemplate.save(record, RankConsts.tree_rank_mongo_cname_prefix + record.getTreeId());
	}

	public void remTreeRank(TreeRankRecord record) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(record.getPlayerId()));
		h5WebRankTemplate.remove(query, RankConsts.tree_rank_mongo_cname_prefix + record.getTreeId());
	}
	
	public boolean updateTreeRank(TreeRankRecord record) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(record.getPlayerId()));
		
		Update update = new Update();
		update.set("progress", record.getProgress()).set("time", record.getTime())
		.set("playerName", record.getPlayerName()).set("playerImg", record.getPlayerImg())
		.set("playerSex", record.getPlayerSex()).set("score", record.getScore());
		
		UpdateResult result = h5WebRankTemplate.updateFirst(query, update, TreeRankRecord.class, RankConsts.tree_rank_mongo_cname_prefix + record.getTreeId());
		return result.wasAcknowledged();
	}

	public List<TreeRankRecord> getTreeRankRecords(long treeId) {
		return h5WebRankTemplate.findAll(TreeRankRecord.class, RankConsts.tree_rank_mongo_cname_prefix + treeId);
	}
	
	public boolean isTreeRankExists(long treeId) {
		return h5WebRankTemplate.collectionExists(RankConsts.tree_rank_mongo_cname_prefix + treeId);
	}
	
	

	public void addNodeRank(NodeRankRecord record) {
		h5WebRankTemplate.save(record, RankConsts.node_rank_mongo_cname_prefix + record.getNodeId());
	}

	public void remNodeRank(NodeRankRecord record) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(record.getPlayerId()));
		h5WebRankTemplate.remove(query, RankConsts.node_rank_mongo_cname_prefix + record.getNodeId());
	}
	
	public boolean updateNodeRank(NodeRankRecord record) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(record.getPlayerId()));
		
		Update update = new Update();
		update.set("progress", record.getProgress()).set("time", record.getTime())
		.set("playerName", record.getPlayerName()).set("playerImg", record.getPlayerImg())
		.set("playerSex", record.getPlayerSex()).set("score", record.getScore());
		
		UpdateResult result = h5WebRankTemplate.updateFirst(query, update, TreeRankRecord.class, RankConsts.node_rank_mongo_cname_prefix + record.getNodeId());
		return result.wasAcknowledged();
	}

	public List<NodeRankRecord> getNodeRankRecords(long nodeId) {
		return h5WebRankTemplate.findAll(NodeRankRecord.class, RankConsts.node_rank_mongo_cname_prefix + nodeId);
	}
	
	public boolean isNodeRankExists(long nodeId) {
		return h5WebRankTemplate.collectionExists(RankConsts.node_rank_mongo_cname_prefix + nodeId);
	}
	
	

	public void addStageRank(StageRankRecord record) {
		h5WebRankTemplate.save(record, RankConsts.stage_rank_mongo_cname_prefix + record.getStageId());
	}

	public void remStageRank(StageRankRecord record) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(record.getPlayerId()));
		h5WebRankTemplate.remove(query, RankConsts.stage_rank_mongo_cname_prefix + record.getStageId());
	}
	
	public boolean updateStageRank(StageRankRecord record) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(record.getPlayerId()));
		
		Update update = new Update();
		update.set("score", record.getScore()).set("time", record.getTime())
		.set("conditionLimit", record.getConditionLimit()).set("remBlockNum", record.getRemBlockNum())
		.set("useStep", record.getUseStep()).set("costSeconds", record.getCostSeconds())
		.set("playerName", record.getPlayerName()).set("playerImg", record.getPlayerImg())
		.set("playerSex", record.getPlayerSex());
		
		UpdateResult result = h5WebRankTemplate.updateFirst(query, update, TreeRankRecord.class, RankConsts.stage_rank_mongo_cname_prefix + record.getStageId());
		return result.wasAcknowledged();
	}

	public List<StageRankRecord> getStageRankRecords(long stageId) {
		return h5WebRankTemplate.findAll(StageRankRecord.class, RankConsts.stage_rank_mongo_cname_prefix + stageId);
	}
	
	public boolean isStageRankExists(long stageId) {
		return h5WebRankTemplate.collectionExists(RankConsts.stage_rank_mongo_cname_prefix + stageId);
	}
	
	
	public List<Long> getTreeRankCollectionList() {
		Document queryD = new Document();
		Document fieldsD = new Document();
		fieldsD.put("treeIds", true);
		Query query = new BasicQuery(queryD, fieldsD);
		RankCollectionList list = h5WebRankTemplate.findOne(query, RankCollectionList.class, RankConsts.tree_rank_collection_list);
		if (Objects.isNull(list)) {
			return Collections.emptyList();
		}
		return list.getTreeIds();
	}
	
	public void addTreeIdToSet(long treeId) {
		Query query = new Query();
		Update update = new Update();
		update.addToSet("treeIds", treeId);
		
		if (h5WebRankTemplate.collectionExists(RankConsts.tree_rank_collection_list)) {
			h5WebRankTemplate.updateFirst(query, update, RankConsts.tree_rank_collection_list);
		}
		else {
			h5WebRankTemplate.createCollection(RankConsts.tree_rank_collection_list);
			RankCollectionList list = new RankCollectionList();
			save(list, RankConsts.tree_rank_collection_list);
			h5WebRankTemplate.updateFirst(query, update, RankConsts.tree_rank_collection_list);
		}
	}
	
	public List<Long> getNodeRankCollectionList() {
		Document queryD = new Document();
		Document fieldsD = new Document();
		fieldsD.put("nodeIds", true);
		Query query = new BasicQuery(queryD, fieldsD);
		RankCollectionList list = h5WebRankTemplate.findOne(query, RankCollectionList.class, RankConsts.node_rank_collection_list);
		if (Objects.isNull(list)) {
			return Collections.emptyList();
		}
		return list.getNodeIds();
	}
	
	public void addNodeIdToSet(long nodeId) {
		Query query = new Query();
		Update update = new Update();
		update.addToSet("nodeIds", nodeId);
		
		if (h5WebRankTemplate.collectionExists(RankConsts.node_rank_collection_list)) {
			h5WebRankTemplate.updateFirst(query, update, RankConsts.node_rank_collection_list);
		}
		else {
			h5WebRankTemplate.createCollection(RankConsts.node_rank_collection_list);
			RankCollectionList list = new RankCollectionList();
			save(list, RankConsts.node_rank_collection_list);
			h5WebRankTemplate.updateFirst(query, update, RankConsts.node_rank_collection_list);
		}
	}
	
	public List<Long> getStageRankCollectionList() {
		Document queryD = new Document();
		Document fieldsD = new Document();
		fieldsD.put("stageIds", true);
		Query query = new BasicQuery(queryD, fieldsD);
		RankCollectionList list = h5WebRankTemplate.findOne(query, RankCollectionList.class, RankConsts.stage_rank_collection_list);
		if (Objects.isNull(list)) {
			return Collections.emptyList();
		}
		return list.getStageIds();
	}
	
	public void addStageIdToSet(long stageId) {
		Query query = new Query();
		Update update = new Update();
		update.addToSet("stageIds", stageId);
		
		if (h5WebRankTemplate.collectionExists(RankConsts.stage_rank_collection_list)) {
			h5WebRankTemplate.updateFirst(query, update, RankConsts.stage_rank_collection_list);
		}
		else {
			h5WebRankTemplate.createCollection(RankConsts.stage_rank_collection_list);
			RankCollectionList list = new RankCollectionList();
			save(list, RankConsts.stage_rank_collection_list);
			h5WebRankTemplate.updateFirst(query, update, RankConsts.stage_rank_collection_list);
		}
	}
	
	public void save(Object object, String collectionName) {
		h5WebRankTemplate.save(object, collectionName);
	}
}
