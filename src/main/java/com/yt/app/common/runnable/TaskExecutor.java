package com.yt.app.common.runnable;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskExecutor {

	public static ExecutorService tastpool;

	// 线程池
	public static ExecutorService newThreadPool() {
		return tastpool = new ThreadPoolExecutor(10, 15, 20, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
				Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
	}
}
