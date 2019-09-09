package com.xmbl.h5.web.game.service;

import com.xmbl.h5.web.game.entity.StageInfo;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StageInfoService 
 * @创建时间:  2018年9月11日 上午10:54:46
 * @修改时间:  2018年9月11日 上午10:54:46
 * @类说明:
 */
public interface StageInfoService {

	/**
	 * 根据关卡id 和 排序号 修改关卡信息
	 * @param stageId
	 * @param sortOrder
	 * @return
	 */
	boolean saveOrUpdByStageIdAndSortOrder(String stageId, Integer sortOrder);

	StageInfo findByStageId(String stageId);

}
