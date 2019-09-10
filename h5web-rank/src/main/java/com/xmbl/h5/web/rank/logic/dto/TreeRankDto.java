package com.xmbl.h5.web.rank.logic.dto;

import java.util.List;

import com.xmbl.h5.web.rank.logic.entity.TreeRankRecord;

import lombok.Data;

@Data
public class TreeRankDto {
	private int rank = -1;
	private int total;
	private List<TreeRankRecord> records;
	
	private TreeRankRecord myRecord;
}
