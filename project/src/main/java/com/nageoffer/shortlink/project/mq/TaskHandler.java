package com.nageoffer.shortlink.project.mq;

import org.springframework.stereotype.Component;

import java.util.concurrent.ForkJoinPool;

/**
 * @author yzy
 * @date 2024/4/21 18:24
 */
@Component
public class TaskHandler {

    static ForkJoinPool forkJoinPool = new ForkJoinPool(5);


    public void submitTask(Runnable task) {
        forkJoinPool.submit(task);
    }
}
