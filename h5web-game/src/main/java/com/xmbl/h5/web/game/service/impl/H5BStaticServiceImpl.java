package com.xmbl.h5.web.game.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmbl.h5.web.game.configure.H5Relad;
import com.xmbl.h5.web.game.consts.EnumBrowser;
import com.xmbl.h5.web.game.dao.H5BStaticDao;
import com.xmbl.h5.web.game.entity.H5BStatic;
import com.xmbl.h5.web.game.service.H5BStaticService;
import com.xmbl.h5.web.util.PhoneUtil;

@Service
public class H5BStaticServiceImpl implements H5BStaticService {
	private static Logger logger = LoggerFactory.getLogger(H5BStaticServiceImpl.class);
	private static final String statisticTemplate = "微信客户端访问量：wx，QQ浏览器访问量：qq，其它平台访问量：other，总访问量：total";
	private static final int phoneTemplateId = 371342;
	
	@Autowired
	private H5BStaticDao h5BStaticDao;
	
	@Override
	public void addH5BStatic(H5BStatic h5bStatic) {
		h5BStaticDao.addH5BStatic(h5bStatic);
	}
	
	@Override
	public void statistic() {
		logger.info("开始执行浏览器访问量统计");
		h5BStaticDao.statistic();
		logger.info("执行浏览器访问量统计定时任务完成");
	}

	@Override
	public void sendStatisticInfo() {
		String mobileString = H5Relad.getInstance().getProperty("h5b.statistic.receivers");
		if (StringUtils.isEmpty(mobileString)) {
			return;
		}
		
		List<H5BStatic> h5bStatics = h5BStaticDao.getH5BStatic();
		long wxBit = 0, qqBit = 0, otherBit = 0;
		double total = 0d, total2 = 0d;
		for (H5BStatic h5bStatic : h5bStatics) {
			String type = h5bStatic.getType();
			if (EnumBrowser.wx.name().equals(type)) {
				wxBit = h5bStatic.getHit2();
				total += wxBit;
			}else if (EnumBrowser.qq.name().equals(type)) {
				qqBit = h5bStatic.getHit2();
				total += qqBit;
			}else if (EnumBrowser.other.name().equals(type)) {
				otherBit = h5bStatic.getHit2();
				total += otherBit;
			}
		}
		total2 = total;
		if (total == 0) {
			total = 1;
		}

		String content = statisticTemplate
				.replaceFirst("wx", ""+wxBit+"("+Math.round(wxBit/total*100)+"%)")
				.replaceFirst("qq", ""+qqBit+"("+Math.round(qqBit/total*100)+"%)")
				.replaceFirst("other", ""+otherBit+"("+Math.round(otherBit/total*100)+"%)")
				.replaceFirst("total", String.valueOf((long)(total2)));
		
		System.out.println(content);
		
		List<String> params = new ArrayList<>();
		params.add("<方块创造>");
		params.add(content);
		
		try {
			List<String> mobiles = Arrays.asList(mobileString.split(","));
			PhoneUtil.sendText(mobiles, phoneTemplateId, params.toArray(new String[0]));
			logger.info("执行发送浏览器访问量信息定时任务成功");
		} catch (Exception e) {
			logger.error("执行发送浏览器访问量信息定时任务发生异常", e);
		}
	}
}
