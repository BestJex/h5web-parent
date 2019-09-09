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
public class TreeRankEvent implements Event {
	private String playerId;
	private long treeId;
	private int pass;

	private String playerName = "";
	private int playerSex;
	private String playerImg = "";

	@Override
	public String toString() {
		return "TreeRankEvent [playerId=" + playerId + ", treeId=" + treeId + ", pass=" + pass + ", playerName="
				+ playerName + "]";
	}

	@Override
	public boolean isAsync() {
		return false;
	}
}
