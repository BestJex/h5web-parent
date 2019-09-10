package com.xmbl.h5.web.rank.exectutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExectutorService {
	
	private static final Logger log = LoggerFactory.getLogger(ExectutorService.class);

	private ExecutorService treeNoticeExectutor;
	private ExecutorService nodeNoticeExectutor;
	private ExecutorService stageNoticeExectutor;
	private ExecutorService dbExectutor;

	@PostConstruct
	public void init() {
		treeNoticeExectutor = Executors.newSingleThreadExecutor(new NamedThreadFactory("treeNoticeExectutor_"));
		nodeNoticeExectutor = Executors.newSingleThreadExecutor(new NamedThreadFactory("nodeNoticeExectutor_"));
		stageNoticeExectutor = Executors.newSingleThreadExecutor(new NamedThreadFactory("stageNoticeExectutor_"));
		dbExectutor = Executors.newCachedThreadPool(new NamedThreadFactory("exectutorService_"));
		log.info("ExectutorService inited");
	}

	@PreDestroy
	public void destory() {
		treeNoticeExectutor.shutdown();
		nodeNoticeExectutor.shutdown();
		stageNoticeExectutor.shutdown();
		dbExectutor.shutdown();
		log.info("ExectutorService destory");
	}

	public void exeTreeNotice(Runnable runnable) {
		treeNoticeExectutor.execute(runnable);

	}

	public void exeNodeNotice(Runnable runnable) {
		nodeNoticeExectutor.execute(runnable);
	}

	public void exeStageNotice(Runnable runnable) {
		stageNoticeExectutor.execute(runnable);
	}
	
	public void addDBTask(Runnable runnable) {
		dbExectutor.execute(runnable);
	}
}
