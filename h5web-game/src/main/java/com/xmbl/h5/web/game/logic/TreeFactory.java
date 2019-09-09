package com.xmbl.h5.web.game.logic;

import com.xmbl.h5.web.game.entity.tree.local.NodeLocalInfo;
import com.xmbl.h5.web.game.entity.tree.local.PanelLocalInfo;
import com.xmbl.h5.web.game.entity.tree.local.TreeLocalInfo;

public class TreeFactory {
	public static TreeLocalInfo createTreeLocalInfo(long treeId) {
		TreeLocalInfo info = new TreeLocalInfo();
		info.setTreeId(treeId);
		info.setNumOfPlayer(0);
		info.setNumOfPassed(0);
		return info;
	}

	public static NodeLocalInfo createNodeLocalInfo(long treeId, long nodeId) {
		NodeLocalInfo info = new NodeLocalInfo();
		info.setTreeId(treeId);
		info.setNodeId(nodeId);
		info.setPassCount(0);
		info.setPlayCount(0);
		return info;
	}

	public static PanelLocalInfo createPanelLocalInfo(long treeId, long nodeId, long stageId, int index) {
		PanelLocalInfo info = new PanelLocalInfo();
		info.setTreeId(treeId);
		info.setNodeId(nodeId);
		info.setStageId(stageId);
		info.setIndex(index);
		info.setNumOfFinished(0l);
		info.setNumOfPlayer(0l);
		return info;
	}
}
