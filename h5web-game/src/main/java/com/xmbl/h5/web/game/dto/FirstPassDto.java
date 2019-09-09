package com.xmbl.h5.web.game.dto;

import lombok.Data;

@Data
public class FirstPassDto {
	private long stageId;
	private String playerId = "";
	private String playerImage = "";
	private String playerName = "";
	private int playerSex;
	private String createDate;
}
