package com.xmbl.h5.web.game.service.impl.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmbl.h5.web.common.logic.ERankType;
import com.xmbl.h5.web.common.proto.RankMsg.G2RQeuryRankReq;
import com.xmbl.h5.web.common.proto.RankMsg.R2GQeuryRankResp;
import com.xmbl.h5.web.game.dao.player.PlayerTreeDao;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeRes.PbTaskTarget;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeShop;
import com.xmbl.h5.web.game.entity.tree.player.PlayerPanelInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo.*;
import com.xmbl.h5.web.game.log.LoggerProvider;
import com.xmbl.h5.web.game.mq.G2RRpcClient;
import com.xmbl.h5.web.game.entity.tree.player.PlayerNodeInfo;
import com.xmbl.h5.web.game.service.player.PlayerTreeService;

@Service
public class PlayerTreeServiceImpl implements PlayerTreeService {
	@Autowired
	private PlayerTreeDao playerTreeDao;
	@Autowired
	private G2RRpcClient g2RRpcClient;

	@Override
	public PlayerTreeInfo getPlayerTreeInfo(String playerId, long treeId) {
		return playerTreeDao.getPlayerTreeInfo(playerId, treeId);
	}

	@Override
	public PlayerNodeInfo getPlayerNodeInfo(String playerId, long treeId, long nodeId) {
		return playerTreeDao.getPlayerTreeNodeInfo(playerId, treeId, nodeId);
	}

	@Override
	public PlayerPanelInfo getPlayerPanelInfo(String playerId, long treeId, long nodeId, long stageId) {
		return playerTreeDao.getPlayerPanelInfo(playerId, treeId, nodeId, stageId);
	}

	@Override
	public void reset(PlayerTreeInfo info, StageTreeShop tree) {
		info.setReturnEnergyFlag(1);
		info.setEnergy(tree.getMaxEnergy());
		info.setProgress(0);
		info.setNodeInfos(new HashMap<>());
		ConditionLimitInfo conditionLimitInfo = new ConditionLimitInfo();

		int conditionType = Objects.isNull(tree.getConditionType()) ? -1 : tree.getConditionType();
		int conditionValue = Objects.isNull(tree.getConditionValue()) ? 0 : tree.getConditionValue();
		conditionLimitInfo.setType(conditionType);
		conditionLimitInfo.setNum(conditionValue);
		conditionLimitInfo.setCur(conditionValue);
		info.setConditionLimitInfo(conditionLimitInfo);
		if (!tree.getIsConditionLimit()) {
			conditionLimitInfo.setType(-1);
		}
		
		List<ElementInfo> element1Infos = new ArrayList<>();
		if (tree.getIsElementLimit1()) {
			List<PbTaskTarget> targets1 = tree.getALimitTargets1();
			if (Objects.nonNull(targets1) && !targets1.isEmpty()) {
				for(PbTaskTarget target : targets1) {
					ElementInfo elementInfo = new ElementInfo();
					elementInfo.setTarget(target.getTarget());
					elementInfo.setCur(target.getCur());
					elementInfo.setNum(target.getCur());
					element1Infos.add(elementInfo);
				}
			}
		}
		info.setElement1Infos(element1Infos);
		
		List<ElementInfo> element2Infos = new ArrayList<>();
		if (tree.getIsElementLimit2()) {
			List<PbTaskTarget> targets2 = tree.getALimitTargets2();
			if (Objects.nonNull(targets2) && !targets2.isEmpty()) {
				for(PbTaskTarget target : targets2) {
					ElementInfo elementInfo = new ElementInfo();
					elementInfo.setTarget(target.getTarget());
					elementInfo.setCur(target.getCur());
					elementInfo.setNum(target.getCur());
					element2Infos.add(elementInfo);
				}
			}
		}
		info.setElement2Infos(element2Infos);
		playerTreeDao.updatePlayerTreeInfo(info);
	}

	@Override
	public PlayerTreeInfo savePlayerTreeInfo(PlayerTreeInfo info) {
		return playerTreeDao.updatePlayerTreeInfo(info);
	}

	@Override
	public PlayerNodeInfo savePlayerNodeInfo(PlayerNodeInfo nodeInfo) {
		return playerTreeDao.savePlayerNodeInfo(nodeInfo);
		
	}

	@Override
	public PlayerPanelInfo savePlayerPanelInfo(PlayerPanelInfo panelInfo) {
		return playerTreeDao.savePlayerPanelInfo(panelInfo);
	}

	@Override
	public int getTreeRank(String playerId, long treeId) {
		return getRank(playerId, treeId, ERankType.TREE);
	}

	@Override
	public int getNodeRank(String playerId, long nodeId) {
		return getRank(playerId, nodeId, ERankType.NODE);
	}

	@Override
	public int getStageRank(String playerId, long stageId) {
		return getRank(playerId, stageId, ERankType.STAGE);
	}
	
	private int getRank(String playerId, long id, ERankType type) {
		G2RQeuryRankReq.Builder builder = G2RQeuryRankReq.newBuilder();
		builder.setPlayerId(playerId);
		builder.setId(id);
		builder.setType(type.type);
		try {
			R2GQeuryRankResp resp = g2RRpcClient.send(builder.build());
			if (Objects.nonNull(resp)) {
				return resp.getRank();
			}
		} catch (Exception e) {
			LoggerProvider.addExceptionLog("远程调用异常", e);
		}
		return 0;
	}
}
