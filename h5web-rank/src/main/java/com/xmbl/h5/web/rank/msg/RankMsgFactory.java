package com.xmbl.h5.web.rank.msg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xmbl.h5.web.common.msg.AbstractMsgFactory;
import com.xmbl.h5.web.rank.RankApplicationContext;

@Component
public class RankMsgFactory extends AbstractMsgFactory {
	@Autowired
	private RankApplicationContext c;
	
	@Override
	@PostConstruct
	public void init() {
		setContext();
		init(RankMsgIDs.class);
	}

	@Override
	protected void setContext() {
		context = c;
	}
}
