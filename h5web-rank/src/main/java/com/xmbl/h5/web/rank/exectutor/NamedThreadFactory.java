package com.xmbl.h5.web.rank.exectutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
	private String prefix;
	private AtomicInteger seq = new AtomicInteger(0);

	public NamedThreadFactory(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName(prefix + seq.incrementAndGet());
		return thread;
	}
}
