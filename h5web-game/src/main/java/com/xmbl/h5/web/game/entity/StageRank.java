package com.xmbl.h5.web.game.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StageRankLst 
 * @创建时间:  2018年9月10日 上午10:30:01
 * @修改时间:  2018年9月10日 上午10:30:01
 * @类说明: 关卡排行榜实体
 */
@Data
@Document(collection ="stage_rank")
public class StageRank {
	
	/**
	 * 排行榜id
	 */
	@Id
	private String _id;
	
	/**
	 * 创建人
	 */
	@CreatedBy
	@Field("create_by")
	@Indexed
	private String createdBy;
	
	/**
	 * 创建时间
	 */
	@CreatedDate
	@Field("created_date")
	@Indexed
	private Date createdDate;
	
	/**
	 * 修改人
	 */
	@LastModifiedBy
	@Field("last_modified_by")
	@Indexed
	private String lastModifiedBy;
	
	/**
	 * 修改时间
	 */
	@LastModifiedDate
	@Field("last_modified_date")
	@Indexed
	private Date LastModifiedDate;
	
	/**
	 * 排行榜关卡id
	 */
	@Field("stage_id")
	@Indexed
	private String stageId;
	
	/**
	 * 决定排序字段
	 * <br></br>
	 * 排行榜关卡类型   0 消除   1 收集
	 */
	@Field("stage_type")
	private String stageType;
	
	/**
	 * 决定排序字段
	 * 
	 * 结束规则  -1 无限制  0 限制步数 1 限制时间
	 */
	@Field("condition_limit")
	private String conditionLimit = "-1";
	
	/**
	 * 玩家id
	 */
	@Field("player_id")
	@Indexed
	private String playerId;
	
	/**
	 * 玩家名
	 */
	@Field("player_name")
	private String playerName;
	
	/**
	 * 玩家性别
	 */
	@Field("player_sex")
	private int playerSex;
	
	/**
	 * 玩家头像
	 */
	@Field("player_img")
	private String playerImg;
	
	/**
	 * 是否最先赢 
	 * <br></br>
	 * 0 不是最先赢 1 是最先赢
	 */
	@Field("is_first_win")
	@Indexed
	private String isFirstWin = "0";
	
	/**
	 * 排序方式:
	 *  1  按步数排序 (消除模式)
	 *  2  按时间排序 (消除模式)
	 *  3  按分数排序 (消除模式)
	 *  4  按消除个数排序  个数相同按分数排序(收集模式)
	 */
	@Field("sort_order")
	private Integer sortOrder = 4;
	
	/**
	 * 使用步数 
	 */
	@Field("used_step_num")
	@Indexed
	private Long usedStepNum;
	
	/**
	 * 使用时间 毫秒数
	 */
	@Field("used_time")
	@Indexed
	private Long usedTime;
	
	/**
	 * 移除方块数
	 */
	@Field("remove_block_num")
	@Indexed
	private Long removeBlockNum;
	
	/**
	 * 获得分数
	 */
	@Field("score_num")
	@Indexed
	private Long scoreNum;
	
	
}
