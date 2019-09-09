package com.xmbl.h5.web.game.service;

import com.xmbl.h5.web.game.entity.H5BStatic;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  H5BStaticService 
 * @创建时间:  2018年11月1日 下午5:32:33
 * @修改时间:  2018年11月1日 下午5:32:33
 * @类说明:
 */
public interface H5BStaticService {

	void addH5BStatic(H5BStatic h5bStatic);

	void sendStatisticInfo();
	
	void statistic();

}
