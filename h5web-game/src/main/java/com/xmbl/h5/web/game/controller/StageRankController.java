package com.xmbl.h5.web.game.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.h5.web.common.consts.EMsgCode;
import com.xmbl.h5.web.common.dto.Pagination;
import com.xmbl.h5.web.common.dto.ResponseResult;
import com.xmbl.h5.web.common.logic.AbstractController;
import com.xmbl.h5.web.game.dto.StageRankDto;
import com.xmbl.h5.web.game.entity.StageInfo;
import com.xmbl.h5.web.game.entity.StageRank;
import com.xmbl.h5.web.game.service.StageInfoService;
import com.xmbl.h5.web.game.service.StageRankService;
import com.xmbl.h5.web.util.DateToolUtils;
import com.xmbl.h5.web.util.RegexUtil;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名: RankLstController
 * @创建时间: 2018年9月10日 上午10:10:12
 * @修改时间: 2018年9月10日 上午10:10:12
 * @类说明: 排行榜
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/stageRank")
public class StageRankController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StageRankController.class);

	@Autowired
	private StageRankService stageRankService;

	@Autowired
	private StageInfoService stageInfoService;

	/**
	 * 查询排行榜信息 最先通关玩家 （有个字段标记，是否最新通关玩家） 第几名查询 排行榜分页查询 （首页查前20个，可下拉查询
	 * ）（结束也查前5，不可下拉查询）
	 */
	@RequestMapping(value = "/findStageRank/stageId/{stageId}/playerId/{playerId}", method = RequestMethod.POST)
	public ResponseResult findRankInfo(//
			HttpServletRequest request, @PathVariable(value = "stageId") String stageId, // 关卡id
			@PathVariable(value = "playerId") String playerId, // 玩家id
			@RequestParam(value = "page", defaultValue = "1") int pageNo, // 查询页码 默认 1
			@RequestParam(value = "size", defaultValue = "20") int pageSize // 每页显示条数
	) {
		if (StringUtils.isBlank(stageId)) {
			return errorJson(EMsgCode.stage_id_can_not_be_null);
		}
		if (StringUtils.isBlank(playerId)) {
			return errorJson(EMsgCode.player_id_can_not_be_null);
		}
		LOGGER.info("stageId:{},playerId:{}", stageId, playerId);
		try {
			// 查询关卡排序方式
			StageInfo stageInfo = stageInfoService.findByStageId(stageId);
			if (Objects.isNull(stageInfo)) {
				return errorJson(EMsgCode.stage_not_exist);
			}
			Integer sortOrder = stageInfo.getSortOrder();
			LOGGER.info("sortOrder 排序方式:" + sortOrder);

			// 最先通关
			JSONObject firstWinRankObj = new JSONObject();
			String isFirstWin = "1";
			firstWinRankObj = stageRankService.getJsonByStageIdAndIsFirstWin(stageId, isFirstWin);
			// 我的排名
			JSONObject myRankObj = new JSONObject();
			if (!"0".equals(playerId)) {
				myRankObj = stageRankService.findMyRankInfoObj(stageId, playerId, sortOrder);
			}
			// 排行榜
			JSONObject rankLstObj = new JSONObject();
			StringBuffer pageUrl = request.getRequestURL();
			Long allCount = stageRankService.countAll(stageId);
			Pagination page = new Pagination(pageNo, pageSize, allCount);
			List<StageRank> rankLst = stageRankService.findByPageNoAndPageSize(stageId, sortOrder, pageNo, pageSize);
			page.setDatas(rankLst);
			page.setPageUrl(pageUrl.toString());
			page.setTotalCount(allCount);
			rankLstObj.put("page", page);

			JSONObject resultObj = new JSONObject();
			resultObj.put("firstWinRankObj", firstWinRankObj);
			resultObj.put("myRankObj", myRankObj);
			resultObj.put("rankLstObj", rankLstObj);
			LOGGER.info("=========== 排行榜信息列表  ===========");
			LOGGER.info(JSONObject.toJSONString(resultObj));
			return successJson(EMsgCode.success, resultObj);
		} catch (Exception e) {
			LOGGER.error("findRankInfo", e);
			return errorJson(EMsgCode.error_query_rank);
		}
	}

	/**
	 * 
	 * 排行榜下拉查詢
	 * 
	 */
	@RequestMapping(value = "/findStageRank/pullDown/stageId/{stageId}/playerId/{playerId}", method = RequestMethod.POST)
	public ResponseResult findRankInfoByPullDown(//
			HttpServletRequest request, @PathVariable(value = "stageId") String stageId, // 关卡id
			@PathVariable(value = "playerId") String playerId, // 玩家id
			@RequestParam(value = "page", defaultValue = "2") int pageNo, // 查询页码 默认 1
			@RequestParam(value = "size", defaultValue = "20") int pageSize // 每页显示条数
	) {
		LOGGER.info("stageId:{},playerId:{}", stageId, playerId);
		if (StringUtils.isBlank(stageId)) {
			return errorJson(EMsgCode.stage_id_can_not_be_null);
		}

		if (StringUtils.isBlank(playerId)) {
			return errorJson(EMsgCode.player_id_can_not_be_null);
		}
		try {
			StageInfo stageInfo = stageInfoService.findByStageId(stageId);
			if (Objects.isNull(stageInfo)) {
				return errorJson(EMsgCode.stage_not_exist);
			}

			Integer sortOrder = stageInfo.getSortOrder();
			LOGGER.info("sortOrder 排序方式:" + sortOrder);
			JSONObject rankLstObj = new JSONObject();
			StringBuffer pageUrl = request.getRequestURL();
			Long allCount = stageRankService.countAll(stageId);
			Assert.isTrue(allCount > (pageNo - 1) * pageSize, "没有更多数据啦");
			Pagination page = new Pagination(pageNo, pageSize, allCount);
			List<StageRank> rankLst = stageRankService.findByPageNoAndPageSize(stageId, sortOrder, pageNo, pageSize);
			page.setDatas(rankLst);
			page.setPageUrl(pageUrl.toString());
			page.setTotalCount(allCount);
			rankLstObj.put("page", page);
			return successJson(EMsgCode.success, rankLstObj);
		} catch (Exception e) {
			LOGGER.error("findRankInfoByPullDown", e);
			return errorJson(EMsgCode.error_query_rank);
		}
	}

	/**
	 * 排行榜详情查询 （根据关卡id查询关卡详情）
	 */
	@RequestMapping(value = "/find/stageRankId/{stageRankId}", method = RequestMethod.POST)
	public ResponseResult findOne(@PathVariable(value = "stageRankId") String stageRankId) {
		if (StringUtils.isBlank(stageRankId)) {
			return errorJson(EMsgCode.rank_id_can_not_be_null);
		}
		try {
			StageRank stageRank = stageRankService.findById(stageRankId);
			if (Objects.isNull(stageRank)) {
				return errorJson(EMsgCode.stage_rank_not_exist_or_stage_rank_id_is_wrong);
			}
			StageRankDto stageRankDto = new StageRankDto();
			BeanUtils.copyProperties(stageRank, stageRankDto);
			stageRankDto
					.setCreatedDateStr(DateToolUtils.format(stageRankDto.getCreatedDate(), DateToolUtils.FORMAT_LONG));
			stageRankDto.setLastModifiedDateStr(
					DateToolUtils.format(stageRankDto.getLastModifiedDate(), DateToolUtils.FORMAT_LONG));
			return successJson(EMsgCode.success, stageRankDto);
		} catch (Exception e) {
			LOGGER.error("findOne", e);
			return errorJson(EMsgCode.error_query_rank_detail);
		}
	}

	/**
	 * 排行榜新增 (修改 删除)
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseResult add(@RequestParam(value = "jsonData", required = false) String jsonDataStr) {
		if (StringUtils.isBlank(jsonDataStr)) {
			return errorJson(EMsgCode.error_add_rank_by_no_param);
		}
		try {
			JSONObject jsonData = JSONObject.parseObject(jsonDataStr);
			String stageId = jsonData.getString("stage_id");
			String stageType = jsonData.getString("stage_type");
			String conditionLimit = jsonData.getString("condition_limit");
			String playerId = jsonData.getString("player_id");
			String usedStepNum = jsonData.getString("used_step_num");
			String usedTime = jsonData.getString("used_time");
			String removeBlockNum = jsonData.getString("remove_block_num");
			String scoreNum = jsonData.getString("score_num");

			if (StringUtils.isBlank(stageId)) {
				return errorJson(EMsgCode.stage_id_can_not_be_null);
			}

			Assert.isTrue("0".equals(stageType) || "1".equals(stageType), "游戏规则类型传参有误");
			Assert.isTrue("-1".equals(conditionLimit) || "0".equals(conditionLimit) || "1".equals(conditionLimit),
					"结束规则类型传参有误");
			Assert.isTrue(StringUtils.isNotBlank(playerId), "玩家id不能为空");
			Assert.isTrue(StringUtils.isNotBlank(usedStepNum), "使用步数不能为空");
			Assert.isTrue(StringUtils.isNotBlank(usedTime), "使用时间不能为空");
			Assert.isTrue(StringUtils.isNotBlank(removeBlockNum), "移除方块数不能为空");
			Assert.isTrue(StringUtils.isNotBlank(scoreNum), "获得分数不能为空");

			Assert.isTrue(RegexUtil.checkRegex(RegexUtil.INTEGER, usedStepNum), "使用步数必须为正整数");
			Assert.isTrue(RegexUtil.checkRegex(RegexUtil.INTEGER, usedTime), "使用时间必须为正整数");
			Assert.isTrue(RegexUtil.checkRegex(RegexUtil.INTEGER, removeBlockNum), "移除方块数必须为正整数");
			Assert.isTrue(RegexUtil.checkRegex(RegexUtil.INTEGER, scoreNum), "获得分数必须为正整数");

			// 验证通过 保存数据开始
			StageRank stageRank = new StageRank();
			stageRank.setStageId(stageId);
			stageRank.setStageType(stageType);
			stageRank.setConditionLimit(conditionLimit);
			stageRank.setPlayerId(playerId);
			stageRank.setUsedStepNum(Long.parseLong(usedStepNum));
			stageRank.setUsedTime(Long.parseLong(usedTime));
			stageRank.setRemoveBlockNum(Long.parseLong(removeBlockNum));
			stageRank.setScoreNum(Long.parseLong(scoreNum));
			// stageRank.setSortOrder(StageUtil.getSortOrderType(stageType,
			// conditionLimit));
			Boolean isSucc = stageRankService.add(stageRank);
			Assert.isTrue(isSucc, "提交关卡游戏成绩失败");
			LOGGER.info("提交关卡游戏成绩成功");
			return successJson(EMsgCode.success, "提交关卡游戏成绩成功");
		} catch (Exception e) {
			LOGGER.error("", e);
			return errorJson(EMsgCode.error);
		}
	}

}
