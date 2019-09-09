package com.xmbl.h5.web.game.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.h5.web.game.entity.StageRank;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StageRankService 
 * @创建时间:  2018年9月10日 下午2:26:36
 * @修改时间:  2018年9月10日 下午2:26:36
 * @类说明:
 */
public interface StageRankService {

	/**
	 * 提交关卡成绩，返回是否提交成功
	 * @param stageRank
	 * @return
	 */
	Boolean add(StageRank stageRank);

	StageRank findById(String stageRankId);

	List<StageRank> findByStageIdAndIsFirstWin(String stageId, String isFirstWin);

	List<StageRank> findByStageIdAndPlayerId(String stageId, String playerId);

	JSONObject findMyRankInfoObj(String stageId, String playerId, Integer sortOrder);

	JSONObject getJsonByStageIdAndIsFirstWin(String stageId, String isFirstWin);

	Long countAll(String stageId);

	List<StageRank> findByPageNoAndPageSize(String stageId, Integer sortOrder, int pageNo, int pageSize);

}
