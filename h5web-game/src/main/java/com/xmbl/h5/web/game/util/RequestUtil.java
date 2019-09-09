package com.xmbl.h5.web.game.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class RequestUtil {
	private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

	public static <T> T parseParam(HttpServletRequest request, Class<T> clazz) {
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
		} catch (IOException e) {
			logger.error("解析请求数据失败", e);
		}

		if (Objects.isNull(inputStream)) {
			return null;
		}

		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 2 * 1024);

		ByteBuffer byteBuffer = ByteBuffer.allocate(2 * 1024 * 1024 * 8);
		byte[] temp = new byte[1024];

		int i = -1;
		try {
			while ((i = bufferedInputStream.read(temp)) != -1) {
				byteBuffer.put(temp, 0, i);
			}
		} catch (IOException e) {
			logger.error("读取请求流出错", e);
			return null;
		}
		;

		byteBuffer.flip();
		byte[] dst = new byte[byteBuffer.limit()];
		byteBuffer.get(dst);

		T result = JSONObject.parseObject(dst, clazz);

		return result;
	}

}
