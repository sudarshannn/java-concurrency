package com.lock;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class SchedulerService {

    private volatile PriorityQueue<Task> taskList;
    private final Object lock = new Object();
    private AtomicBoolean isRunning;
    private AtomicLong tasksRunCount = new AtomicLong(0);
    private AtomicLong tasksScheduledCount = new AtomicLong(0);

    public SchedulerService() {
        this.taskList = new PriorityQueue<>(Comparator.comparingLong(Task::getRunAt));
        this.isRunning = new AtomicBoolean(true);
    }

    public void start() {
        Thread taskCreatorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startCreatingTasks();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread schedulerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startScheduling();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        schedulerThread.start();
        taskCreatorThread.start();

        try {
            taskCreatorThread.join();
            schedulerThread.join();

            System.out.println("Tasks scheduled : " + tasksScheduledCount.get());
            System.out.println("Tasks executed : " + tasksRunCount.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void startScheduling() throws InterruptedException {
        executeTasks();
        //        System.out.println("Starting scheduling");
        //        long timeBeforeExecuting = System.currentTimeMillis();
        //        int index = 0;
        //        while(isRunning.get()) {
        //
        //        }
        //        System.out.println("Active task count : " + threadPoolExecutor.getActiveCount());
        //        System.out.println("Queue task count : " + threadPoolExecutor.getQueue().size());
        //        System.out.println("Total task count : " + threadPoolExecutor.getTaskCount());
        //        System.out.println("Shutting down now");
        //        threadPoolExecutor.shutdownNow();
        //        long timeAfterExecuting = System.currentTimeMillis();
        //        System.out.println("Scheduler ran for " + (timeAfterExecuting - timeBeforeExecuting) + " millis");
        //        Thread.currentThread().interrupt();
    }

    private void executeTasks() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(1, 10, 100, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(5));
        synchronized (lock) {
            lock.wait();
            //            System.out.println("Processing tasks in list");
            while (!taskList.isEmpty()) {
                Task task = taskList.peek();
                long taskRunAt = task.getRunAt();
                if (System.currentTimeMillis() >= taskRunAt) {
                    threadPoolExecutor.submit(createTaskRunnable(task));
                    taskList.poll();
                    continue;
                }
                long waitMillis = taskRunAt - System.currentTimeMillis();
                System.out.println("Waiting for " + waitMillis + " millis");
                lock.wait(waitMillis);
                task = taskList.peek();
                long newTaskRunAt = task.getRunAt();
                if (newTaskRunAt != taskRunAt) {
                    System.out.println("First element of queue changed");
                }
                taskRunAt = newTaskRunAt;
                if (System.currentTimeMillis() >= taskRunAt) {
                    System.out.println("Wait finished due to task time");
                    threadPoolExecutor.submit(createTaskRunnable(task));
                    taskList.poll();
                } else {
                    System.out.println("Wait finished due to interrupt");
                }
            }
        }
        threadPoolExecutor.shutdown();
    }

    private void schedule(Task task) {
        synchronized (lock) {
            System.out.println("Scheduling task for time : " + task.getRunAt());
            taskList.add(task);
            tasksScheduledCount.incrementAndGet();
            lock.notifyAll();
        }
    }

    public void startCreatingTasks() throws InterruptedException {
        System.out.println("Starting task creation");
        while(isRunning.get()) {
            long runAfter = System.currentTimeMillis () + (long) (Math.random() * 10000);
            Task task = new Task(runAfter, false);
            schedule(task);
            //            Thread.sleep(100L); //sleeping for 1 second after each task creation
            boolean keepRunning = Math.random() > 0.02;
            isRunning.set(keepRunning);
        }
        System.out.println("Task Creation completed");
    }

    private Runnable createTaskRunnable(Task task) {
        return new Runnable() {
            @Override
            public void run() {
                long runAt = task.getRunAt();
                System.out.println("Ran task for " + runAt + " milliseconds");
                tasksRunCount.incrementAndGet();
            }
        };
    }

    private List<Task> createTasks() {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            int runAfter = (int) (Math.random() * 1000);
            Task task = new Task(runAfter, false);
            tasks.add(task);
        }
        return tasks;
    }

    private class Task {
        private long runAt;
        private boolean isRecurring;

        Task(long runAfter, boolean isRecurring) {
            this.runAt = runAfter;
            this.isRecurring = isRecurring;
        }
        public boolean getIsRecurring() {
            return isRecurring;
        }

        public void setIsRecurring(boolean isRecurring) {
            this.isRecurring = isRecurring;
        }

        public long getRunAt() {
            return runAt;
        }

        public void setRunAt(long runAt) {
            this.runAt = runAt;
        }
    }
}