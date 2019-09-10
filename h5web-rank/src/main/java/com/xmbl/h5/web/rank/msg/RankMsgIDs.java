package com.xmbl.h5.web.rank.msg;

import com.xmbl.h5.web.common.msg.AMsg;
import com.xmbl.h5.web.common.msg.AMsgProcessor;
import com.xmbl.h5.web.common.proto.RankMsg;
import com.xmbl.h5.web.rank.msg.processor.G2RNodeRankNoticeProcessor;
import com.xmbl.h5.web.rank.msg.processor.G2RStageRankNoticeProcessor;
import com.xmbl.h5.web.rank.msg.processor.G2RTreeRankNoticeProcessor;

public class RankMsgIDs {

	@AMsg(message = RankMsg.G2RTreeRankNotice.class)
	@AMsgProcessor(processor = G2RTreeRankNoticeProcessor.class)
	public static final int G2R_tree_rank_notice = 2001;
	
	@AMsg(message = RankMsg.G2RNodeRankNotice.class)
	@AMsgProcessor(processor = G2RNodeRankNoticeProcessor.class)
	public static final int G2R__node_rank_notice = 2002;
	
	@AMsg(message = RankMsg.G2RStageRankNotice.class)
	@AMsgProcessor(processor = G2RStageRankNoticeProcessor.class)
	public static final int G2R_stage_rank_notice = 2003;
	
	@AMsg(message = RankMsg.G2RQeuryRankReq.class)
	public static final int G2R_qeury_rank_req = 2010;
	
	@AMsg(message = RankMsg.R2GQeuryRankResp.class)
	public static final int R2G_qeury_rank_resp = 2011;

}
