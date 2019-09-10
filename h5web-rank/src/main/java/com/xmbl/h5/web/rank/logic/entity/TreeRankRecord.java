package com.xmbl.h5.web.rank.logic.entity;

import com.alibaba.fastjson.JSON;
import com.xmbl.h5.web.util.MathUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TreeRankRecord implements Comparable<TreeRankRecord> {
	private String playerId;
	private String playerName;
	private int playerSex;
	private String playerImg;
	private long treeId;
	private long progress;//单关通关数
	private long time = System.currentTimeMillis();
	private transient long score;

	public void calculate() {
		score = progress << 41 | (Long.MAX_VALUE / MathUtil.twoPow22 - time);
	}

	public void setProgress(long progress) {
		this.progress = progress;
		calculate();
	}

	@Override
	public int compareTo(TreeRankRecord o) {
		if (score > o.score) {
			return -1;
		}else if (score == o.score) {
			return 0;
		}
		return 1;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
