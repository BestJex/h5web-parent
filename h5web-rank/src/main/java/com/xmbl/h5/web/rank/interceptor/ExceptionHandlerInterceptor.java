package com.xmbl.h5.web.rank.interceptor;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.h5.web.common.consts.EMsgCode;
import com.xmbl.h5.web.common.dto.ResponseResult;
import com.xmbl.h5.web.common.logic.AbstractController;
import com.xmbl.h5.web.rank.log.LoggerProvider;

public class ExceptionHandlerInterceptor implements HandlerInterceptor {
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if (Objects.nonNull(ex)) {
			ResponseResult result = AbstractController.errorJson(EMsgCode.error);
			response.resetBuffer();
			response.setContentType("application/json");
			String json = JSONObject.toJSONString(result);
			byte[] bytes = json.getBytes("utf-8");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			LoggerProvider.addExceptionLog("intercepted request process exception", ex);
		}
	}
}
