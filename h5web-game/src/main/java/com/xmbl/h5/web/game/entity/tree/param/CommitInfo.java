package com.xmbl.h5.web.game.entity.tree.param;

import java.util.List;

import com.xmbl.h5.web.game.entity.tree.player.PlayerTreeInfo.ElementInfo;

import lombok.Data;

@Data
public class CommitInfo {
	private long tree_id;
	private long node_id;
	private long stage_id;
	private int type;//0表示失败，1表示成功
	private int stage_type;
	private int condition_limit;
	private String player_id;
	private int used_step_num;
	private int used_time;
	private int remove_block_num;
	private int score_num;
	private List<ElementInfo> element1Infos;
	private List<ElementInfo> element2Infos;
	
	private String friendId;
}
