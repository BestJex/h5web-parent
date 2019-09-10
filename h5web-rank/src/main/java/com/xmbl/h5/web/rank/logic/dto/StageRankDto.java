package com.xmbl.h5.web.rank.logic.dto;

import java.util.List;

import com.xmbl.h5.web.rank.logic.entity.StageRankRecord;

import lombok.Data;

@Data
public class StageRankDto {
	private int rank = -1;
	private int total;
	private List<StageRankRecord> records;
	private StageRankRecord myRecord;
}
