package com.xmbl.h5.web.game.entity.tree.rank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "tree_stage_rank_info")
public class TreeStageRankInfo {
	public static final int show_item_num = 100;
	private String playerId;//玩家ID
	private long treeId;//关卡集ID
	private long nodeId;//节点ID
	private long stageId;//关卡ID
	private int goal;//完成目标数
	private long usedTime;//使用时间
	private int usedStep;//步数
	private long score;//积分
	
}
