package com.xmbl.h5.web.common.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmbl.h5.web.common.consts.EMsgCode;
import com.xmbl.h5.web.common.consts.EnumResCode;
import com.xmbl.h5.web.common.dto.ResponseResult;

public abstract class AbstractController {

private static final Logger log = LoggerFactory.getLogger(AbstractController.class);

	protected ResponseResult successJson(EMsgCode msgCode, Object data) {
		ResponseResult result = new ResponseResult();
		result.setStatus(EnumResCode.SUCCESSFUL.value());
		result.setMsgCode(msgCode.code);
		result.setResult(data);
		return result;
	}

	protected ResponseResult successJson(Object data) {
		ResponseResult result = new ResponseResult();
		result.setStatus(EnumResCode.SUCCESSFUL.value());
		result.setMsgCode(EMsgCode.success.code);
		result.setResult(data);
		return result;
	}
	
	public static ResponseResult errorJson(EMsgCode msgCode) {
		log.info(msgCode.toString());
		ResponseResult result = new ResponseResult();
		result.setStatus(EnumResCode.SERVER_ERROR.value());
		result.setMsgCode(msgCode.code);
		return result;
	}
}
