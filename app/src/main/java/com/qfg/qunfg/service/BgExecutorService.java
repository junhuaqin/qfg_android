package com.qfg.qunfg.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by rbtq on 8/19/16.
 */
public class BgExecutorService {
    private ExecutorService executorService;
    private final static BgExecutorService instance = new BgExecutorService();

    private BgExecutorService() {
        executorService = Executors.newFixedThreadPool(1);
    }

    public static BgExecutorService getInstance() {
        return instance;
    }

    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }

    public Future<?> submit(Runnable task) {
        return executorService.submit(task);
    }
}
