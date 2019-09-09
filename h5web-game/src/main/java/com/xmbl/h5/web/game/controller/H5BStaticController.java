package com.xmbl.h5.web.game.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmbl.h5.web.common.consts.EMsgCode;
import com.xmbl.h5.web.common.dto.ResponseResult;
import com.xmbl.h5.web.common.logic.AbstractController;
import com.xmbl.h5.web.game.entity.H5BStatic;
import com.xmbl.h5.web.game.service.H5BStaticService;

@RestController
@RequestMapping(value = "/h5BStatic")
@CrossOrigin(origins = "*", maxAge = 3600)
public class H5BStaticController extends AbstractController {

	@Autowired
	private H5BStaticService h5BStaticService;

	@PostMapping(value = "/clickOne")
	public ResponseResult clickOne(@RequestParam(value = "type", required = false) String type) {
		if (StringUtils.isNotEmpty(type)) {
			H5BStatic h5BStatic = new H5BStatic();
			h5BStatic.setType(type);
			h5BStaticService.addH5BStatic(h5BStatic);
		}
		return successJson(EMsgCode.success, "");
	}
}
