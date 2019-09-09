package com.xmbl.h5.web.game.msg;

import com.xmbl.h5.web.common.msg.AMsg;
import com.xmbl.h5.web.common.proto.RankMsg;

public class GameMsgIDs {
	
	@AMsg(message = RankMsg.G2RQeuryRankReq.class)
	public static final int G2R_qeury_rank_req = 2010;
	
	@AMsg(message = RankMsg.R2GQeuryRankResp.class)
	public static final int R2G_qeury_rank_resp = 2011;

}
