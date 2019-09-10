package com.xmbl.h5.web.rank.logic;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xmbl.h5.web.rank.RankApplicationContext;
import com.xmbl.h5.web.rank.dao.RankDao;
import com.xmbl.h5.web.rank.logic.dto.StageRankDto;
import com.xmbl.h5.web.rank.logic.entity.StageRankRecord;

@Component
public class StageRankService {
	private static final Logger log = LoggerFactory.getLogger(StageRankService.class);

	@Autowired
	private RankDao rankDao;
	private final Map<Long, StageRank> ranks = new ConcurrentHashMap<>(100);
	@Autowired
	private RankApplicationContext context;

	public void addRecord(StageRankRecord record) {
		if (Objects.isNull(record)) {
			return;
		}
		Long stageId = record.getStageId();
		StageRank rank = null;
		if (!ranks.containsKey(stageId)) {
			rank = context.getBean(StageRank.class, stageId);
			rank.setStageId(stageId);
			StageRank temp = ranks.putIfAbsent(stageId, rank);
			if (Objects.isNull(temp)) {
				rankDao.addStageIdToSet(stageId);
				log.info("create new stageRank, stageId:{}", stageId);
			}
		} else {
			rank = ranks.get(stageId);
		}
		if (Objects.nonNull(rank)) {
			rank.addRecord(record);
		}
	}

	public StageRankDto rankRecords(long nodeId, int start, int end, String playerId) {
		StageRankDto dto = new StageRankDto();
		StageRank rank = ranks.get(nodeId);
		if (Objects.isNull(rank)) {
			dto.setRecords(Collections.emptyList());
			return dto;
		}
		List<StageRankRecord> records = rank.getRecords(start, end);
		dto.setRecords(records);
		int r = rank.getRank(playerId);
		dto.setRank(r);
		
		StageRankRecord myRecord = rank.getRecord(playerId);
		dto.setMyRecord(myRecord);
		int total = rank.getTotal();
		dto.setTotal(total);
		return dto;
	}

	public int getPlayerRank(long nodeId, String playerId) {
		StageRank rank = ranks.get(nodeId);
		if (Objects.isNull(rank)) {
			return -1;
		}
		return rank.getRank(playerId);
	}

	public StageRankRecord getPlayerRecord(long nodeId, String playerId) {
		StageRank rank = ranks.get(nodeId);
		if (Objects.isNull(rank)) {
			return null;
		}
		return rank.getRecord(playerId);
	}

	@PostConstruct
	public void init() {
		List<Long> nodeIds = rankDao.getStageRankCollectionList();
		if (Objects.nonNull(nodeIds) && !nodeIds.isEmpty()) {
			Set<Long> temp = new HashSet<>();
			temp.addAll(nodeIds);
			for (Long treeId : temp) {
				if (!ranks.containsKey(treeId)) {
					StageRank treeRank = context.getBean(StageRank.class, treeId);
					ranks.putIfAbsent(treeId, treeRank);
				}
			}
		}
	}
}
