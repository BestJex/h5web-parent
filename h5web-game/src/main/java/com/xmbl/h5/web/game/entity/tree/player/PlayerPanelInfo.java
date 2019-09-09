package com.xmbl.h5.web.game.entity.tree.player;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "player_tree_panel_info")
public class PlayerPanelInfo {
	
	@Id
	private String id;
	private String playerId;//玩家Id
	private long treeId;//关卡集Id
	private long nodeId;//节点Id
	private long stageId;//关卡Id
	private int score;//得分
	private int status;//状态，0表示没玩过，1表示未通关，2表示已通关,3表示已跳过
	private int finishNum;//通关次数
	
	private int rank = -1;
}
