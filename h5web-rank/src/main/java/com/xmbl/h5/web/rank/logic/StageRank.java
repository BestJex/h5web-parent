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
import com.xmbl.h5.web.rank.logic.entity.StageRankRecord;

import lombok.Getter;
import lombok.Setter;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StageRank {
	@Setter
	@Getter
	private long stageId = 0;

	public StageRank(long stageId) {
		this.stageId = stageId;
	}

	private static final Logger log = LoggerFactory.getLogger(StageRank.class);
	private final int size = RankConsts.stage_rank_size;
	private final Map<String, StageRankRecord> p2rMap = new ConcurrentHashMap<>(100);
	private final ConcurrentSkipListSet<StageRankRecord> records = new ConcurrentSkipListSet<>();

	@Autowired
	private RankDao rankDao;
	@Autowired
	private ExectutorService exectutorService;

	public synchronized void addRecord(StageRankRecord record) {
		if (Objects.isNull(record)) {
			return;
		}
		if (!p2rMap.containsKey(record.getPlayerId())) {
			p2rMap.putIfAbsent(record.getPlayerId(), record);
			records.add(record);
			exectutorService.addDBTask(new AddTask(record));
			log.info("add StageRankRecord:{}", record);
			while (records.size() > size) {
				StageRankRecord temp = records.pollLast();
				exectutorService.addDBTask(new RemTask(temp));
			}
		} else {
			StageRankRecord old = p2rMap.get(record.getPlayerId());
			if (record.compareTo(old) < 0) {
				if (records.remove(old)) {
					records.add(record);
					exectutorService.addDBTask(new UpdateTask(record));
					p2rMap.put(record.getPlayerId(), record);
					log.info("update StageRankRecord: old:{}, new:{}", old, record);
				}
			}
		}
	}

	public synchronized List<StageRankRecord> getRecords(int start, int end) {
		StageRankRecord[] rankRecords = records.toArray(new StageRankRecord[0]);
		if (Objects.isNull(rankRecords) || rankRecords.length < 1) {
			return Collections.emptyList();
		}
		
		if (start > rankRecords.length) {
			return Collections.emptyList();
		}
		
		end = end > rankRecords.length ? rankRecords.length : end;
		StageRankRecord[] result = Arrays.copyOfRange(rankRecords, start, end);
		return Arrays.asList(result);
	}

	public StageRankRecord getRecord(String playerId) {
		return p2rMap.get(playerId);
	}

	public int getRank(String playerId) {
		if (StringUtils.isBlank(playerId)) {
			return -1;
		}
		if (!p2rMap.containsKey(playerId)) {
			return -1;
		}
		StageRankRecord old = p2rMap.get(playerId);
		if (Objects.isNull(old)) {
			return -1;
		}
		return records.headSet(old, true).size();
	}

	public int getTotal() {
		return records.size();
	}

	public class AddTask implements Runnable {
		private StageRankRecord r;

		public AddTask(StageRankRecord record) {
			r = record;
		}

		@Override
		public void run() {
			rankDao.addStageRank(r);
		}
	}

	public class RemTask implements Runnable {
		private StageRankRecord r;

		public RemTask(StageRankRecord record) {
			r = record;
		}

		@Override
		public void run() {
			rankDao.remStageRank(r);
		}
	}

	public class UpdateTask implements Runnable {
		private StageRankRecord r;

		public UpdateTask(StageRankRecord record) {
			r = record;
		}

		@Override
		public void run() {
			rankDao.updateStageRank(r);
		}
	}

	@PostConstruct
	public synchronized void init() {
		if (rankDao.isStageRankExists(stageId)) {
			List<StageRankRecord> rankRecords = rankDao.getStageRankRecords(stageId);
			if (Objects.nonNull(rankRecords) && !rankRecords.isEmpty()) {
				records.addAll(rankRecords);
				rankRecords.forEach(rankRecord -> {
					p2rMap.put(rankRecord.getPlayerId(), rankRecord);
				});
			}
		}
	}

}
