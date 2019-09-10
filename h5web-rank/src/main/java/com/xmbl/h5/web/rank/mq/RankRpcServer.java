package com.xmbl.h5.web.rank.mq;

import java.nio.ByteBuffer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.Message;
import com.xmbl.h5.web.common.consts.RabbitConst;
import com.xmbl.h5.web.common.logic.ERankType;
import com.xmbl.h5.web.common.proto.Options;
import com.xmbl.h5.web.common.proto.RankMsg.G2RQeuryRankReq;
import com.xmbl.h5.web.common.proto.RankMsg.R2GQeuryRankResp;
import com.xmbl.h5.web.rank.logic.NodeRankService;
import com.xmbl.h5.web.rank.logic.StageRankService;
import com.xmbl.h5.web.rank.logic.TreeRankService;
import com.xmbl.h5.web.rank.msg.RankMsgFactory;
import com.xmbl.h5.web.rank.msg.RankMsgIDs;

public class RankRpcServer {
	@Autowired
	private RankMsgFactory msgFactory;
	@Autowired
	private TreeRankService treeRankService;
	@Autowired
	private NodeRankService nodeRankService;
	@Autowired
	StageRankService stageRankService;

	@RabbitListener(queues = RabbitConst.h5_web_rank_query_rpc_queue)
	public byte[] query(byte[] data) {
		Message req = parse(data);
		Message resp = process(req);
		return assemble(resp);
	}
	
	private Message parse(byte[] data) {
		ByteBuffer buffer = ByteBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		int msgId = buffer.getInt();
		byte[] dst = new byte[data.length-Integer.BYTES];
		buffer.get(dst);
		Message message = msgFactory.parseMessage(msgId, dst);
		return message;
	}

	private Message process(Message message) {
		int msgId = message.getDescriptorForType().getOptions().getExtension(Options.messageId);
		switch (msgId) {
		case RankMsgIDs.G2R_qeury_rank_req:
			G2RQeuryRankReq req = (G2RQeuryRankReq) message;
			long id = req.getId();
			int type = req.getType();
			String playerId = req.getPlayerId();
			
			R2GQeuryRankResp.Builder resp = R2GQeuryRankResp.newBuilder();
			ERankType rankType = ERankType.getRankType(type);
			switch (rankType) {
			case TREE:
				resp.setRank(treeRankService.getPlayerRank(id, playerId));
				break;
			case NODE:
				resp.setRank(nodeRankService.getPlayerRank(id, playerId));
				break;
			case STAGE:
				resp.setRank(stageRankService.getPlayerRank(id, playerId));
				break;
			case ERROR:
				resp.setRank(-1);
				break;
			}
			return resp.build();
		default:
			return null;
		}
	}

	private byte[] assemble(Message message) {
		int messageId = message.getDescriptorForType().getOptions().getExtension(Options.messageId);
		byte[] bytes = message.toByteArray();
		int length = Integer.BYTES + bytes.length;
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.putInt(messageId);
		buffer.put(bytes);
		buffer.flip();
		byte[] data = new byte[length];
		buffer.get(data);
		return data;
	}
}