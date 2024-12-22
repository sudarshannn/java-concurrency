package com;

import java.util.Set;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskExecutor2 {

    private Set<String> completedTaskIds = Collections.synchronizedSet(new HashSet<>());
    private Set<String> visitedTaskIds = new HashSet<>();

    private boolean stopExecution = false;
    private Lock lock = new ReentrantLock(true);

    private ExecutorService executorService = Executors.newCachedThreadPool();
    Set<Thread> threads = new HashSet<>();
    public void run() throws ExecutionException, InterruptedException {
        while (!isStopExecution()) {
            List<Task> newTasks = (List<Task>) nextTasks(completedTaskIds);
            for (Task task : newTasks) {
                if (visitedTaskIds.contains(task.getId())) continue;
                visitedTaskIds.add(task.getId());
                Thread thread = new Thread(()-> {
                    try {
                        task.doWork();
                    } catch (Exception e) {
                        lock.lock();
                        stopExecution = true;
                        lock.unlock();
                    } finally {
                        completedTaskIds.add(task.getId());
                    }
                });
                threads.add(thread);
            }
            if (isStopExecution()) {
                break;
            }
        }
        for (Thread thread : threads) {
            thread.join();
            if (isStopExecution()) {
                break;
            }
        }
    }

    private boolean isStopExecution() {
        boolean result ;
        lock.lock();
        result = stopExecution;
        lock.unlock();
        return result;
    }


    private Collection<Task> nextTasks(Collection<String> completedIDs) {
        return Collections.emptyList();
    }


    public static class Task {
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
