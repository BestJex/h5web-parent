package com.xmbl.h5.web.rank.logic.entity;

import lombok.Data;

@Data
public class StageRankRecord implements Comparable<StageRankRecord> {
	private String playerId = "";
	private String playerName = "";
	private String playerImg = "";
	private int playerSex;

	private int conditionLimit;// -1不限制，0步数。1时间
	private int remBlockNum;
	private int useStep;
	private int costSeconds;
	private int score;

	private long stageId;
	private int stageType;// 0消除，1收集

	private long time = System.currentTimeMillis();

	@Override
	public int compareTo(StageRankRecord o) {
		if (stageType == 0) {
			if (remBlockNum != o.remBlockNum) {
				return o.remBlockNum - remBlockNum;
			} else {
				if (conditionLimit == 0) {
					if (useStep != o.useStep) {
						return useStep - o.useStep;
					} else {
						return o.score - score;
					}
				} else {
					if (costSeconds != o.costSeconds) {
						return costSeconds - o.costSeconds;
					} else {
						return o.score - score;
					}
				}
			}
		} else {
			if (remBlockNum != o.remBlockNum) {
				return o.remBlockNum - remBlockNum;
			} else {
				return o.score - score;
			}
		}
	}
}
