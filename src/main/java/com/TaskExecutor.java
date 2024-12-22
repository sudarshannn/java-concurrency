package com;


import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskExecutor {

    private Set<String> completedTaskIds = Collections.synchronizedSet(new HashSet<>());
    private Set<String> visitedTaskIds = new HashSet<>();

    private boolean stopExecution = false;
    private Lock lock = new ReentrantLock(true);

    private ExecutorService executorService = Executors.newCachedThreadPool();
    List<Future<?>> futures = new ArrayList<>();
    public void run() throws ExecutionException, InterruptedException {
        while (!isStopExecution()) {
            List<Task> newTasks = (List<Task>) nextTasks(completedTaskIds);
            for (Task task : newTasks) {
                if (visitedTaskIds.contains(task.getId())) continue;
                visitedTaskIds.add(task.getId());
                Callable<Integer> callable = new Callable<Integer>() {
                    @Override
                    public Integer call() {
                        try {
                            task.doWork();
                        } catch (Exception e) {
                            lock.lock();
                            stopExecution = true;
                            lock.unlock();
                            return 503;
                        } finally {
                            completedTaskIds.add(task.getId());
                        }
                        return 200;
                    }
                };
                futures.add(executorService.submit(callable));
                if (isStopExecution()) {
                    break;
                }
            }
            if (isStopExecution()) {
                break;
            }
        }
        for (Future<?> future : futures) {
            future.get();
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
