package com.xmbl.h5.web.game.mq;

import java.nio.ByteBuffer;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.protobuf.Message;
import com.xmbl.h5.web.common.consts.RabbitConst;
import com.xmbl.h5.web.common.proto.Options;

@Component
public class G2RTreeRankSender {
	@Autowired
	private RabbitTemplate template;

	@Autowired
	@Qualifier(RabbitConst.h5_web_rank_notice_queue)
	private Queue noticeQueue;

	public void send(Message message) {
		int messageId = message.getDescriptorForType().getOptions().getExtension(Options.messageId);
		byte[] bytes = message.toByteArray();
		int length = Integer.BYTES + bytes.length;
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.putInt(messageId);
		buffer.put(bytes);
		buffer.flip();
		byte[] data = new byte[length];
		buffer.get(data);
		template.convertAndSend(noticeQueue.getName(), data);
		
	}
}