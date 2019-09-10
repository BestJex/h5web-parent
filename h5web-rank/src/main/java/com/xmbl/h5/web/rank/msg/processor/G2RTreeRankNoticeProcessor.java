package com.xmbl.h5.web.rank.msg.processor;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.Message;
import com.xmbl.h5.web.common.msg.IMsgProcessor;
import com.xmbl.h5.web.common.proto.RankMsg;
import com.xmbl.h5.web.rank.logic.TreeRankService;
import com.xmbl.h5.web.rank.logic.entity.TreeRankRecord;

@Component
public class G2RTreeRankNoticeProcessor implements IMsgProcessor<Object> {
	private static final Logger log = LoggerFactory.getLogger(G2RTreeRankNoticeProcessor.class);
	@Autowired
	private TreeRankService treeRankService;// = SpringApplicationContext.getBean("treeRankService", TreeRankService.class);

	@Override
	public void processor(Message message, Object ctx) {
		log.debug("收到关卡集进度变化通知， mgs:{}", message);
		RankMsg.G2RTreeRankNotice notice = (RankMsg.G2RTreeRankNotice) message;
		if (Objects.isNull(treeRankService)) {
			log.error("treeRankService is null");
			return;
		}
		
		TreeRankRecord record = new TreeRankRecord();
		record.setPlayerId(notice.getPlayerId());
		record.setProgress(notice.getProgress());
		record.setTreeId(notice.getTreeId());
		record.setPlayerImg(notice.getPlayerImg());
		record.setPlayerName(notice.getPlayerName());
		record.setPlayerSex(notice.getPlayerSex());
		treeRankService.addRecord(record);
	}
}
