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
import com.xmbl.h5.web.rank.logic.dto.NodeRankDto;
import com.xmbl.h5.web.rank.logic.entity.NodeRankRecord;

@Component
public class NodeRankService {
	private static final Logger log = LoggerFactory.getLogger(NodeRankService.class);

	@Autowired
	private RankDao rankDao;
	private final Map<Long, NodeRank> ranks = new ConcurrentHashMap<>(100);
	@Autowired
	private RankApplicationContext context;

	public void addRecord(NodeRankRecord record) {
		if (Objects.isNull(record)) {
			return;
		}
		Long nodeId = record.getNodeId();
		NodeRank rank = null;
		if (!ranks.containsKey(nodeId)) {
			rank = context.getBean(NodeRank.class, nodeId);
			rank.setNodeId(nodeId);
			NodeRank temp = ranks.putIfAbsent(nodeId, rank);
			if (Objects.isNull(temp)) {
				rankDao.addNodeIdToSet(nodeId);
				log.info("create new nodeRank, nodeId:{}", nodeId);
			}
		} else {
			rank = ranks.get(nodeId);
		}
		if (Objects.nonNull(rank)) {
			rank.addRecord(record);
		}
	}

	public NodeRankDto rankRecords(long nodeId, int start, int end, String playerId) {
		NodeRankDto dto = new NodeRankDto();
		NodeRank rank = ranks.get(nodeId);
		if (Objects.isNull(rank)) {
			dto.setRecords(Collections.emptyList());
			return dto;
		}
		List<NodeRankRecord> records = rank.getRecords(start, end);
		dto.setRecords(records);
		int r = rank.getRank(playerId);
		dto.setRank(r);
		NodeRankRecord myRecord = rank.getRecord(playerId);
		dto.setMyRecord(myRecord);
		int total = rank.getTotal();
		dto.setTotal(total);
		return dto;
	}

	public int getPlayerRank(long nodeId, String playerId) {
		NodeRank rank = ranks.get(nodeId);
		if (Objects.isNull(rank)) {
			return -1;
		}
		return rank.getRank(playerId);
	}

	public NodeRankRecord getPlayerRecord(long nodeId, String playerId) {
		NodeRank rank = ranks.get(nodeId);
		if (Objects.isNull(rank)) {
			return null;
		}
		return rank.getRecord(playerId);
	}

	@PostConstruct
	public void init() {
		List<Long> nodeIds = rankDao.getNodeRankCollectionList();
		if (Objects.nonNull(nodeIds) && !nodeIds.isEmpty()) {
			Set<Long> temp = new HashSet<>();
			temp.addAll(nodeIds);
			for (Long treeId : temp) {
				if (!ranks.containsKey(treeId)) {
					NodeRank treeRank = context.getBean(NodeRank.class, treeId);
					ranks.putIfAbsent(treeId, treeRank);
				}
			}
		}
	}
}
