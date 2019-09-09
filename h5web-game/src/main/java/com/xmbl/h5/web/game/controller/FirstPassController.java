package com.xmbl.h5.web.game.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.xmbl.h5.web.common.consts.EMsgCode;
import com.xmbl.h5.web.common.dto.ResponseResult;
import com.xmbl.h5.web.common.logic.AbstractController;
import com.xmbl.h5.web.game.dto.FirstPassDto;
import com.xmbl.h5.web.game.entity.FirstPassInfo;
import com.xmbl.h5.web.game.service.StageTreeService;
import com.xmbl.h5.web.util.DateUtils;

@RestController
@RequestMapping("/game/firstPass")
@CrossOrigin(origins = "*")
public class FirstPassController extends AbstractController {
	
	@Autowired
	private StageTreeService stageTreeService;

	@RequestMapping("/getFirstPassInfo")
	public ResponseResult getFirstPassInfo(@RequestParam(name = "stageId", required = false) Long stageId) {
		FirstPassInfo info = stageTreeService.getFirstPassInfo(stageId);
		if (Objects.isNull(info)) {
			return errorJson(EMsgCode.no_player_in_rank);
		}
		FirstPassDto dto = info.transfer2Dto();
		return successJson(EMsgCode.query_success, dto);
	}
	
	public static void main(String[] args) {
		FirstPassDto dto = new FirstPassDto();
		
		dto.setPlayerId("playerId");
		dto.setCreateDate(DateUtils.formatDate(System.currentTimeMillis()));
		System.out.println(JSON.toJSONString(dto));
	}

}
