package com.xmbl.h5.web.rank.logic.entity;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class RankCollectionList {
	@Id
	private String id;
	private List<Long> treeIds;
	private List<Long> nodeIds;
	private List<Long> stageIds;
}
