package com.xmbl.h5.web.game.event.events;

import com.xmbl.h5.web.common.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NodeRankEvent implements Event {
	private String playerId = "";
	private long nodeId;
	private int pass;
	
	private String playerName = "";
	private int playerSex;
	private String playerImg = "";

	@Override
	public boolean isAsync() {
		return true;
	}
}
