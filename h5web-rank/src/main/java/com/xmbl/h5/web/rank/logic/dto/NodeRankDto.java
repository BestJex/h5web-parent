package com.xmbl.h5.web.rank.logic.dto;

import java.util.List;

import com.xmbl.h5.web.rank.logic.entity.NodeRankRecord;

import lombok.Data;

@Data
public class NodeRankDto {
	private int rank = -1;
	private int total;
	private List<NodeRankRecord> records;
	private NodeRankRecord myRecord;

}
