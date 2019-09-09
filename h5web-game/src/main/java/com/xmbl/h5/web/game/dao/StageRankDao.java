package com.xmbl.h5.web.game.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.h5.web.game.entity.StageRank;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StageRankDao 
 * @创建时间:  2018年9月10日 下午2:42:53
 * @修改时间:  2018年9月10日 下午2:42:53
 * @类说明:
 */
@Repository
public class StageRankDao extends EntityMongoLoginDaoImpl<StageRank> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(StageRankDao.class);
	
	/**
	 * 根据关卡id和玩家id 获取玩家在该关卡的成绩记录
	 * @param stageId
	 * @param playerId
	 * @return
	 */
	public List<StageRank> findByStageIdAndPlayerId(String stageId, String playerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("player_id").is(playerId).and("stage_id").is(stageId));
			List<StageRank> stageRankLst = mongoTemplateLogin.find(query, entityClass);
			return stageRankLst;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return Collections.emptyList();
		}
		
	}

	/**
	 * 根据关卡id 查询该关卡下的总共有多少条玩家记录
	 * @param stageId
	 * @return
	 */
	public Long countByStageId(String stageId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("stage_id").is(stageId));
			Long count = mongoTemplateLogin.count(query, entityClass);
			return count;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return 0l;
		}
	}

	/**
	 * 添加一条记录
	 * @param stageRank
	 * @return
	 */
	public boolean add(StageRank stageRank) {
		try {
			mongoTemplateLogin.save(stageRank);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 修改一条记录
	 * @param stageRank2
	 * @return
	 */
	public boolean upd(StageRank stageRank) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(stageRank.get_id()));
			Update update = new Update();
			update.set("last_modified_date", stageRank.getLastModifiedDate());
			update.set("player_name", stageRank.getPlayerName());
			update.set("player_img", stageRank.getPlayerImg());
			update.set("player_sex", stageRank.getPlayerSex());
			update.set("used_step_num", stageRank.getUsedStepNum());
			update.set("used_time", stageRank.getUsedTime());
			update.set("remove_block_num", stageRank.getRemoveBlockNum());
			update.set("score_num", stageRank.getScoreNum());
			mongoTemplateLogin.updateFirst(query, update, entityClass);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}
	}

	public StageRank findById(String id) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(id));
			List<StageRank> stageRankLst = mongoTemplateLogin.find(query, entityClass);
			if (stageRankLst.size() > 0) {
				return stageRankLst.get(0);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	public List<StageRank> findByStageIdAndIsFirstWin(String stageId, String isFirstWin) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("stage_id").is(stageId).and("is_first_win").is(isFirstWin));
			List<StageRank> stageRankLst = mongoTemplateLogin.find(query, entityClass);
			return stageRankLst;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	public Long findRankCountByStageRank(StageRank stageRank) {
		try {
			String stageId = stageRank.getStageId();
			Integer sortOrder = stageRank.getSortOrder();
			// 查询当前排行是第多少名
			Query query = new Query();
			query.addCriteria(Criteria.where("stage_id").is(stageId));
			List<Order> orders = new ArrayList<Sort.Order>();
			if (sortOrder == 1) {// 步数排行
				Criteria c1 = Criteria.where("used_step_num").lt(stageRank.getUsedStepNum());
				Criteria c2 = Criteria.where("used_step_num").is(stageRank.getUsedStepNum())
									  .and("score_num").gt(stageRank.getScoreNum());
				Criteria c3 = Criteria.where("used_step_num").is(stageRank.getUsedStepNum())
									  .and("score_num").is(stageRank.getScoreNum())
									  .and("last_modified_date").lt(stageRank.getLastModifiedDate());
				Criteria c4 = Criteria.where("used_step_num").is(stageRank.getUsedStepNum())
						.and("score_num").is(stageRank.getScoreNum())
						.and("last_modified_date").is(stageRank.getLastModifiedDate())
						.and("created_date").lte(stageRank.getCreatedDate());
				query.addCriteria(new Criteria().orOperator(c1,c2,c3,c4));
				orders.add(new Sort.Order(Sort.Direction.ASC,"used_step_num"));
				orders.add(new Sort.Order(Sort.Direction.DESC,"score_num"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"last_modified_date"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"created_date"));
			}
			if (sortOrder == 2) {// 时间排序
				Criteria c1 = Criteria.where("used_time").lt(stageRank.getUsedTime());
				Criteria c2 = Criteria.where("used_time").is(stageRank.getUsedTime())
									  .and("score_num").gt(stageRank.getScoreNum());
				Criteria c3 = Criteria.where("used_time").is(stageRank.getUsedTime())
									  .and("score_num").is(stageRank.getScoreNum())
									  .and("last_modified_date").lt(stageRank.getLastModifiedDate());
				Criteria c4 = Criteria.where("used_time").is(stageRank.getUsedTime())
						.and("score_num").is(stageRank.getScoreNum())
						.and("last_modified_date").is(stageRank.getLastModifiedDate())
						.and("created_date").lte(stageRank.getCreatedDate());
				query.addCriteria(new Criteria().orOperator(c1,c2,c3,c4));
				orders.add(new Sort.Order(Sort.Direction.ASC,"used_time"));
				orders.add(new Sort.Order(Sort.Direction.DESC,"score_num"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"last_modified_date"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"created_date"));
			}
			if (sortOrder == 3) {
				Criteria c1 = Criteria.where("score_num").gt(stageRank.getScoreNum());
				Criteria c2 = Criteria.where("score_num").is(stageRank.getScoreNum())
									  .and("last_modified_date").lt(stageRank.getLastModifiedDate());
				Criteria c3 = Criteria.where("score_num").is(stageRank.getScoreNum())
						.and("last_modified_date").is(stageRank.getLastModifiedDate())
						.and("created_date").lte(stageRank.getCreatedDate());
				query.addCriteria(new Criteria().orOperator(c1,c2,c3));
				orders.add(new Sort.Order(Sort.Direction.DESC,"score_num"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"last_modified_date"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"created_date"));
			}
			if (sortOrder == 4) {
				Criteria c1 = Criteria.where("remove_block_num").gt(stageRank.getRemoveBlockNum());
				Criteria c2 = Criteria.where("remove_block_num").is(stageRank.getRemoveBlockNum())
									  .and("score_num").gt(stageRank.getScoreNum());
				Criteria c3 = Criteria.where("remove_block_num").is(stageRank.getRemoveBlockNum())
									  .and("score_num").is(stageRank.getScoreNum())
									  .and("last_modified_date").lt(stageRank.getLastModifiedDate());
				Criteria c4 = Criteria.where("remove_block_num").is(stageRank.getRemoveBlockNum())
						.and("score_num").is(stageRank.getScoreNum())
						.and("last_modified_date").is(stageRank.getLastModifiedDate())
						.and("created_date").lte(stageRank.getCreatedDate());
				query.addCriteria(new Criteria().orOperator(c1,c2,c3,c4));
				orders.add(new Sort.Order(Sort.Direction.DESC,"remove_block_num"));
				orders.add(new Sort.Order(Sort.Direction.DESC,"score_num"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"last_modified_date"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"created_date"));
			}
			Sort sort = new Sort(orders);
			query.with(sort);
			Long rankNum = mongoTemplateLogin.count(query, entityClass);
			return rankNum;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return 0l;
		}
	}

	public List<StageRank> findByPageNoAndPageSize(String stageId, Integer sortOrder, int pageNo, int pageSize) {
		try {
			Query query = new Query();
			List<Order> orders = new ArrayList<Sort.Order>();
			if (sortOrder == 1) {// 步数排行
				query.addCriteria(Criteria.where("stage_id").is(stageId));
				orders.add(new Sort.Order(Sort.Direction.ASC,"used_step_num"));
				orders.add(new Sort.Order(Sort.Direction.DESC,"score_num"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"last_modified_date"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"created_date"));
			}
			if (sortOrder == 2) {// 时间排序
				query.addCriteria(Criteria.where("stage_id").is(stageId));
				orders.add(new Sort.Order(Sort.Direction.ASC,"used_time"));
				orders.add(new Sort.Order(Sort.Direction.DESC,"score_num"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"last_modified_date"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"created_date"));
			}
			if (sortOrder == 3) {
				query.addCriteria(Criteria.where("stage_id").is(stageId));
				orders.add(new Sort.Order(Sort.Direction.DESC,"score_num"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"last_modified_date"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"created_date"));
			}
			if (sortOrder == 4) {
				query.addCriteria(Criteria.where("stage_id").is(stageId));
				orders.add(new Sort.Order(Sort.Direction.DESC,"remove_block_num"));
				orders.add(new Sort.Order(Sort.Direction.DESC,"score_num"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"last_modified_date"));
				orders.add(new Sort.Order(Sort.Direction.ASC,"created_date"));
			}
			Sort sort = new Sort(orders);
			query.with(sort);
			query.limit(pageSize);
			query.skip((pageNo-1) * pageSize);
			List<StageRank> stageRankLst = mongoTemplateLogin.find(query, entityClass);
			return stageRankLst;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return Collections.emptyList();
		}
	}

}
