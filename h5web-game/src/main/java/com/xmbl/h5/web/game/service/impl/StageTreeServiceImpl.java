package com.xmbl.h5.web.game.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmbl.h5.web.game.dao.FirstPassDao;
import com.xmbl.h5.web.game.dao.StageTreeDao;
import com.xmbl.h5.web.game.entity.FirstPassInfo;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeRes;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeShop;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeShop.Node;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeShop.Panel;
import com.xmbl.h5.web.game.entity.tree.base.TreeCover;
import com.xmbl.h5.web.game.entity.tree.base.TreeDetail;
import com.xmbl.h5.web.game.entity.tree.local.NodeLocalInfo;
import com.xmbl.h5.web.game.entity.tree.local.PanelLocalInfo;
import com.xmbl.h5.web.game.entity.tree.local.TreeLocalInfo;
import com.xmbl.h5.web.game.service.StageTreeService;

@Service
public class StageTreeServiceImpl implements StageTreeService {
	
	@Autowired 
	private StageTreeDao stageTreeDao;
	@Autowired
	private FirstPassDao firstPassDao;
	
	public List<TreeCover> geTreeCovers(int pageNumber,int pageSize){
		List<TreeCover> treeInfos = stageTreeDao.geTreeCovers(pageNumber,pageSize);
		return treeInfos;
	}
	
	@Override
	public TreeDetail getTreeDetailById(long treeId) {
		TreeDetail detail = stageTreeDao.geTreeDetailById(treeId);
		return detail;
	}

	@Override
	public StageTreeShop getStageTree(long treeId) {
		StageTreeShop stageTree = stageTreeDao.getStageTreeById(treeId);
		return stageTree;
	}

	@Override
	public StageTreeRes getStageTreeRes(long treeId, long id) {
		StageTreeRes res = stageTreeDao.getStageTreeRes(treeId, id);
		return res;
	}

	@Override
	public void incTreePlayerNum(long treeId) {
		stageTreeDao.incTreePlayerNum(treeId);
	}

	@Override
	public void incTreePlayerPassed(long treeId) {
		stageTreeDao.incTreePlayerPassed(treeId);
		
	}

	@Override
	public TreeLocalInfo getTreeLocalInfo(long treeId) {
		return stageTreeDao.getTreeLocalInfo(treeId);
	}

	@Override
	public NodeLocalInfo getNodeLocalInfo(long treeId, long nodeId) {
		return stageTreeDao.getNodeLocalInfo(treeId, nodeId);
	}

	@Override
	public PanelLocalInfo getPanelLocalInfo(long treeId, long nodeId, int index) {
		return stageTreeDao.getPanelLocalInfo(treeId, nodeId, index);
	}

	@Override
	public void saveTreeLocalInfo(TreeLocalInfo info) {
		stageTreeDao.save(info);
	}

	@Override
	public void saveNodeLocalInfo(NodeLocalInfo info) {
		stageTreeDao.save(info);
	}

	@Override
	public void savePanelLocalInfo(PanelLocalInfo info) {
		stageTreeDao.save(info);
	}

	@Override
	public void incNodePlayerNum(long treeId, long nodeId) {
		
	}

	@Override
	public void incNodePlayerPassed(long treeId, long nodeId, int index) {
		
	}

	@Override
	public Node getNode(long treeId, long nodeId, StageTreeShop stageTree) {
		if (Objects.isNull(stageTree)) {
			stageTree = stageTreeDao.getStageTreeById(treeId);
		}
		
		if (Objects.isNull(stageTree)) {
			return null;
		}
		
		List<Node> nodes = stageTree.getNodes();
		if (Objects.isNull(nodes) || nodes.isEmpty()) {
			return null;
		}
		for (Node node : nodes) {
			if (node.getNodeId().longValue() == nodeId) {
				return node;
			}
		}
		return null;
	}

	@Override
	public Panel getPanel(long treeId, long nodeId, long stageId, StageTreeShop stageTree) {
		if (Objects.isNull(stageTree)) {
			stageTree = stageTreeDao.getStageTreeById(treeId);
		}
		
		if (Objects.isNull(stageTree)) {
			return null;
		}
		
		List<Node> nodes = stageTree.getNodes();
		if (Objects.isNull(nodes) || nodes.isEmpty()) {
			return null;
		}
		
		Node node = null;
		for (Node n : nodes) {
			if (n.getNodeId().longValue() == nodeId) {
				node = n;
				break;
			}
		}
		
		if (Objects.isNull(node)) {
			return null;
		}
		
		List<Panel> panels = node.getPanels();
		if (Objects.isNull(panels) || panels.isEmpty()) {
			return null;
		}
		
		for (Panel p : panels) {
			if (p.getStageId().longValue() == stageId) {
				return p;
			}
		}
		return null;
	}

	@Override
	public Panel getPanelByIndex(long treeId, long nodeId, long index, StageTreeShop stageTree) {
		if (Objects.isNull(stageTree)) {
			stageTree = stageTreeDao.getStageTreeById(treeId);
		}
		
		if (Objects.isNull(stageTree)) {
			return null;
		}
		
		List<Node> nodes = stageTree.getNodes();
		if (Objects.isNull(nodes) || nodes.isEmpty()) {
			return null;
		}
		
		Node node = null;
		for (Node n : nodes) {
			if (n.getNodeId().longValue() == nodeId) {
				node = n;
				break;
			}
		}
		
		if (Objects.isNull(node)) {
			return null;
		}
		
		List<Panel> panels = node.getPanels();
		if (Objects.isNull(panels) || panels.isEmpty()) {
			return null;
		}
		
		for (Panel p : panels) {
			if (p.getIndex().longValue() == index) {
				return p;
			}
		}
		return null;
	}

	@Override
	public FirstPassInfo getFirstPassInfo(long stageId) {
		return firstPassDao.getFirstPassInfo(stageId);
	}
	
	public void addFirstPassInfo(FirstPassInfo info) {
		firstPassDao.addFirstPassInfo(info);
	}
}
