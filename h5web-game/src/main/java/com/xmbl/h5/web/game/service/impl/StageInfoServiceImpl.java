package com.xmbl.h5.web.game.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmbl.h5.web.game.dao.StageInfoDao;
import com.xmbl.h5.web.game.entity.StageInfo;
import com.xmbl.h5.web.game.service.StageInfoService;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StageInfoServiceImpl 
 * @创建时间:  2018年9月11日 上午10:55:09
 * @修改时间:  2018年9月11日 上午10:55:09
 * @类说明:
 */
@Service
public class StageInfoServiceImpl implements StageInfoService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(StageInfoServiceImpl.class);
	
	@Autowired
	private StageInfoDao stageInfoDao;
	
	@Override
	public boolean saveOrUpdByStageIdAndSortOrder(String stageId, Integer sortOrder) {
		try {
			StageInfo stageInfo = stageInfoDao.findByStageIdAndSortOrder(stageId,sortOrder);
			boolean flag = false;
			if (stageInfo == null) {// 保存
				flag = stageInfoDao.saveByStageIdAndSortOrder(stageId,sortOrder);
			} else {// 修改
				flag = stageInfoDao.updByStageIdAndSortOrder(stageId,sortOrder);
			}
			return flag;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}
	}

	@Override
	public StageInfo findByStageId(String stageId) {
		try {
			StageInfo stageInfo = stageInfoDao.findByStageId(stageId);
			return stageInfo;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

}
