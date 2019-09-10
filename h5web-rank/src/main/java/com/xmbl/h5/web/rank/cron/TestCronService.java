package com.xmbl.h5.web.rank.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.xmbl.h5.web.rank.dao.RankDao;
import com.xmbl.h5.web.rank.logic.TreeRankService;

@Component
@Profile({ "dev" })
public class TestCronService {
	static final Logger log = LoggerFactory.getLogger(TestCronService.class);

	@Autowired
	RankDao rankDao;

	@Autowired
	TreeRankService treeRankService;

	public void evetyMinuteTask() throws Exception {

	}
}
