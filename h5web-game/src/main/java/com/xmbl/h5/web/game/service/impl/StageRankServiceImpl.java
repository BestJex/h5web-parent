package com.xmbl.h5.web.game.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.h5.web.game.GameApplicationContext;
import com.xmbl.h5.web.game.dao.StageRankDao;
import com.xmbl.h5.web.game.dao.WxLoginDao;
import com.xmbl.h5.web.game.entity.StageRank;
import com.xmbl.h5.web.game.entity.WxLogin;
import com.xmbl.h5.web.game.service.StageInfoService;
import com.xmbl.h5.web.game.service.StageRankService;
import com.xmbl.h5.web.game.util.DateToolUtils;
import com.xmbl.h5.web.game.util.StageUtil;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StageRankService 
 * @创建时间:  2018年9月10日 下午2:26:36
 * @修改时间:  2018年9月10日 下午2:26:36
 * @类说明:
 */
@Service
public class StageRankServiceImpl implements StageRankService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(StageRankServiceImpl.class);
	
	@Autowired
	private WxLoginDao wxLoginDao;
	
	@Autowired
	private StageRankDao stageRankDao;
	
	@Autowired
	private StageInfoService stageInfoService;
	
	@Autowired
	private GameApplicationContext applicationContext;
	
	@Override
	public Boolean add(StageRank stageRank) {
		try {
			String playerId = stageRank.getPlayerId();
			String stageId = stageRank.getStageId();
			String stageType = stageRank.getStageType();
			String conditionLimit = stageRank.getConditionLimit();
			// 获取排序方式
			Integer sortOrder =  StageUtil.getSortOrderType(stageType, conditionLimit);
			// 修改关卡信息 无关卡信息保存一条记录 有关卡信息 修改关卡记录
			boolean flag = stageInfoService.saveOrUpdByStageIdAndSortOrder(stageId, sortOrder);
			Assert.isTrue(flag, "保存或修改关卡信息失败");
			WxLogin wxLogin = null;
			// 开发 和 测试 环境不需要验证是否是微信玩家
			if (applicationContext.getProfileList().contains("pro")) {
				wxLogin = wxLoginDao.findById(playerId);
				Assert.isTrue(wxLogin != null, "玩家未微信登陆过");
			} else {
				wxLogin = new WxLogin();
				wxLogin.setNickname("dev_nickname");
				wxLogin.setSex((int)Math.round(Math.random()*10)%2);
				wxLogin.setHeadimgurl("head_001");
			}
			// 玩家登录过 可以提交数据
			// 1.查询玩家是否已有成绩记录
			List<StageRank> stageRank2Lst = stageRankDao.findByStageIdAndPlayerId(stageId,playerId);
			StageRank stageRank2 = null;
			if (stageRank2Lst.size() > 0) {
				stageRank2 = stageRank2Lst.get(0);
			}
			Date nowDate = new Date();
			if (stageRank2 == null) {// 玩家没有成绩记录 新增一条成绩操作
				stageRank.setSortOrder(sortOrder);
				stageRank.setCreatedBy(playerId);
				stageRank.setCreatedDate(nowDate);
				stageRank.setLastModifiedBy(playerId);
				stageRank.setLastModifiedDate(nowDate);
				stageRank.setPlayerName(wxLogin.getNickname());
				stageRank.setPlayerSex(wxLogin.getSex());
				stageRank.setPlayerImg(wxLogin.getHeadimgurl());
				Long count = stageRankDao.countByStageId(stageId);
				if (count == 0) {
					stageRank.setIsFirstWin("1");
				} else {
					stageRank.setIsFirstWin("0");
				}
				flag = stageRankDao.add(stageRank);
				return flag;
			} else {// 玩家有成绩记录 修改一条成绩
				boolean isNeedCommit = StageUtil.isNeedCommit(stageRank, stageRank2);
				if (!isNeedCommit) {
					LOGGER.info("玩家成绩不需要提交");
					return true;
				}
				
				// 需要提交
				// stageRank2.setLastModifiedBy(playerId);
				stageRank2.setLastModifiedDate(nowDate);
				stageRank2.setPlayerName(wxLogin.getNickname());
				stageRank2.setPlayerSex(wxLogin.getSex());
				stageRank2.setPlayerImg(wxLogin.getHeadimgurl());
				
				stageRank2.setUsedStepNum(stageRank.getUsedStepNum());
				stageRank2.setUsedTime(stageRank.getUsedTime());
				stageRank2.setRemoveBlockNum(stageRank.getRemoveBlockNum());
				stageRank2.setScoreNum(stageRank.getScoreNum());
				
				flag = stageRankDao.upd(stageRank2);
				return flag;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}
	}

	@Override
	public StageRank findById(String id) {
		try {
			StageRank stageRank = stageRankDao.findById(id);
			return stageRank;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<StageRank> findByStageIdAndIsFirstWin(String stageId, String isFirstWin) {
		try {
			List<StageRank> stageRankLst = stageRankDao.findByStageIdAndIsFirstWin(stageId, isFirstWin);
			return stageRankLst;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	@Override
	public List<StageRank> findByStageIdAndPlayerId(String stageId, String playerId) {
		try {
			List<StageRank> stageRankLst = stageRankDao.findByStageIdAndPlayerId(stageId, playerId);
			return stageRankLst;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	@Override
	public JSONObject findMyRankInfoObj(String stageId, String playerId, Integer sortOrder) {
		JSONObject jsonObject = new JSONObject();
		try {
			List<StageRank> stageRankLst = stageRankDao.findByStageIdAndPlayerId(stageId,playerId);
			Assert.isTrue(stageRankLst.size() > 0, "当前用户还没有排行榜记录");
			StageRank stageRank = stageRankLst.get(0);
			Long rankNum = stageRankDao.findRankCountByStageRank(stageRank);
			jsonObject.put("rankNum", rankNum);
			jsonObject.put("_id", stageRank.get_id());
			jsonObject.put("created_date", stageRank.getCreatedDate());
			jsonObject.put("last_modified_date", stageRank.getLastModifiedDate());
			jsonObject.put("stage_id", stageRank.getStageId());
			jsonObject.put("stage_type", stageRank.getStageType());
			jsonObject.put("condition_limit", stageRank.getConditionLimit());
			jsonObject.put("player_id", stageRank.getPlayerId());
			jsonObject.put("player_name", stageRank.getPlayerName());
			jsonObject.put("player_sex", stageRank.getPlayerSex());
			jsonObject.put("player_img", stageRank.getPlayerImg());
			jsonObject.put("is_first_win", stageRank.getIsFirstWin());
			jsonObject.put("sort_order", stageRank.getSortOrder());
			jsonObject.put("used_step_num", stageRank.getUsedStepNum());
			jsonObject.put("used_time", stageRank.getUsedTime());
			jsonObject.put("remove_block_num", stageRank.getRemoveBlockNum());
			jsonObject.put("score_num", stageRank.getScoreNum());
			return jsonObject;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return jsonObject;
		}
	}

	@Override
	public JSONObject getJsonByStageIdAndIsFirstWin(String stageId, String isFirstWin) {
		JSONObject jsonObject = new JSONObject();
		try {
			List<StageRank> stageRankLst = stageRankDao.findByStageIdAndIsFirstWin(stageId, isFirstWin);
			Assert.isTrue(stageRankLst.size() > 0, "当前还没有最先通过玩家");
			StageRank stageRank = stageRankLst.get(0);
			Long rankNum = stageRankDao.findRankCountByStageRank(stageRank);
			jsonObject.put("rankNum", rankNum);
			jsonObject.put("_id", stageRank.get_id());
			jsonObject.put("created_date", stageRank.getCreatedDate());
			jsonObject.put("created_date_str", DateToolUtils.format(stageRank.getCreatedDate(), "yyyy/MM/dd HH:mm:ss"));
			jsonObject.put("last_modified_date", stageRank.getLastModifiedDate());
			jsonObject.put("last_modified_date_str", DateToolUtils.format(stageRank.getLastModifiedDate(), "yyyy/MM/dd HH:mm:ss"));
			jsonObject.put("stage_id", stageRank.getStageId());
			jsonObject.put("stage_type", stageRank.getStageType());
			jsonObject.put("condition_limit", stageRank.getConditionLimit());
			jsonObject.put("player_id", stageRank.getPlayerId());
			jsonObject.put("player_name", stageRank.getPlayerName());
			jsonObject.put("player_sex", stageRank.getPlayerSex());
			jsonObject.put("player_img", stageRank.getPlayerImg());
			jsonObject.put("is_first_win", stageRank.getIsFirstWin());
			jsonObject.put("sort_order", stageRank.getSortOrder());
			jsonObject.put("used_step_num", stageRank.getUsedStepNum());
			jsonObject.put("used_time", stageRank.getUsedTime());
			jsonObject.put("remove_block_num", stageRank.getRemoveBlockNum());
			jsonObject.put("score_num", stageRank.getScoreNum());
			return jsonObject;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return jsonObject;
		}
	}

	@Override
	public Long countAll(String stageId) {
		try {
			Long count = stageRankDao.countByStageId(stageId);
			return count;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return 0l;
		}
	}

	@Override
	public List<StageRank> findByPageNoAndPageSize(String stageId,Integer sortOrder,int pageNo, int pageSize) {
		try {
			List<StageRank> stageRankLst = stageRankDao.findByPageNoAndPageSize(stageId, sortOrder, pageNo, pageSize);
			return stageRankLst;
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

}
