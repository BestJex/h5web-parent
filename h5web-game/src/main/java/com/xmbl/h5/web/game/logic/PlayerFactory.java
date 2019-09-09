package com.xmbl.h5.web.game.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.xmbl.h5.web.game.consts.GameConst;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeRes.PbTaskTarget;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeShop;
import com.xmbl.h5.web.game.entity.tree.player.PlayerPanelInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo.ConditionLimitInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo.ElementInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerNodeInfo;

public class PlayerFactory {
	private static final Logger log = LoggerFactory.getLogger(PlayerFactory.class);

	/** 不为null */
	public static final PlayerTreeInfo createPlayerTreeInfo(String playerId, long treeId, @Nonnull StageTreeShop tree) {
		PlayerTreeInfo info = new PlayerTreeInfo();
		info.setPlayerId(playerId);
		info.setTreeId(treeId);
		info.setProgress(0);
		info.setReturnEnergyFlag(1);

		ConditionLimitInfo conditionLimitInfo = new ConditionLimitInfo();
		int conditionType = Objects.isNull(tree.getConditionType()) ? -1 : tree.getConditionType();
		int conditionValue = Objects.isNull(tree.getConditionValue()) ? 0 : tree.getConditionValue();
		conditionLimitInfo.setType(conditionType);
		conditionLimitInfo.setCur(conditionValue);
		conditionLimitInfo.setNum(conditionValue);
		info.setConditionLimitInfo(conditionLimitInfo);
		if (!tree.getIsConditionLimit()) {
			conditionLimitInfo.setType(-1);
		}

		info.setEnergy(tree.getMaxEnergy());
		info.setNodeInfos(new HashMap<Long, PlayerTreeInfo.NodeInfo>());

		List<ElementInfo> element1Infos = new ArrayList<>();
		if (tree.getIsElementLimit1()) {
			List<PbTaskTarget> targets1 = tree.getALimitTargets1();
			if (Objects.nonNull(targets1) && !targets1.isEmpty()) {
				for (PbTaskTarget target : targets1) {
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
				for (PbTaskTarget target : targets2) {
					ElementInfo elementInfo = new ElementInfo();
					elementInfo.setTarget(target.getTarget());
					elementInfo.setCur(target.getCur());
					elementInfo.setNum(target.getCur());
					element2Infos.add(elementInfo);
				}
			}
		}
		info.setElement2Infos(element2Infos);

		log.info(JSON.toJSONString(info));
		return info;
	}

	public static final PlayerNodeInfo createPlayerNodeInfo(String playerId, long treeId, long nodeId) {
		PlayerNodeInfo info = new PlayerNodeInfo();
		info.setPlayerId(playerId);
		info.setTreeId(treeId);
		info.setNodeId(nodeId);
		info.setProgress(0);
		info.setStatus(GameConst.tree_node_status_unpass);
		info.setScore(0l);
		info.setRank(0);
		info.setProgress(0);
		info.setSkipedStageIds(new ArrayList<>());
		info.setFinishedStageIds(new ArrayList<>());
		log.info(JSON.toJSONString(info));
		return info;
	}

	public static final PlayerPanelInfo createPlayerPanelInfo(String playerId, long treeId, long nodeId, long stageId) {
		PlayerPanelInfo info = new PlayerPanelInfo();
		info.setPlayerId(playerId);
		info.setTreeId(treeId);
		info.setNodeId(nodeId);
		info.setStageId(stageId);
		info.setFinishNum(0);
		info.setScore(0);
		info.setStatus(GameConst.tree_stage_status_unpass);
		log.info(JSON.toJSONString(info));
		return info;
	}

}
