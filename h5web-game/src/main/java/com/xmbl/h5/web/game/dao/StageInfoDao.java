package com.xmbl.h5.web.game.dao;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.h5.web.game.entity.StageInfo;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StageInfoDao 
 * @创建时间:  2018年9月10日 下午2:42:53
 * @修改时间:  2018年9月10日 下午2:42:53
 * @类说明: 平台保存关卡信息
 */
@Repository
public class StageInfoDao extends EntityMongoLoginDaoImpl<StageInfo> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(StageInfoDao.class);
	
	/**
	 * 根据关卡id 和 排序号 查询关卡信息 
	 * @param stageId
	 * @param playerId
	 * @return
	 */
	public StageInfo findByStageIdAndSortOrder(String stageId, Integer sortOrder) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("sort_order").is(sortOrder).and("stage_id").is(stageId));
			List<StageInfo> stageInfoLst = mongoTemplateLogin.find(query, entityClass);
			if (stageInfoLst.size() >0) {
				return stageInfoLst.get(0);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
	}

	/**
	 * 修改关卡信息 通过关卡id 和 排序号
	 * @param stageId
	 * @param sortOrder
	 * @return
	 */
	public boolean updByStageIdAndSortOrder(String stageId, Integer sortOrder) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("stage_id").is(stageId));
			Update update = new Update();
			update.set("sort_order", sortOrder);
			mongoTemplateLogin.updateFirst(query, update, entityClass);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 保存关卡信息 通过关卡id和排序号
	 * @param stageId
	 * @param sortOrder
	 * @return
	 */
	public boolean saveByStageIdAndSortOrder(String stageId, Integer sortOrder) {
		try {
			StageInfo stageInfo = new StageInfo();
			Date nowDate = new Date();
			stageInfo.setCreatedDate(nowDate);
			stageInfo.setLastModifiedDate(nowDate);
			stageInfo.setStageId(stageId);
			stageInfo.setSortOrder(sortOrder);
			mongoTemplateLogin.save(stageInfo);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 根据关卡id 查询关卡信息
	 * @param stageId
	 * @return
	 */
	public StageInfo findByStageId(String stageId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("stage_id").is(stageId));
			List<StageInfo> stageInfoLst = mongoTemplateLogin.find(query, entityClass);
			if (stageInfoLst.size() >0) {
				return stageInfoLst.get(0);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

}
