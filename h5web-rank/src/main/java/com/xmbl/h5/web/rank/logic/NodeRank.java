package com.xmbl.h5.web.rank.logic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.xmbl.h5.web.rank.consts.RankConsts;
import com.xmbl.h5.web.rank.dao.RankDao;
import com.xmbl.h5.web.rank.exectutor.ExectutorService;
import com.xmbl.h5.web.rank.logic.entity.NodeRankRecord;

import lombok.Getter;
import lombok.Setter;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NodeRank {
	@Setter
	@Getter
	private long nodeId = 0;

	public NodeRank(long nodeId) {
		this.nodeId = nodeId;
	}

	private static final Logger log = LoggerFactory.getLogger(NodeRank.class);
	private final int size = RankConsts.node_rank_size;
	private final Map<String, NodeRankRecord> p2rMap = new ConcurrentHashMap<>(100);
	private final ConcurrentSkipListSet<NodeRankRecord> records = new ConcurrentSkipListSet<>();

	@Autowired
	private RankDao rankDao;
	@Autowired
	private ExectutorService exectutorService;

	public synchronized void addRecord(NodeRankRecord record) {
		if (Objects.isNull(record)) {
			return;
		}
		if (!p2rMap.containsKey(record.getPlayerId())) {
			p2rMap.putIfAbsent(record.getPlayerId(), record);
			records.add(record);
			exectutorService.addDBTask(new AddTask(record));
			log.info("add NodeRankRecord:{}", record);
			while (records.size() > size) {
				NodeRankRecord temp = records.pollLast();
				exectutorService.addDBTask(new RemTask(temp));
			}
		} else {
			NodeRankRecord old = p2rMap.get(record.getPlayerId());
			if (record.getProgress() > old.getProgress()) {
				if (records.remove(old)) {
					records.add(record);
					exectutorService.addDBTask(new UpdateTask(record));
					p2rMap.put(record.getPlayerId(), record);
					log.info("update NodeRankRecord:old:{}, new{}", old, record);
				}
			}
		}
	}

	public synchronized List<NodeRankRecord> getRecords(int start, int end) {
		NodeRankRecord[] rankRecords = records.toArray(new NodeRankRecord[0]);
		if (Objects.isNull(rankRecords) || rankRecords.length < 1) {
			return Collections.emptyList();
		}
		
		if (start > rankRecords.length) {
			return Collections.emptyList();
		}
		
		end = end > rankRecords.length ? rankRecords.length : end;
		NodeRankRecord[] result = Arrays.copyOfRange(rankRecords, start, end);
		return Arrays.asList(result);
	}
	
	public NodeRankRecord getRecord(String playerId) {
		return p2rMap.get(playerId);
	}

	public int getRank(String playerId) {
		if (StringUtils.isBlank(playerId)) {
			return -1;
		}
		if (!p2rMap.containsKey(playerId)) {
			return -1;
		}
		NodeRankRecord old = p2rMap.get(playerId);
		if (Objects.isNull(old)) {
			return -1;
		}
		return records.headSet(old, true).size();
	}
	
	public int getTotal() {
		return records.size();
	}

	public class AddTask implements Runnable {
		private NodeRankRecord r;

		public AddTask(NodeRankRecord record) {
			r = record;
		}

		@Override
		public void run() {
			rankDao.addNodeRank(r);
		}
	}

	public class RemTask implements Runnable {
		private NodeRankRecord r;

		public RemTask(NodeRankRecord record) {
			r = record;
		}

		@Override
		public void run() {
			rankDao.remNodeRank(r);
		}
	}

	public class UpdateTask implements Runnable {
		private NodeRankRecord r;

		public UpdateTask(NodeRankRecord record) {
			r = record;
		}

		@Override
		public void run() {
			rankDao.updateNodeRank(r);
		}
	}

	@PostConstruct
	public synchronized void init() {
		if (rankDao.isNodeRankExists(nodeId)) {
			List<NodeRankRecord> rankRecords = rankDao.getNodeRankRecords(nodeId);
			if (Objects.nonNull(rankRecords) && !rankRecords.isEmpty()) {
				records.addAll(rankRecords);
				rankRecords.forEach(rankRecord -> {
					p2rMap.put(rankRecord.getPlayerId(), rankRecord);
				});
			}
		}
	}
}
