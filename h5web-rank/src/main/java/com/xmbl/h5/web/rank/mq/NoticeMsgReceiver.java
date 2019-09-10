package com.xmbl.h5.web.rank.mq;

import java.nio.ByteBuffer;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.Message;
import com.xmbl.h5.web.common.consts.RabbitConst;
import com.xmbl.h5.web.rank.msg.RankMsgFactory;

@RabbitListener(queues = RabbitConst.h5_web_rank_notice_queue)
public class NoticeMsgReceiver {
	private static final Logger log = LoggerFactory.getLogger(NoticeMsgReceiver.class);
	@Autowired
	private RankMsgFactory msgFactory;
	
	@RabbitHandler
	public void receive(byte[] data) {
		ByteBuffer buffer = ByteBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		int msgId = buffer.getInt();
		byte[] dst = new byte[data.length-Integer.BYTES];
		buffer.get(dst);
		Message message = msgFactory.parseMessage(msgId, dst);
		if (Objects.isNull(message)) {
			log.info("消息{}解析结果为null", msgId);
			return;
		}
		msgFactory.processor(message, msgId, null);
	}
}
