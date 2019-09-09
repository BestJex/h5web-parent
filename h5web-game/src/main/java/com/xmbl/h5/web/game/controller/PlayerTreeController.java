package com.xmbl.h5.web.game.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.xmbl.h5.web.common.consts.EMsgCode;
import com.xmbl.h5.web.common.dto.ResponseResult;
import com.xmbl.h5.web.common.logic.AbstractController;
import com.xmbl.h5.web.game.consts.GameConst;
import com.xmbl.h5.web.game.dao.WxLoginDao;
import com.xmbl.h5.web.game.entity.FirstPassInfo;
import com.xmbl.h5.web.game.entity.WxLogin;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeShop;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeShop.Node;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeShop.Panel;
import com.xmbl.h5.web.game.entity.tree.param.CommitInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerNodeInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerPanelInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo.ConditionLimitInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo.ElementInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo.NodeInfo;
import com.xmbl.h5.web.game.event.EventDispatcher;
import com.xmbl.h5.web.game.event.events.NodeRankEvent;
import com.xmbl.h5.web.game.event.events.StageRankEvent;
import com.xmbl.h5.web.game.event.events.TreeRankEvent;
import com.xmbl.h5.web.game.logic.PlayerFactory;
import com.xmbl.h5.web.game.logic.TreeEnums;
import com.xmbl.h5.web.game.logic.TreeEnums.NodeType;
import com.xmbl.h5.web.game.logic.TreeEnums.StoryType;
import com.xmbl.h5.web.game.mq.G2RTreeRankSender;
import com.xmbl.h5.web.game.service.StageTreeService;
import com.xmbl.h5.web.game.service.player.PlayerTreeService;

@RestController
@RequestMapping("/player/tree")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlayerTreeController extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(PlayerTreeController.class);

	@Autowired
	private PlayerTreeService playerTreeService;
	@Autowired
	StageTreeService treeService;
	@Autowired
	G2RTreeRankSender g2RTreeRankSender;
	@Autowired
	EventDispatcher eventDispatcher;
	@Autowired
	private WxLoginDao wxLoginDao;

	@RequestMapping("/info")
	public ResponseResult getPlayerTreeInfo(@RequestParam(name = "playerId", required = false) String playerId,
			@RequestParam(name = "treeId", required = false) Long treeId) {

		StageTreeShop stageTreeShop = treeService.getStageTree(treeId);
		if (Objects.isNull(stageTreeShop)) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}

		PlayerTreeInfo info = playerTreeService.getPlayerTreeInfo(playerId, treeId);
		if (Objects.isNull(info)) {
			info = PlayerFactory.createPlayerTreeInfo(playerId, treeId, stageTreeShop);
			info = playerTreeService.savePlayerTreeInfo(info);
			treeService.incTreePlayerNum(treeId);
		}

		Long headNodeId = stageTreeShop.getHeadNodeId();
		if (Objects.nonNull(headNodeId)) {
			Node headNode = treeService.getNode(treeId, headNodeId, stageTreeShop);
			if (Objects.nonNull(headNode)) {
				Integer passC = headNode.getPassCondition();
				if (Objects.isNull(passC) || passC <= 0) {
					PlayerNodeInfo playerNodeInfo = playerTreeService.getPlayerNodeInfo(playerId, treeId, headNodeId);
					if (Objects.isNull(playerNodeInfo)) {
						playerNodeInfo = PlayerFactory.createPlayerNodeInfo(playerId, treeId, headNodeId);
						playerNodeInfo.setStatus(GameConst.tree_node_status_pass);
						playerTreeService.savePlayerNodeInfo(playerNodeInfo);
					} else {
						if (playerNodeInfo.getStatus() != GameConst.tree_node_status_pass) {
							playerNodeInfo.setStatus(GameConst.tree_node_status_pass);
							playerTreeService.savePlayerNodeInfo(playerNodeInfo);
						}
					}
					passNode(playerNodeInfo, info, headNodeId, stageTreeShop, headNode);
					playerTreeService.savePlayerTreeInfo(info);
				}
			}
		}

		int rank = playerTreeService.getTreeRank(playerId, treeId);
		info.setRank(rank);
		logger.info("getPlayerTreeInfo: {}", JSON.toJSONString(info));
		return successJson(EMsgCode.success, info);
	}

	@RequestMapping("/node")
	public ResponseResult getPlayerTreeNodeInfo(@RequestParam(name = "playerId", required = false) String playerId,
			@RequestParam(name = "treeId", required = false) long treeId,
			@RequestParam(name = "nodeId", required = false) long nodeId) {

		StageTreeShop stageTree = treeService.getStageTree(treeId);
		if (Objects.isNull(stageTree)) {
			return errorJson(EMsgCode.stage_is_off);
		}

		Node node = treeService.getNode(treeId, nodeId, stageTree);
		if (Objects.isNull(node)) {
			return errorJson(EMsgCode.stage_is_off);
		}

		PlayerTreeInfo playerTreeInfo = playerTreeService.getPlayerTreeInfo(playerId, treeId);
		if (Objects.isNull(playerTreeInfo)) {
			return errorJson(EMsgCode.error);//"获取不到玩家关卡集数据"
		}

		if (!canEnterNode(playerTreeInfo, node)) {
			return errorJson(EMsgCode.error);//"你还不能进入当前节点, 请完成或跳过前置节点"
		}

		PlayerNodeInfo playerNodeInfo = playerTreeService.getPlayerNodeInfo(playerId, treeId, nodeId);
		if (Objects.isNull(playerNodeInfo)) {
			playerNodeInfo = PlayerFactory.createPlayerNodeInfo(playerId, treeId, nodeId);
			playerTreeService.savePlayerNodeInfo(playerNodeInfo);
			treeService.incNodePlayerNum(treeId, playerNodeInfo.getNodeId());
		}
		
		int rank = playerTreeService.getNodeRank(playerId, nodeId);
		playerNodeInfo.setRank(rank);
		
		return successJson(EMsgCode.success, playerNodeInfo);
	}

	/** 是否可以进入节点 */
	private boolean canEnterNode(PlayerTreeInfo playerTreeInfo, Node node) {
		long nodeId = node.getNodeId();
		long priorNodeId = node.getPriorId();
		if (priorNodeId == 0) {
			return true;
		}

		Map<Long, NodeInfo> nodeInfos = playerTreeInfo.getNodeInfos();
		NodeInfo nodeInfo = nodeInfos.get(nodeId);
		if (Objects.nonNull(nodeInfo)) {
			if (nodeInfo.getStatus() == GameConst.player_nodeInfo_status_finish
					|| nodeInfo.getStatus() == GameConst.player_nodeInfo_status_skip) {
				return true;
			}
		}

		if (Objects.isNull(nodeInfo)) {
			NodeInfo priorNodeInfo = nodeInfos.get(priorNodeId);
			if (Objects.isNull(priorNodeInfo)) {
				return false;
			}
			if (priorNodeInfo.getStatus() == GameConst.player_nodeInfo_status_finish
					|| priorNodeInfo.getStatus() == GameConst.player_nodeInfo_status_skip) {
				return true;
			}
		}
		return false;
	}

	@RequestMapping("/node/stage")
	public ResponseResult getPlayerPanelInfo(@RequestParam(name = "playerId", required = false) String playerId,
			@RequestParam(name = "treeId", required = false) long treeId,
			@RequestParam(name = "nodeId", required = false) long nodeId,
			@RequestParam(name = "stageId", required = false) long stageId) {

		if (StringUtils.isEmpty(playerId)) {
			return errorJson(EMsgCode.player_id_can_not_be_null);
		}

		StageTreeShop stageTree = treeService.getStageTree(treeId);
		if (Objects.isNull(stageTree)) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}

		Node node = treeService.getNode(treeId, nodeId, stageTree);
		if (Objects.isNull(node)) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}

		Panel panel = treeService.getPanel(treeId, nodeId, stageId, stageTree);
		if (Objects.isNull(panel)) {
			return errorJson(EMsgCode.stage_is_off);
		}

		PlayerTreeInfo playerTreeInfo = playerTreeService.getPlayerTreeInfo(playerId, treeId);
		if (Objects.isNull(playerTreeInfo)) {
			return errorJson(EMsgCode.error);//"获取不到玩家关卡集数据"
		}

		PlayerNodeInfo playerNodeInfo = playerTreeService.getPlayerNodeInfo(playerId, treeId, nodeId);
		if (Objects.isNull(playerNodeInfo)) {
			playerNodeInfo = PlayerFactory.createPlayerNodeInfo(playerId, treeId, nodeId);
			playerTreeService.savePlayerNodeInfo(playerNodeInfo);
		}

		if (!canEnterPanel(playerTreeInfo, playerNodeInfo, node, panel)) {
			return errorJson(EMsgCode.error);//"你还不能进入当前关卡, 请完成或跳过前置节点"
		}

		PlayerPanelInfo playerPanelInfo = playerTreeService.getPlayerPanelInfo(playerId, treeId, nodeId, stageId);
		if (Objects.isNull(playerPanelInfo)) {
			playerPanelInfo = PlayerFactory.createPlayerPanelInfo(playerId, treeId, nodeId, stageId);
			playerTreeService.savePlayerPanelInfo(playerPanelInfo);
		}
		
		int rank = playerTreeService.getStageRank(playerId, stageId);
		playerPanelInfo.setRank(rank);
		
		return successJson(EMsgCode.success, playerPanelInfo);
	}

	/** 扣除进入关卡需要消耗的体力值 */
	private boolean deductEnengy(PlayerTreeInfo playerTreeInfo, StageTreeShop treeShop, Node node) {
		int energy = playerTreeInfo.getEnergy();
		int pce = getPce(treeShop, node);
		if (energy < pce) {
			return false;
		}
		playerTreeInfo.setEnergy(energy - pce);
		playerTreeService.savePlayerTreeInfo(playerTreeInfo);
		return true;
	}

	/** 是否可以进入关卡 */
	private boolean canEnterPanel(PlayerTreeInfo playerTreeInfo, PlayerNodeInfo playerNodeInfo, Node node,
			Panel panel) {
		if (!canEnterNode(playerTreeInfo, node)) {
			return false;
		}
		Integer openType = node.getOpenType();
		if (TreeEnums.OpenType.noOrder.ordinal() == openType) {
			return true;
		}
		
		List<Long> finishedStageIds = playerNodeInfo.getFinishedStageIds();
		//已完成的关卡不能重复玩
		if (Objects.nonNull(finishedStageIds) && finishedStageIds.contains(panel.getStageId())) {
			return false;
		}

		long index = panel.getIndex();
		if (index == 0) {
			return true;
		}
		
		List<Long> skipedStageIds = playerNodeInfo.getSkipedStageIds();
		if (Objects.nonNull(skipedStageIds) && skipedStageIds.contains(panel.getStageId())) {
			return true;
		}

		long priorIndex = panel.getIndex() - 1;
		Panel priorPanel = treeService.getPanelByIndex(playerTreeInfo.getTreeId(), playerNodeInfo.getNodeId(),
				priorIndex, null);
		if (Objects.isNull(priorPanel)) {
			return false;
		}
		long priorStageId = priorPanel.getStageId();
		if (Objects.nonNull(finishedStageIds) && finishedStageIds.contains(priorStageId)) {
			return true;
		}

		if (Objects.nonNull(skipedStageIds) && skipedStageIds.contains(priorStageId)) {
			return true;
		}
		return false;
	}

	@RequestMapping("/reset")
	public ResponseResult resetTree(@RequestParam(name = "playerId", required = false) String playerId,
			@RequestParam(name = "treeId", required = false) long treeId) {

		StageTreeShop treeShop = treeService.getStageTree(treeId);
		if (Objects.isNull(treeShop)) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}
		Integer st = treeShop.getStoryType();
		if (Objects.isNull(st)) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}

		StoryType storyType = StoryType.getStoryType(st.intValue());
		if (Objects.isNull(storyType)) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}

		if (storyType != StoryType.survive) {
			return errorJson(EMsgCode.error);//"生存模式才需要重置关卡"
		}

		PlayerTreeInfo playerTreeInfo = playerTreeService.getPlayerTreeInfo(playerId, treeId);
		if (Objects.isNull(playerTreeInfo)) {
			return errorJson(EMsgCode.error);//"获取不到玩家关卡集数据"
		}

		playerTreeService.reset(playerTreeInfo, treeShop);
		
		int rank = playerTreeService.getTreeRank(playerId, treeId);
		playerTreeInfo.setRank(rank);
		
		return successJson(EMsgCode.success, playerTreeInfo);
	}

	@RequestMapping("/skip")
	public ResponseResult skip(@RequestParam(name = "playerId", required = false) String playerId,
			@RequestParam(name = "treeId", required = false) long treeId,
			@RequestParam(name = "nodeId", required = false) long nodeId,
			@RequestParam(name = "stageId", required = false) long stageId) {

		if (stageId <= 0) {
			return errorJson(EMsgCode.stageId_must_be_int);
		}

		PlayerTreeInfo playerTreeInfo = playerTreeService.getPlayerTreeInfo(playerId, treeId);
		if (Objects.isNull(playerTreeInfo)) {
			return errorJson(EMsgCode.error);//"获取不到玩家关卡集数据"
		}

		Map<Long, NodeInfo> nodeInfos = playerTreeInfo.getNodeInfos();
		NodeInfo nodeInfo = nodeInfos.get(nodeId);
		if (Objects.nonNull(nodeInfo)) {
			if (nodeInfo.getStatus() == GameConst.player_nodeInfo_status_finish) {
				return errorJson(EMsgCode.error);//"该关卡已经完成"
			}
			if (nodeInfo.getStatus() == GameConst.player_nodeInfo_status_skip) {
				return errorJson(EMsgCode.error);//"该关卡已经跳过"
			}
		}

		StageTreeShop stageTree = treeService.getStageTree(treeId);
		if (Objects.isNull(stageTree)) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}

		Node node = treeService.getNode(treeId, nodeId, stageTree);
		if (Objects.isNull(node)) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}

		Panel panel = treeService.getPanel(treeId, nodeId, stageId, stageTree);
		if (Objects.isNull(panel)) {
			return errorJson(EMsgCode.stage_is_off);
		}

		PlayerNodeInfo playerNodeInfo = playerTreeService.getPlayerNodeInfo(playerId, treeId, nodeId);
		if (Objects.isNull(playerNodeInfo)) {
			playerNodeInfo = PlayerFactory.createPlayerNodeInfo(playerId, treeId, nodeId);
		}

		if (playerNodeInfo.getFinishedStageIds().contains(stageId)) {
			return errorJson(EMsgCode.error);//"该关卡已完成"
		}

		if (playerNodeInfo.getSkipedStageIds().contains(stageId)) {
			return errorJson(EMsgCode.error);//"该关卡已跳过"
		}

		PlayerPanelInfo playerPanelInfo = playerTreeService.getPlayerPanelInfo(playerId, treeId, nodeId, stageId);
		if (Objects.isNull(playerPanelInfo)) {
			playerPanelInfo = PlayerFactory.createPlayerPanelInfo(playerId, treeId, nodeId, stageId);
		}

		NodeType nodeType = NodeType.getNodeType(node.getNodeType().intValue());
		if (nodeType == NodeType.pack) {
			if (panel.getIndex() == 0) {
				long priorNodeId = node.getPriorId();
				NodeInfo priorNodeInfo = nodeInfos.get(priorNodeId);
				if (Objects.isNull(priorNodeInfo) || priorNodeInfo.getStatus() == GameConst.tree_node_status_unpass) {
					return errorJson(EMsgCode.error);//"你还不能进入当前节点, 请完成或跳过前置节点"
				}
			} else {
				long priorIndex = panel.getIndex() - 1;
				Panel priorPanel = treeService.getPanelByIndex(treeId, nodeId, priorIndex, stageTree);
				if (Objects.isNull(priorPanel)) {
					return errorJson(EMsgCode.stage_is_off);
				}

				Long priorStageId = priorPanel.getStageId();
				if (!playerNodeInfo.getFinishedStageIds().contains(priorStageId)
						&& !playerNodeInfo.getSkipedStageIds().contains(priorStageId)) {
					return errorJson(EMsgCode.error);//"你还不能进入当前关卡, 请完成或跳过前置关卡"
				}
			}
		} else if (nodeType == NodeType.normal) {
			long priorNodeId = node.getPriorId();
			NodeInfo priorNodeInfo = nodeInfos.get(priorNodeId);
			if (Objects.isNull(priorNodeInfo) || priorNodeInfo.getStatus() == GameConst.tree_node_status_unpass) {
				return errorJson(EMsgCode.error);//"你还不能进入当前节点, 请完成或跳过前置节点"
			}
		}

		if (Objects.isNull(nodeInfo)) {
			nodeInfo = new NodeInfo(nodeId, GameConst.player_nodeInfo_status_skip);
			nodeInfos.put(nodeId, nodeInfo);
		}
		playerTreeInfo.setSkipStageNum(playerTreeInfo.getSkipStageNum() + 1);
		playerTreeService.savePlayerTreeInfo(playerTreeInfo);

		playerNodeInfo.getSkipedStageIds().add(stageId);
		playerNodeInfo.setStatus(GameConst.tree_node_status_skip);
		playerTreeService.savePlayerNodeInfo(playerNodeInfo);

		playerPanelInfo.setStatus(GameConst.tree_stage_status_skip);
		playerTreeService.savePlayerPanelInfo(playerPanelInfo);
		
		int rank = playerTreeService.getTreeRank(playerId, treeId);
		playerTreeInfo.setRank(rank);
		
		return successJson(EMsgCode.success, playerTreeInfo);
	}

	@RequestMapping("/commit")
	public ResponseResult commit(@RequestParam(name = "jsonData", required = false) String jsonData) {
		logger.info("commit params = " + jsonData);
		CommitInfo result = JSON.parseObject(jsonData, CommitInfo.class);
		if (Objects.isNull(result)) {
			return errorJson(EMsgCode.commit_stage_error);
		}
		String playerId = result.getPlayer_id();
//		String friendId = result.getFriendId();
//		
//		if (StringUtils.isNoneBlank(friendId)) {
//			playerId = friendId;
//		}
		
		int r = result.getType();// 结果，0失败，1成功
		long treeId = result.getTree_id();
		long nodeId = result.getNode_id();
		long stageId = result.getStage_id();
		int step = result.getUsed_step_num();// 使用步数
		int score = result.getScore_num();// 得分
		int costSeconds = result.getUsed_time();// 使用时间
		int remBlockNum = result.getRemove_block_num();// 消除的方块数
		int condition_limit = result.getCondition_limit();// -1不限制，0步数。1时间
		int stage_type = result.getStage_type();// 0消除，1收集

		PlayerTreeInfo playerTreeInfo = playerTreeService.getPlayerTreeInfo(playerId, treeId);
		if (Objects.isNull(playerTreeInfo)) {
			return errorJson(EMsgCode.commit_stage_error);
		}

		if (r == GameConst.tree_stage_result_success) {
			PlayerNodeInfo playerNodeInfo = playerTreeService.getPlayerNodeInfo(playerId, treeId, nodeId);
			if (Objects.isNull(playerNodeInfo)) {
				return errorJson(EMsgCode.commit_stage_error);
			}

			PlayerPanelInfo playerPanelInfo = playerTreeService.getPlayerPanelInfo(playerId, treeId, nodeId, stageId);
			if (Objects.isNull(playerPanelInfo)) {
				playerPanelInfo = PlayerFactory.createPlayerPanelInfo(playerId, treeId, nodeId, stageId);
				playerTreeService.savePlayerPanelInfo(playerPanelInfo);
			}

			StageTreeShop stageTree = treeService.getStageTree(treeId);
			if (Objects.isNull(stageTree)) {
				logger.info("无法获取关卡集({})的信息", treeId);
				return errorJson(EMsgCode.stage_tree_is_off);
			}
			
			int storyType = Objects.isNull(stageTree.getStoryType())?-1:stageTree.getStoryType();

			boolean isConditionLimit = stageTree.getIsConditionLimit();
			int conditionType = Objects.isNull(stageTree.getConditionType()) ? -1 : stageTree.getConditionType();
			int conditonValue = Objects.isNull(stageTree.getConditionValue()) ? 0 : stageTree.getConditionValue();
			if (isConditionLimit) {
				ConditionLimitInfo conditionLimitInfo = playerTreeInfo.getConditionLimitInfo();
				if (Objects.isNull(conditionLimitInfo)) {
					conditionLimitInfo = new ConditionLimitInfo();
					conditionLimitInfo.setType(conditionType);
					conditionLimitInfo.setNum(conditonValue);
					conditionLimitInfo.setCur(conditonValue);
					playerTreeInfo.setConditionLimitInfo(conditionLimitInfo);
					if (!stageTree.getIsConditionLimit()) {
						conditionLimitInfo.setType(-1);
					}
				}
				int cur = conditionLimitInfo.getCur();
				if (conditionType == TreeEnums.ConditionType.step.ordinal()) {
					if (storyType == TreeEnums.StoryType.survive.storyType) {
						if (step>cur) {
							return errorJson(EMsgCode.commit_stage_error);
						}
					}
					conditionLimitInfo.setCur(cur-step);

				} else if (conditionType == TreeEnums.ConditionType.time.ordinal()) {
					if (storyType == TreeEnums.StoryType.survive.storyType) {
						if (costSeconds>cur) {
							return errorJson(EMsgCode.commit_stage_error);
						}
					}
					conditionLimitInfo.setCur(cur-costSeconds);
				}
				
				playerTreeInfo.setConditionLimitInfo(conditionLimitInfo);
			}

			boolean isElementLimit1 = stageTree.getIsElementLimit1();
			if (isElementLimit1) {
				List<ElementInfo> reis = result.getElement1Infos();
				List<ElementInfo> elementInfos = playerTreeInfo.getElement1Infos();
				if (Objects.nonNull(elementInfos) && !elementInfos.isEmpty() && Objects.nonNull(reis) && !reis.isEmpty()) {
					for (int i = 0; i < elementInfos.size(); i++) {
						ElementInfo elementInfo = elementInfos.get(i);
						ElementInfo rei = reis.get(i);
						
						int limit = elementInfo.getCur();
						int use = rei.getSurplus();
						if (storyType == TreeEnums.StoryType.survive.storyType) {
							if (use > limit) {
								return errorJson(EMsgCode.commit_stage_error);
							}
						}
						elementInfo.setCur(limit-use);
					}
				}
			}

			boolean isElementLimit2 = stageTree.getIsElementLimit2();
			if (isElementLimit2) {
				List<ElementInfo> rei2s = result.getElement2Infos();
				List<ElementInfo> element2Infos = playerTreeInfo.getElement2Infos();
				if (Objects.nonNull(element2Infos) && !element2Infos.isEmpty() && Objects.nonNull(rei2s) && !rei2s.isEmpty()) {
					for (int i = 0; i < element2Infos.size(); i++) {
						ElementInfo element2Info = element2Infos.get(i);
						ElementInfo rei2 = rei2s.get(i);
						
						int limit = element2Info.getCur();
						int use = rei2.getSurplus();
						if (storyType == TreeEnums.StoryType.survive.storyType) {
							if (use > limit) {
								return errorJson(EMsgCode.commit_stage_error);
							}
						}
						element2Info.setCur(limit-use);
					}
				}
			}

			Node node = treeService.getNode(treeId, nodeId, stageTree);
			if (Objects.isNull(node)) {
				logger.info("无法获取关卡集({})的节点({})的信息", treeId, nodeId);
				return errorJson(EMsgCode.commit_stage_error);
			}

			WxLogin wxLogin = wxLoginDao.findById(playerId);

			StageRankEvent stageRankEvent = new StageRankEvent();
			stageRankEvent.setConditionLimit(condition_limit);
			stageRankEvent.setPlayerId(playerId);
			stageRankEvent.setRemBlockNum(remBlockNum);
			stageRankEvent.setScore(score);
			stageRankEvent.setStageId(stageId);
			stageRankEvent.setStageType(stage_type);
			stageRankEvent.setCostSeconds(costSeconds);
			stageRankEvent.setUseStepNum(step);
			
			if (Objects.nonNull(wxLogin)) {
				stageRankEvent.setPlayerImg(wxLogin.getHeadimgurl());
				stageRankEvent.setPlayerName(wxLogin.getNickname());
				stageRankEvent.setPlayerSex(wxLogin.getSex());
			}
			eventDispatcher.post(stageRankEvent);

			if (playerPanelInfo.getStatus() != GameConst.tree_stage_status_pass) {
				playerPanelInfo.setStatus(GameConst.tree_stage_status_pass);
				int pass = playerTreeInfo.getPassStageNum() + 1;
				playerTreeInfo.setPassStageNum(pass);

				TreeRankEvent treeRankEvent = new TreeRankEvent();
				treeRankEvent.setPlayerId(playerId);
				treeRankEvent.setTreeId(treeId);
				treeRankEvent.setPass(pass);

				if (Objects.nonNull(wxLogin)) {
					treeRankEvent.setPlayerName(wxLogin.getNickname());
					treeRankEvent.setPlayerSex(wxLogin.getSex());
					treeRankEvent.setPlayerImg(wxLogin.getHeadimgurl());
				}
				eventDispatcher.post(treeRankEvent);

				// returnEnergy(playerTreeInfo, stageTree, node); TODO 先不做体力
			}
			playerPanelInfo.setFinishNum(playerPanelInfo.getFinishNum() + 1);

			if (score > playerPanelInfo.getScore()) {
				playerPanelInfo.setScore(score);
			}
			playerTreeService.savePlayerPanelInfo(playerPanelInfo);

			if (Objects.isNull(playerNodeInfo.getFinishedStageIds())) {
				List<Long> temp = new ArrayList<>();
				playerNodeInfo.setFinishedStageIds(temp);
			}

			if (!playerNodeInfo.getFinishedStageIds().contains(stageId)) {
				playerNodeInfo.getFinishedStageIds().add(stageId);
				int pass = playerNodeInfo.getFinishedStageIds().size();
				int total = node.getPanels().size();
				int progress = Math.round(1.0f * pass / total * 100);
				playerNodeInfo.setProgress(progress);

				NodeRankEvent nodeRankEvent = new NodeRankEvent();
				nodeRankEvent.setPlayerId(playerId);
				nodeRankEvent.setPass(pass);
				nodeRankEvent.setNodeId(nodeId);
				
				if (Objects.nonNull(wxLogin)) {
					nodeRankEvent.setPlayerName(wxLogin.getNickname());
					nodeRankEvent.setPlayerSex(wxLogin.getSex());
					nodeRankEvent.setPlayerImg(wxLogin.getHeadimgurl());
				}
				eventDispatcher.post(nodeRankEvent);
				logger.info("node:{}进度改变，progress:{}", nodeId, progress);
			}

			if (Objects.nonNull(playerNodeInfo.getSkipedStageIds())
					&& playerNodeInfo.getSkipedStageIds().contains(stageId)) {
				playerNodeInfo.getSkipedStageIds().remove(stageId);
				playerTreeInfo.setSkipStageNum(playerTreeInfo.getSkipStageNum() - 1);
			}

			if (node.getNodeType().intValue() == TreeEnums.NodeType.normal.ordinal()) {
				playerNodeInfo.setStatus(GameConst.tree_node_status_pass);

			} else if (node.getNodeType().intValue() == TreeEnums.NodeType.pack.ordinal()) {
				if (playerNodeInfo.getStatus().intValue() == GameConst.tree_node_status_unpass) {
					if (playerNodeInfo.getProgress() >= node.getPassCondition()) {
						playerNodeInfo.setStatus(GameConst.tree_node_status_pass);
					}
				}
			}
			playerTreeService.savePlayerNodeInfo(playerNodeInfo);
			passNode(playerNodeInfo, playerTreeInfo, nodeId, stageTree, node);
			playerTreeService.savePlayerTreeInfo(playerTreeInfo);

			int rank = playerTreeService.getTreeRank(playerId, treeId);
			playerTreeInfo.setRank(rank);
			
			FirstPassInfo firstPassInfo = treeService.getFirstPassInfo(stageId);
			if (Objects.isNull(firstPassInfo)) {
				firstPassInfo = new FirstPassInfo();
				firstPassInfo.setPlayerId(playerId);
				firstPassInfo.setStageId(stageId);
				firstPassInfo.setCreateTime(System.currentTimeMillis());
				if (Objects.isNull(wxLogin)) {
					wxLogin = wxLoginDao.findById(playerId);
					if (Objects.nonNull(wxLogin)) {
						firstPassInfo.setPlayerImage(wxLogin.getHeadimgurl());
						firstPassInfo.setPlayerName(wxLogin.getNickname());
						firstPassInfo.setPlayerSex(wxLogin.getSex());
					}
				}
				treeService.addFirstPassInfo(firstPassInfo);
			}
			
			logger.info("commit succ response:{}", JSON.toJSONString(playerTreeInfo));
			return successJson(EMsgCode.success, playerTreeInfo);
		} else {
			logger.info("commit error response:{}", JSON.toJSONString(playerTreeInfo));
			return successJson(EMsgCode.success, playerTreeInfo);
		}
	}

	private void passNode(PlayerNodeInfo playerNodeInfo, PlayerTreeInfo playerTreeInfo, Long nodeId,
			StageTreeShop stageTree, Node node) {
		if (playerNodeInfo.getStatus().intValue() == GameConst.tree_node_status_pass) {
			NodeInfo nodeInfo = playerTreeInfo.getNodeInfos().get(nodeId);
			if (Objects.isNull(nodeInfo)) {
				nodeInfo = new NodeInfo();
				nodeInfo.setNodeId(nodeId);
				playerTreeInfo.getNodeInfos().put(nodeId, nodeInfo);
			}

			if (nodeInfo.getStatus() != GameConst.player_nodeInfo_status_finish) {
				nodeInfo.setStatus(GameConst.player_nodeInfo_status_finish);
				playerTreeInfo.setPassNodeNum(playerTreeInfo.getPassNodeNum() + 1);

				if (playerTreeInfo.getPassNodeNum() == 0) {
					playerTreeInfo.setProgress(0);
				} else {
					playerTreeInfo.setProgress(
							Math.round(1.0f * playerTreeInfo.getPassNodeNum() / stageTree.getNodes().size() * 100));
				}
				logger.info("playerId:{}tree:{}的progress{}", playerTreeInfo.getPlayerId(), playerTreeInfo.getTreeId(),
						playerTreeInfo.getProgress());
			}
		}

		Long nextNodeId = node.getNextId();
		if (Objects.nonNull(nextNodeId) && nextNodeId > 0) {
			Node nextNode = treeService.getNode(0, nextNodeId, stageTree);
			if (Objects.nonNull(nextNode)) {
				if (Objects.isNull(nextNode.getPassCondition()) || nextNode.getPassCondition() <= 0) {
					PlayerNodeInfo nextPlayerNodeInfo = playerTreeService
							.getPlayerNodeInfo(playerNodeInfo.getPlayerId(), stageTree.getTreeId(), nextNodeId);
					if (Objects.isNull(nextPlayerNodeInfo)) {
						nextPlayerNodeInfo = PlayerFactory.createPlayerNodeInfo(playerNodeInfo.getPlayerId(),
								stageTree.getTreeId(), nextNodeId);
					}
					nextPlayerNodeInfo.setStatus(GameConst.tree_node_status_pass);
					playerTreeService.savePlayerNodeInfo(nextPlayerNodeInfo);
					passNode(nextPlayerNodeInfo, playerTreeInfo, nextNodeId, stageTree, nextNode);
				}
			}
		}

		Long branchNodeId = node.getBranchId();
		if (Objects.nonNull(branchNodeId) && branchNodeId > 0) {
			Node branchNode = treeService.getNode(0, branchNodeId, stageTree);
			if (Objects.nonNull(branchNode)) {
				if (Objects.isNull(branchNode.getPassCondition()) || branchNode.getPassCondition() <= 0) {
					PlayerNodeInfo branchPlayerNodeInfo = playerTreeService
							.getPlayerNodeInfo(playerNodeInfo.getPlayerId(), stageTree.getTreeId(), branchNodeId);
					if (Objects.isNull(branchPlayerNodeInfo)) {
						branchPlayerNodeInfo = PlayerFactory.createPlayerNodeInfo(playerNodeInfo.getPlayerId(),
								stageTree.getTreeId(), branchNodeId);
					}
					branchPlayerNodeInfo.setStatus(GameConst.tree_node_status_pass);
					playerTreeService.savePlayerNodeInfo(branchPlayerNodeInfo);
					passNode(branchPlayerNodeInfo, playerTreeInfo, branchNodeId, stageTree, branchNode);
				}
			}
		}
	}

	private void returnEnergy(PlayerTreeInfo playerTreeInfo, StageTreeShop treeShop, Node node) {
		int currentEnergy = playerTreeInfo.getEnergy();
		int max = treeShop.getMaxEnergy();
		int pce = getPce(treeShop, node);
		int r = currentEnergy + pce > max ? max : currentEnergy + pce;
		playerTreeInfo.setEnergy(r);
	}

	private int getPce(StageTreeShop treeShop, Node node) {
		Integer temp = treeShop.getPce();
		int pce = 0;
		if (Objects.nonNull(temp)) {
			pce = temp.intValue();
		}

		Integer temp2 = node.getPce();
		if (Objects.nonNull(temp2)) {
			pce = temp2.intValue();
		}

		return pce;
	}

	@RequestMapping("/setReturnEnergyFlag")
	public ResponseResult setReturnEngeryFlag(@RequestParam(name = "playerId", required = false) String playerId,
			@RequestParam(name = "treeId", required = false) long treeId,
			@RequestParam(name = "flag", required = false) int flag) {

		PlayerTreeInfo info = playerTreeService.getPlayerTreeInfo(playerId, treeId);
		if (Objects.isNull(info)) {
			return errorJson(EMsgCode.error);
		}

		info.setReturnEnergyFlag(flag);
		playerTreeService.savePlayerTreeInfo(info);

		return successJson(EMsgCode.success, "");
	}

	@RequestMapping("/share")
	public ResponseResult share(@RequestParam(name = "playerId", required = false) String playerId,
			@RequestParam(name = "treeId", required = false) long treeId) {
		PlayerTreeInfo info = playerTreeService.getPlayerTreeInfo(playerId, treeId);
		if (Objects.isNull(info)) {
			return errorJson(EMsgCode.error);
		}

		int currentEnergy = info.getEnergy();
		int shareTime = info.getShareTime();
		StageTreeShop stageTreeShop = treeService.getStageTree(treeId);
		int maxEnergy = stageTreeShop.getMaxEnergy();

		if (currentEnergy > maxEnergy) {
			logger.info("玩家:{}在关卡集:{}的体力值大于限制值:{}", playerId, treeId, maxEnergy);
			return successJson(EMsgCode.success, "");
		} else if (currentEnergy == maxEnergy) {
			return successJson(EMsgCode.success, "");
		} else {
			if (shareTime == 0) {
				shareTime++;
				info.setEnergy(maxEnergy);
			} else if (shareTime == 1) {
				int temp = currentEnergy + GameConst.share_second_return_energy;
				int returnValue = temp < maxEnergy ? temp : maxEnergy;
				info.setEnergy(returnValue);
			}
		}

		info.setShareTime(shareTime++);
		playerTreeService.savePlayerTreeInfo(info);

		ResponseResult.ShareResult shareResult = new ResponseResult.ShareResult();
		shareResult.setEnergy(info.getEnergy());
		return successJson(EMsgCode.success, shareResult);
	}

	@RequestMapping("/stageStart")
	public ResponseResult start(@RequestParam(name = "playerId", required = false) String playerId,
			@RequestParam(name = "treeId", required = false) Long treeId,
			@RequestParam(name = "nodeId", required = false) Long nodeId,
			@RequestParam(name = "stageId", required = false) Long stageId) {

		if (StringUtils.isEmpty(playerId)) {
			return errorJson(EMsgCode.player_id_can_not_be_null);
		}

		StageTreeShop stageTree = treeService.getStageTree(treeId);
		if (Objects.isNull(stageTree)) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}

		Node node = treeService.getNode(treeId, nodeId, stageTree);
		if (Objects.isNull(node)) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}

		Panel panel = treeService.getPanel(treeId, nodeId, stageId, stageTree);
		if (Objects.isNull(panel)) {
			return errorJson(EMsgCode.stage_is_off);
		}

		PlayerTreeInfo playerTreeInfo = playerTreeService.getPlayerTreeInfo(playerId, treeId);
		if (Objects.isNull(playerTreeInfo)) {
			return errorJson(EMsgCode.error);
		}

		PlayerNodeInfo playerNodeInfo = playerTreeService.getPlayerNodeInfo(playerId, treeId, nodeId);
		if (Objects.isNull(playerNodeInfo)) {
			playerNodeInfo = PlayerFactory.createPlayerNodeInfo(playerId, treeId, nodeId);
			playerTreeService.savePlayerNodeInfo(playerNodeInfo);
		}

		if (!canEnterPanel(playerTreeInfo, playerNodeInfo, node, panel)) {
			return errorJson(EMsgCode.error);
		}
		
		int storyType = Objects.isNull(stageTree.getStoryType())?-1:stageTree.getStoryType();

		if (storyType == TreeEnums.StoryType.survive.storyType) {
			boolean isConditionLimit = stageTree.getIsConditionLimit();
			if (isConditionLimit) {
				ConditionLimitInfo conditionLimitInfo = playerTreeInfo.getConditionLimitInfo();
				if (Objects.nonNull(conditionLimitInfo)) {
					int num = conditionLimitInfo.getNum();
					int cur = conditionLimitInfo.getCur();
					if (cur >= num) {
						return errorJson(EMsgCode.error);
					}
				}
			}

			boolean isElementLimit1 = stageTree.getIsElementLimit1();
			if (isElementLimit1) {
				List<ElementInfo> element1Infos = playerTreeInfo.getElement1Infos();
				if (Objects.nonNull(element1Infos) && !element1Infos.isEmpty()) {
					for (int i = 0; i < element1Infos.size(); i++) {
						ElementInfo element1Info = element1Infos.get(i);
						if (element1Info.getCur() >= element1Info.getNum()) {
							return errorJson(EMsgCode.error);
						}
					}
				}
			}

			boolean isElementLimit2 = stageTree.getIsElementLimit2();
			if (isElementLimit2) {
				List<ElementInfo> element2Infos = playerTreeInfo.getElement2Infos();
				if (Objects.nonNull(element2Infos) && !element2Infos.isEmpty()) {
					for (int i = 0; i < element2Infos.size(); i++) {
						ElementInfo element2Info = element2Infos.get(i);
						if (element2Info.getCur() >= element2Info.getNum()) {
							return errorJson(EMsgCode.error);
						}
					}
				}
			}
		}

		// if (!deductEnengy(playerTreeInfo, stageTree, node)) {// TODO 先不做
		// return errorJson("扣除体力值失败");
		// }

		// PlayerPanelInfo playerPanelInfo =
		// playerTreeService.getPlayerPanelInfo(playerId, treeId, nodeId, stageId);
		// if (Objects.isNull(playerPanelInfo)) {
		// playerPanelInfo = PlayerFactory.createPlayerPanelInfo(playerId, treeId,
		// nodeId, stageId);
		// }

		int rank = playerTreeService.getTreeRank(playerId, treeId);
		playerTreeInfo.setRank(rank);
		
		ResponseResult responseResult = successJson(EMsgCode.success, playerTreeInfo);
		return responseResult;
	}
}
