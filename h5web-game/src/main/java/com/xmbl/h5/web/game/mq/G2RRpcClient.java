package com.xmbl.h5.web.game.mq;

import java.nio.ByteBuffer;
import java.util.Objects;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.protobuf.Message;
import com.xmbl.h5.web.common.consts.RabbitConst;
import com.xmbl.h5.web.common.proto.Options;
import com.xmbl.h5.web.game.msg.GameMsgFactory;

@Component
public class G2RRpcClient {
	@Autowired
	private RabbitTemplate template;
	@Autowired
	@Qualifier(RabbitConst.h5_web_rank_query_rpc_exchange)
	private DirectExchange exchange;
	@Autowired
	private GameMsgFactory gameMsgFactory;

	@SuppressWarnings("unchecked")
	public <T extends Message> T send(Message message) {
		int messageId = message.getDescriptorForType().getOptions().getExtension(Options.messageId);
		byte[] bytes = message.toByteArray();
		int length = Integer.BYTES + bytes.length;
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.putInt(messageId);
		buffer.put(bytes);
		buffer.flip();
		byte[] data = new byte[length];
		buffer.get(data);
		byte[] resultData = (byte[]) template.convertSendAndReceive(exchange.getName(), RabbitConst.h5_web_rank_query_rpc_route_key, data);
		ByteBuffer rByteBuffer = ByteBuffer.allocate(resultData.length);
		rByteBuffer.put(resultData);
		rByteBuffer.flip();
		int msgId = rByteBuffer.getInt();
		byte[] rData = new byte[rByteBuffer.remaining()];
		rByteBuffer.get(rData);
		Message msg = gameMsgFactory.parseMessage(msgId, rData);
		if (Objects.isNull(msg)) {
			return null;
		}
		return (T) msg;
	}
}