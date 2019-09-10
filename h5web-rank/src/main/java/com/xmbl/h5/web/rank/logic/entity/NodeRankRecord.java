package com.xmbl.h5.web.rank.logic.entity;

import com.xmbl.h5.web.util.MathUtil;

import lombok.Data;

@Data
public class NodeRankRecord implements Comparable<NodeRankRecord> {
	private String playerId;
	private long nodeId;
	private int progress;
	private long time = System.currentTimeMillis();
	private String playerName;
	private int playerSex;
	private String playerImg;
	private transient long score;

	public void calculate() {
		score = progress << 41 | (Long.MAX_VALUE / MathUtil.twoPow22 - time);
	}

	public void setProgress(int progress) {
		this.progress = progress;
		calculate();
	}

	@Override
	public int compareTo(NodeRankRecord o) {
		if (score > o.score) {
			return -1;
		}else if (score == o.score) {
			return 0;
		}
		return 1;
	}
}
