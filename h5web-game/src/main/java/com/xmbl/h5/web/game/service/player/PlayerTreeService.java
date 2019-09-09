package com.xmbl.h5.web.game.service.player;

import com.xmbl.h5.web.game.entity.tree.base.StageTreeShop;
import com.xmbl.h5.web.game.entity.tree.player.PlayerNodeInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerPanelInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo;

public interface PlayerTreeService {

	public PlayerTreeInfo getPlayerTreeInfo(String playerId, long treeId);

	public PlayerNodeInfo getPlayerNodeInfo(String playerId, long treeId, long nodeId);

	public PlayerPanelInfo getPlayerPanelInfo(String playerId, long treeId, long nodeId, long stageId);

	public void reset(PlayerTreeInfo info, StageTreeShop treeShop);

	public PlayerTreeInfo savePlayerTreeInfo(PlayerTreeInfo info);

	public PlayerNodeInfo savePlayerNodeInfo(PlayerNodeInfo nodeInfo);

	public PlayerPanelInfo savePlayerPanelInfo(PlayerPanelInfo panelInfo);
	
	public int getTreeRank(String playerId, long treeId);
	public int getNodeRank(String playerId, long nodeId);
	public int getStageRank(String playerId, long stageId);
}
