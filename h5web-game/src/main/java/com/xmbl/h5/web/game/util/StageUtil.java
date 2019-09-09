package com.xmbl.h5.web.game.util;

import com.xmbl.h5.web.game.entity.StageRank;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StageUtil 
 * @创建时间:  2018年9月10日 下午1:53:17
 * @修改时间:  2018年9月10日 下午1:53:17
 * @类说明:
 */
public class StageUtil {

	/**
	 * 获取关卡排序方式 
	 * 
	 * 排序方式:
	 *  1  按步数排序 (消除模式)
	 *  2  按时间排序 (消除模式)
	 *  3  按分数排序 (消除模式)
	 *  4  按消除个数排序  个数相同按分数排序(收集模式)
	 *  
	 * @param stageType 玩法类型
	 * @param conditionLimit 条件限制 -1 无限制 0 限制步数 1 限制时间
	 * @return
	 */
	public static Integer getSortOrderType(String stageType, String conditionLimit) {
		if ("1".equals(stageType)) {// 收集
			return 4;
		}
		if ("0".equals(stageType)) {// 消除
			if ("-1".equals(conditionLimit)) {
				return 3;
			}
			if ("0".equals(conditionLimit)) {
				return 1;
			}
			if ("1".equals(conditionLimit)) {
				return 2;
			}
			
		}
		return 4;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isNeedCommit(StageRank stageRank,StageRank stageRank2) {
		String stageType = stageRank.getStageType();
		String conditionLimit = stageRank.getConditionLimit();
		Integer sortOrder =  StageUtil.getSortOrderType(stageType, conditionLimit);
		
		// 只有当前关卡的当前玩家的成绩比之前的成绩好才允许提交成绩
		// 排序方式: 1  按步数排序 (消除模式) 2  按时间排序 (消除模式) 3  按分数排序 (消除模式)  4  按消除个数排序  个数相同按分数排序(收集模式)
		if (sortOrder == 1) {//
			if (stageRank.getUsedStepNum() > stageRank2.getUsedStepNum()) {// 使用步数大于上一次使用步数 不用保存
				return false;
			}
			if (stageRank.getUsedStepNum() == stageRank2.getUsedStepNum()) {// 使用步数相同, 
				if (stageRank.getScoreNum() <= stageRank2.getScoreNum()) {// 使用分数小于等于上次提交分数 不用保存
					return false;
				}
			}
		}
		
		if (sortOrder == 2) {
			if (stageRank.getUsedTime() > stageRank2.getUsedTime()) {// 使用时间大于上一次使用时间 不用保存
				return false;
			}
			if (stageRank.getUsedStepNum() == stageRank2.getUsedStepNum()) {// 使用步数相同, 
				if (stageRank.getScoreNum() <= stageRank2.getScoreNum()) {// 使用分数小于等于上次提交分数 不用保存
					return false;
				}
			}
		}
		
		if (sortOrder == 3) {
			if (stageRank.getScoreNum() <= stageRank2.getScoreNum()) {// 使用分数小于等于上次提交分数 不用保存
				return false;
			}
		}
		
		if (sortOrder == 4) {
			if (stageRank.getRemoveBlockNum() < stageRank2.getRemoveBlockNum()) {// 移除个数 少于上次移除个数  不用保存
				return false;
			}
			if (stageRank.getRemoveBlockNum() == stageRank2.getRemoveBlockNum()) {// 移除块个数相同
				if (stageRank.getScoreNum() <= stageRank2.getScoreNum()) {// 使用分数小于等于上次提交分数 不用保存
					return false;
				}
			}
		}
		return true;
	}
}
