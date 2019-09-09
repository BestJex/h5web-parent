package com.xmbl.h5.web.game.event.events;

import com.xmbl.h5.web.common.event.Event;

import lombok.Data;

@Data
public class StageRankEvent implements Event{
	
	private String playerId;
	private String playerName = "";
	private String playerImg = "";
	private int playerSex;
	
	private int conditionLimit;
	private int remBlockNum;
	private int useStepNum;
	private int costSeconds;
	private int score;
	
	private long stageId;
	private int stageType;
	
	@Override
	public boolean isAsync() {
		return true;
	}
}
