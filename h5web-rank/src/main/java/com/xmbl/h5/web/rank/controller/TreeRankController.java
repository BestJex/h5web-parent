package com.xmbl.h5.web.rank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmbl.h5.web.common.consts.EMsgCode;
import com.xmbl.h5.web.common.dto.ResponseResult;
import com.xmbl.h5.web.common.logic.AbstractController;
import com.xmbl.h5.web.rank.logic.NodeRankService;
import com.xmbl.h5.web.rank.logic.StageRankService;
import com.xmbl.h5.web.rank.logic.TreeRankService;
import com.xmbl.h5.web.rank.logic.dto.NodeRankDto;
import com.xmbl.h5.web.rank.logic.dto.StageRankDto;
import com.xmbl.h5.web.rank.logic.dto.TreeRankDto;

@RestController
@RequestMapping("/rank/st")
@CrossOrigin(origins = "*")
public class TreeRankController extends AbstractController {
	private static final Logger log = LoggerFactory.getLogger(TreeRankController.class);

	@Autowired
	private TreeRankService treeRankService;
	@Autowired
	private NodeRankService nodeRankService;
	@Autowired
	private StageRankService stageRankService;

	@RequestMapping("/tree")
	public ResponseResult getTreeRank(
			@RequestParam(name = "treeId", required = false) long treeId,
			@RequestParam(name = "start", required = false) int start,
			@RequestParam(name = "end", required = false) int end,
			@RequestParam(name = "playerId", required = false) String playerId) {

		if (start>end) {
			return errorJson(EMsgCode.error);
		}
		
		TreeRankDto dto = treeRankService.rankRecords(treeId, start, end, playerId);
		log.info("getTreeRank:{}", dto);
		return successJson(EMsgCode.success, dto);

	}

	@RequestMapping("/node")
	public ResponseResult getNodeRank(
			@RequestParam(name = "nodeId", required = false) long nodeId,
			@RequestParam(name = "start", required = false) int start,
			@RequestParam(name = "end", required = false) int end,
			@RequestParam(name = "playerId", required = false) String playerId) {

		if (start>end) {
			return errorJson(EMsgCode.error);
		}
		
		NodeRankDto dto = nodeRankService.rankRecords(nodeId, start, end, playerId);
		log.info("getNodeRank:{}", dto);
		return successJson(EMsgCode.success, dto);

	}

	@RequestMapping("/stage")
	public ResponseResult getStageRank(
			@RequestParam(name = "stageId", required = false) long stageId,
			@RequestParam(name = "start", required = false) int start,
			@RequestParam(name = "end", required = false) int end,
			@RequestParam(name = "playerId", required = false) String playerId) {
		
		if (start>end) {
			return errorJson(EMsgCode.error);
		}
		
		StageRankDto dto = stageRankService.rankRecords(stageId, start, end, playerId);
		log.info("getStageRank:{}", dto);
		return successJson(EMsgCode.success, dto);
	}
	
	@RequestMapping("/myrank/tree")
	public ResponseResult getMyTreeRank(@RequestParam long treeId, @RequestParam String playerId) {
		int rank = treeRankService.getPlayerRank(treeId, playerId);
		return successJson(rank);
	}
	
	@RequestMapping("/myrank/node")
	public ResponseResult getMyNodeRank(@RequestParam long nodeId, @RequestParam String playerId) {
		int rank = nodeRankService.getPlayerRank(nodeId, playerId);
		return successJson(rank);
	}
	
	@RequestMapping("/myrank/stage")
	public ResponseResult getMyStageRank(@RequestParam long stageId, @RequestParam String playerId) {
		int rank = stageRankService.getPlayerRank(stageId, playerId);
		return successJson(rank);
	}
}
