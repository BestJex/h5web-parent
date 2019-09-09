package com.xmbl.h5.web.game.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;
import com.google.protobuf.Message;
import com.xmbl.h5.web.common.event.EventListener;
import com.xmbl.h5.web.common.proto.RankMsg.G2RNodeRankNotice;
import com.xmbl.h5.web.common.proto.RankMsg.G2RStageRankNotice;
import com.xmbl.h5.web.common.proto.RankMsg.G2RTreeRankNotice;
import com.xmbl.h5.web.game.event.events.NodeRankEvent;
import com.xmbl.h5.web.game.event.events.StageRankEvent;
import com.xmbl.h5.web.game.event.events.TreeRankEvent;
import com.xmbl.h5.web.game.mq.G2RTreeRankSender;

@Component
@EventListener
public class RankEventListener {
	private static final Logger log = LoggerFactory.getLogger(RankEventListener.class);
	@Autowired
	private G2RTreeRankSender g2RTreeRankSender;
	@Subscribe
	public void treeRankEvent(TreeRankEvent event) {
		G2RTreeRankNotice.Builder builder = G2RTreeRankNotice.newBuilder();
		builder.setPlayerId(event.getPlayerId());
		builder.setProgress(event.getPass());
		builder.setTreeId(event.getTreeId());
		builder.setPlayerImg(event.getPlayerImg());
		builder.setPlayerSex(event.getPlayerSex());
		builder.setPlayerName(event.getPlayerName());
		
		Message message = builder.build();
		log.info("treeRankEvent:{}", message);
		g2RTreeRankSender.send(message);
	}
	
	@Subscribe
	public void nodeRankEvent(NodeRankEvent event) {
		G2RNodeRankNotice.Builder builder = G2RNodeRankNotice.newBuilder();
		builder.setPlayerId(event.getPlayerId());
		builder.setProgress(event.getPass());
		builder.setNodeId(event.getNodeId());
		builder.setPlayerImg(event.getPlayerImg());
		builder.setPlayerSex(event.getPlayerSex());
		builder.setPlayerName(event.getPlayerName());
		
		Message message = builder.build();
		log.info("nodeRankEvent:{}", message);
		g2RTreeRankSender.send(message);
	}
	
	@Subscribe
	public void stageRankEvent(StageRankEvent event) {
		G2RStageRankNotice.Builder builder = G2RStageRankNotice.newBuilder();
		builder.setPlayerId(event.getPlayerId());
		builder.setPlayerImg(event.getPlayerImg());
		builder.setPlayerSex(event.getPlayerSex());
		builder.setPlayerName(event.getPlayerName());
		
		builder.setConditionLimit(event.getConditionLimit());
		builder.setCostSeconds(event.getCostSeconds());
		builder.setRemBlockNum(event.getRemBlockNum());
		builder.setScore(event.getScore());
		builder.setStageId(event.getStageId());
		builder.setStageType(event.getStageType());
		builder.setStep(event.getUseStepNum());
		
		Message message = builder.build();
		log.info("nodeRankEvent:{}", message);
		g2RTreeRankSender.send(message);
	}
}
