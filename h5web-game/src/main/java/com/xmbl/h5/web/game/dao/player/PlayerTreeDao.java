package com.xmbl.h5.web.game.dao.player;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.xmbl.h5.web.game.entity.tree.player.PlayerNodeInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerPanelInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo;

@Repository
public class PlayerTreeDao {

	@Resource
	@Qualifier("mongoTemplateLogin")
	public MongoTemplate mongoTemplateLogin;

	public PlayerTreeInfo getPlayerTreeInfo(String playerId, long treeId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(playerId).and("treeId").is(treeId));
		PlayerTreeInfo info = mongoTemplateLogin.findOne(query, PlayerTreeInfo.class);
		return info;
	}

	public PlayerTreeInfo updatePlayerTreeInfo(PlayerTreeInfo info) {
		return mongoTemplateLogin.save(info);
	}
	
	public PlayerNodeInfo savePlayerNodeInfo(PlayerNodeInfo nodeInfo) {
		return mongoTemplateLogin.save(nodeInfo);
	}

	public PlayerPanelInfo savePlayerPanelInfo(PlayerPanelInfo panelInfo) {
		return mongoTemplateLogin.save(panelInfo);
	}

	public PlayerNodeInfo getPlayerTreeNodeInfo(String playerId, long treeId, long nodeId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(playerId).and("treeId").is(treeId).and("nodeId").is(nodeId));
		PlayerNodeInfo info = mongoTemplateLogin.findOne(query, PlayerNodeInfo.class);
		return info;
	}

	public PlayerPanelInfo getPlayerPanelInfo(String playerId, long treeId, long nodeId, long stageId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(playerId).and("treeId").is(treeId).and("nodeId").is(nodeId)
				.and("stageId").is(stageId));
		PlayerPanelInfo info = mongoTemplateLogin.findOne(query, PlayerPanelInfo.class);
		return info;
	}
}
