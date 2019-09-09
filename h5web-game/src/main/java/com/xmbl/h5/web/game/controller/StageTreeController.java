package com.xmbl.h5.web.game.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.protobuf.format.JsonFormat;
import com.xmbl.h5.web.common.consts.EMsgCode;
import com.xmbl.h5.web.common.dto.ResponseResult;
import com.xmbl.h5.web.common.logic.AbstractController;
import com.xmbl.h5.web.game.consts.GameConst;
import com.xmbl.h5.web.game.dto.PageData;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeRes;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeShop;
import com.xmbl.h5.web.game.entity.tree.base.TreeCover;
import com.xmbl.h5.web.game.entity.tree.local.NodeLocalInfo;
import com.xmbl.h5.web.game.entity.tree.local.PanelLocalInfo;
import com.xmbl.h5.web.game.entity.tree.local.TreeLocalInfo;
import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo;
import com.xmbl.h5.web.game.logic.TreeFactory;
import com.xmbl.h5.web.game.service.StageTreeService;
import com.xmbl.h5.web.game.service.player.PlayerTreeService;

@RestController
@RequestMapping("/stage/tree")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StageTreeController extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(StageTreeController.class);

	@Autowired
	private StageTreeService stageTreeService;
	@Autowired
	private PlayerTreeService playerTreeService;

	@PostMapping("/covers")
    public ResponseResult getTrees(
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("playerId") String playerId) {
        logger.info("查询关卡集列表，当前用户为 playerId,{}  分页信息为：页数,pageNumber,{},页码,pageSize,{}",playerId,pageNumber,pageSize);
        List<PageData> pdList = new ArrayList<>();
        try {
            List<TreeCover> treeInfos = stageTreeService.geTreeCovers(pageNumber, pageSize);
            if(treeInfos != null && !treeInfos.isEmpty()){
                PageData pd = null;
                for(TreeCover cover : treeInfos){
                    pd = new PageData();
                    pd.put("authorId",cover.getAuthor().getAuthorId());  // 作者ID
                    pd.put("authorName",cover.getAuthor().getName());  // 作者昵称
                    pd.put("avatar",cover.getAuthor().getAvatar());   // 作者头像作者头像
                    pd.put("treeId",cover.getTreeId());   // 关卡集ID
                    pd.put("name",cover.getName());   // 关卡集名称
                    pd.put("type",cover.getStoryType());   // 关卡集类型
                    pd.put("praise",cover.getPraise());   // 收藏数
                    // 玩家在当前关卡集的留存体力
                    PlayerTreeInfo playerTreeInfo = playerTreeService.getPlayerTreeInfo(playerId, cover.getTreeId());
                    if(playerTreeInfo != null){
                        pd.put("energy",playerTreeInfo.getEnergy());   // 体力值
                        pd.put("maxEnergy",cover.getMaxEnergy());   // 体力值上限
                    }else{
                        pd.put("energy", GameConst.player_treeInfo_maxEnergy);   // 体力值
                        pd.put("maxEnergy",cover.getMaxEnergy());   // 体力值上限
                    }
                    pdList.add(pd);
                }
            }
            logger.info(pdList.toString());
            return successJson(EMsgCode.success, pdList);
        }catch (Exception e){
            logger.error("获取关卡集列表失败， errorMsg,{}",e.getMessage());
            return errorJson(EMsgCode.error);
        }
    }

	@RequestMapping("/info")
	public ResponseResult getTree(@RequestParam(name = "treeId", required = false) long treeId) {
		if (treeId <= 0) {
			return errorJson(EMsgCode.stage_tree_is_delete);
		}
		StageTreeShop stageTree = stageTreeService.getStageTree(treeId);
		return successJson(EMsgCode.success, stageTree);
	}

	@RequestMapping("/res")
	public ResponseResult getStageTreeRes(@RequestParam(name = "treeId", required = false) long treeId,
			@RequestParam(name = "stageId", required = false) long stageId) {
		if (treeId <= 0) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}
		if (stageId <= 0) {
			return errorJson(EMsgCode.stageId_must_be_int);
		}
		
		StageTreeShop tree = stageTreeService.getStageTree(treeId);
		if (Objects.isNull(tree)) {
			return errorJson(EMsgCode.stage_tree_is_off);
		}
		
		StageTreeRes res = stageTreeService.getStageTreeRes(treeId, stageId);
		if (Objects.isNull(res)) {
			return errorJson(EMsgCode.stage_is_off);
		}

		String jsonString = JsonFormat.printToString(res.transfer(tree));
		JSONObject jsonObject = JSONObject.parseObject(jsonString);

		JSONObject parent = new JSONObject();
		parent.put("Stage", jsonObject);
		return successJson(EMsgCode.success, parent);
	}

	@RequestMapping("/local/treeinfo")
	public ResponseResult getTreeLocalInfo(@RequestParam(name = "treeId", required = false) long treeId) {
		TreeLocalInfo info = stageTreeService.getTreeLocalInfo(treeId);
		if (Objects.nonNull(info)) {
			info = TreeFactory.createTreeLocalInfo(treeId);
			stageTreeService.saveTreeLocalInfo(info);
		}
		return successJson(EMsgCode.success, info);
	}

	@RequestMapping("/local/nodeinfo")
	public ResponseResult getNodeLocalInfo(@RequestParam(name = "treeId", required = false) long treeId,
			@RequestParam(name = "nodeId", required = false) long nodeId) {
		NodeLocalInfo info = stageTreeService.getNodeLocalInfo(treeId, nodeId);
		if (Objects.nonNull(info)) {
			info = TreeFactory.createNodeLocalInfo(treeId, nodeId);
			stageTreeService.saveNodeLocalInfo(info);
		}
		return successJson(EMsgCode.success, info);
	}

	@RequestMapping("/local/panelinfo")
	public ResponseResult getPanelLocalInfo(@RequestParam(name = "treeId", required = false) long treeId,
			@RequestParam(name = "nodeId", required = false) long nodeId,
			@RequestParam(name = "stageId", required = false) long stageId,
			@RequestParam(name = "index", required = false) int index) {
		PanelLocalInfo info = stageTreeService.getPanelLocalInfo(treeId, nodeId, index);
		if (Objects.nonNull(info)) {
			info = TreeFactory.createPanelLocalInfo(treeId, nodeId, stageId, index);
			stageTreeService.savePanelLocalInfo(info);
		}
		return successJson(EMsgCode.success, info);
	}
}
