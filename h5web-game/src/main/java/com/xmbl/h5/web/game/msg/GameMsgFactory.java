package com.xmbl.h5.web.game.msg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xmbl.h5.web.common.msg.AbstractMsgFactory;
import com.xmbl.h5.web.game.GameApplicationContext;

@Component
public class GameMsgFactory extends AbstractMsgFactory {
	@Autowired
	private GameApplicationContext c;
	
	@Override
	@PostConstruct
	public void init() {
		setContext();
		init(GameMsgIDs.class);
	}

	@Override
	protected void setContext() {
		context = c;
	}
}
