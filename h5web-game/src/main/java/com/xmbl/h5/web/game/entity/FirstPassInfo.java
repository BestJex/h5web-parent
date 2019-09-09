package com.xmbl.h5.web.game.entity;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xmbl.h5.web.game.consts.MongoDBConst;
import com.xmbl.h5.web.game.dto.FirstPassDto;
import com.xmbl.h5.web.util.DateUtils;

import lombok.Data;

@Data
@Document(collection = MongoDBConst.firstPassInfo)
public class FirstPassInfo {
	@Indexed(unique = true)
	private long stageId;
	private String playerId = "";
	private String playerImage = "";
	private String playerName = "";
	private int playerSex;
	private long createTime;
	
	public FirstPassDto transfer2Dto(){
		FirstPassDto dto = new FirstPassDto();
		BeanUtils.copyProperties(this, dto);
		String temp = DateUtils.formatDate(createTime);
		dto.setCreateDate(temp);
		return dto;
	}
}
