package com.xmbl.h5.web.common.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author  noseparte
 * @date  2019/9/9 11:28
 * @Description
 * 		<p>服务器响应对象</p>
 */
@Data
@ToString
public class ResponseResult {
	private int status;
	private String msg = "";
	private int msgCode;
	private Object result;
	
	@Data
	public static class ShareResult {
		int energy;
	}
}

