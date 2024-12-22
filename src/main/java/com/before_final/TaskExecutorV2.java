package com.before_final;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskExecutorV2 {

    private Set<String> finishedTaskIds;
    private Set<String> visitedTaskIds;
    private boolean stopExecution;
    private ExecutorService executorService;
    private Lock lock;

    public TaskExecutorV2(int workerThreads) {
        finishedTaskIds = Collections.synchronizedSet(new HashSet<>());
        visitedTaskIds = new HashSet<>();
        stopExecution = false;
        executorService = Executors.newFixedThreadPool(workerThreads);
        lock = new ReentrantLock(true);
    }

    public void start() {
        List<Future<?>> futures = new ArrayList<>();
        while (!isStopExecution()) {
            try {
                List<Task> tasks = getNextTasks(finishedTaskIds);
                for (Task task : tasks) {
                    if (isStopExecution()) break;
                    if (visitedTaskIds.contains(task.getId())) continue;
                    visitedTaskIds.add(task.getId());

                    Callable<Integer> callable = () -> {
                        int statusCode;
                        try {
                            task.doWork();
                            statusCode = 200;
                        } catch (Exception e) {
                            lock.lock();
                            stopExecution = true;
                            lock.unlock();
                            statusCode = 502;
                        } finally {
                            finishedTaskIds.add(task.getId());
                        }
                        return statusCode;
                    };
                    futures.add(executorService.submit(callable));
                }
            } catch (Exception e) {
                e.printStackTrace();
                lock.lock();
                stopExecution = true;
                lock.unlock();
            }

            for (Future future : futures) {
                if (isStopExecution()) {
                    break;
                }
                try {
                    future.get();
                } catch (Exception e) {

                }
            }
        }

    }

    private boolean isStopExecution() {
        lock.lock();
        try {
            return stopExecution;
        } finally {
            lock.unlock();
        }
    }

    private List<Task> getNextTasks(Collection<String> finishedTaskIds) {
        // logic here
        return Collections.emptyList();
    }

    private static class Task {
        private String id;

        public Task(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void doWork() {

        }

    }
}
