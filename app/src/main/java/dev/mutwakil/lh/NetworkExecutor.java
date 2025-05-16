package dev.mutwakil.lh;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkExecutor {
	
	private static ExecutorService executor = Executors.newFixedThreadPool(4);
	private static final Object lock = new Object();
	
	 
	public static void execute(Runnable task) {
			if (executor.isShutdown() || executor.isTerminated()) {
				executor = Executors.newFixedThreadPool(4);
			}
			executor.execute(task);
	}
	
	 
	public static void shutdown() {
			if (!executor.isShutdown()) {
				executor.shutdown();
			}
	}
	
	
	public static void restart() {
			if (!executor.isShutdown()) {
				executor.shutdownNow();
			}
			executor = Executors.newFixedThreadPool(4);
	}
}