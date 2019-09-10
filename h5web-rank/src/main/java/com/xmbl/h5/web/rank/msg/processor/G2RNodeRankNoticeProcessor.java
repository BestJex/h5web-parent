package com.xmbl.h5.web.rank.msg.processor;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.Message;
import com.xmbl.h5.web.common.msg.IMsgProcessor;
import com.xmbl.h5.web.common.proto.RankMsg;
import com.xmbl.h5.web.rank.logic.NodeRankService;
import com.xmbl.h5.web.rank.logic.entity.NodeRankRecord;

@Component
public class G2RNodeRankNoticeProcessor implements IMsgProcessor<Object> {
	private static final Logger log = LoggerFactory.getLogger(G2RNodeRankNoticeProcessor.class);

	@Autowired
	private NodeRankService nodeRankService;
	@Override
	public void processor(Message message, Object ctx) {
		log.debug("收到关卡包进度变化通知， mgs:{}", message);
		RankMsg.G2RNodeRankNotice notice = (RankMsg.G2RNodeRankNotice) message;
		if (Objects.isNull(nodeRankService)) {
			log.error("nodeRankService is null");
			return;
		}
		
		NodeRankRecord record = new NodeRankRecord();
		record.setPlayerId(notice.getPlayerId());
		record.setProgress(notice.getProgress());
		record.setNodeId(notice.getNodeId());
		record.setPlayerImg(notice.getPlayerImg());
		record.setPlayerName(notice.getPlayerName());
		record.setPlayerSex(notice.getPlayerSex());
		nodeRankService.addRecord(record);
		
	}

}
