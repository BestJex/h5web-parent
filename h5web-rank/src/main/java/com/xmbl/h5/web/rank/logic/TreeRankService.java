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
import com.xmbl.h5.web.rank.logic.dto.TreeRankDto;
import com.xmbl.h5.web.rank.logic.entity.TreeRankRecord;

@Component("treeRankService")
public class TreeRankService {
	private static final Logger log = LoggerFactory.getLogger(TreeRankService.class);

	@Autowired private RankDao rankDao;
	private final Map<Long, TreeRank> treeRanks = new ConcurrentHashMap<>(100);
	@Autowired
	private RankApplicationContext context;
	public void addRecord(TreeRankRecord record) {
		if (Objects.isNull(record)) {
			return;
		}
		Long treeId = record.getTreeId();
		TreeRank treeRank = null;
		if (!treeRanks.containsKey(treeId)) {
			treeRank = context.getBean(TreeRank.class, treeId);
			treeRank.setTreeId(treeId);
			TreeRank temp = treeRanks.putIfAbsent(treeId, treeRank);
			if (Objects.isNull(temp)) {
				rankDao.addTreeIdToSet(treeId);
				log.info("create new treeRank, treeId:{}", treeId);
			}
		}else {
			treeRank = treeRanks.get(treeId);
		}
		if (Objects.nonNull(treeRank)) {
			treeRank.addRecord(record);
		}
	}

	public TreeRankDto rankRecords(long treeId, int start, int end, String playerId) {
		TreeRankDto dto = new TreeRankDto();
		TreeRank treeRank = treeRanks.get(treeId);
		if (Objects.isNull(treeRank)) {
			dto.setRecords(Collections.emptyList());
			return dto;
		}
		List<TreeRankRecord> records = treeRank.getRecords(start, end);
		dto.setRecords(records);
		int rank = treeRank.getRank(playerId);
		int total = treeRank.getTotal();
		dto.setTotal(total);
		dto.setRank(rank);
		TreeRankRecord myRecord = treeRank.getRecord(playerId);
		dto.setMyRecord(myRecord);
		return dto;
	}

	public int getPlayerRank(long treeId, String playerId) {
		TreeRank treeRank = treeRanks.get(treeId);
		if (Objects.isNull(treeRank)) {
			return -1;
		}
		return treeRank.getRank(playerId);
	}

	public TreeRankRecord getPlayerRecord(long treeId, String playerId) {
		TreeRank treeRank = treeRanks.get(treeId);
		if (Objects.isNull(treeRank)) {
			return null;
		}
		return treeRank.getRecord(playerId);
	}

	@PostConstruct
	public void init() {
		List<Long> treeIds = rankDao.getTreeRankCollectionList();
		if (Objects.nonNull(treeIds) && !treeIds.isEmpty()) {
			Set<Long> temp = new HashSet<>();
			temp.addAll(treeIds);
			for (Long treeId : temp) {
				if (!treeRanks.containsKey(treeId)) {
					TreeRank treeRank = context.getBean(TreeRank.class, treeId);
					treeRanks.putIfAbsent(treeId, treeRank);
				}
			}
		}
	}
}
