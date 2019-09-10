package com.xmbl.h5.web.rank.msg.processor;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.Message;
import com.xmbl.h5.web.common.msg.IMsgProcessor;
import com.xmbl.h5.web.common.proto.RankMsg.G2RStageRankNotice;
import com.xmbl.h5.web.rank.logic.StageRankService;
import com.xmbl.h5.web.rank.logic.entity.StageRankRecord;

@Component
public class G2RStageRankNoticeProcessor implements IMsgProcessor<Object> {
	private static final Logger log = LoggerFactory.getLogger(G2RStageRankNoticeProcessor.class);

	@Autowired
	private StageRankService stageRankService;
	@Override
	public void processor(Message message, Object ctx) {
		log.debug("收到关卡树单关进度变化通知， mgs:{}", message);
		G2RStageRankNotice notice = (G2RStageRankNotice) message;
		
		if (Objects.isNull(stageRankService)) {
			log.error("stageRankService is null");
			return;
		}
		
		StageRankRecord record = new StageRankRecord();
		record.setPlayerId(notice.getPlayerId());
		record.setPlayerImg(notice.getPlayerImg());
		record.setPlayerName(notice.getPlayerName());
		record.setPlayerSex(notice.getPlayerSex());
		
		record.setConditionLimit(notice.getConditionLimit());
		record.setRemBlockNum(notice.getRemBlockNum());
		record.setScore(notice.getScore());
		record.setStageId(notice.getStageId());
		record.setStageType(notice.getStageType());
		record.setCostSeconds(notice.getCostSeconds());
		record.setUseStep(notice.getStep());
		
		
		stageRankService.addRecord(record);
	}
}
