package com.xmbl.h5.web.game.entity.tree.rank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="tree_node_rank_info")
public class TreeNodeRankInfo {
	public static final int show_item_num = 100;
	
	private String playerId;
	private long treeId;
	private long nodeId;
	private int finishedStageNum;//完成的关卡数
	private long finishTime;
}
