package com.lock;

import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Scheduler {

    private PriorityQueue<Task> pq;
    private ExecutorService executorService;
    private ReentrantLock lock = new ReentrantLock();
    private Condition newTasksAdded = lock.newCondition();
    private AtomicBoolean isRunning = new AtomicBoolean(true);

    public Scheduler(int workerThreadSize) {
        pq = new PriorityQueue<>();
        executorService = Executors.newFixedThreadPool(workerThreadSize);
    }

    public void start() {
        while(isRunning.get()) {
            lock.lock();
            try {
                while(pq.isEmpty()) {
                    newTasksAdded.await();
                }
                while(!pq.isEmpty()) {
                    long timeToSleep = pq.peek().getScheduledAt()  - System.currentTimeMillis();
                    if (timeToSleep <= 0) {
                        break;
                    }
                    newTasksAdded.await(timeToSleep, TimeUnit.MILLISECONDS);
                }

                Task task = pq.poll();
                lock.unlock();
                switch(task.getTaskType()) {
                    case 1:
                        executorService.submit(task.getCommand());
                        break;

                    case 2:
                        executorService.submit(task.getCommand());
                        task.setScheduledAt(task.getScheduledAt() + task.getTimeunit().toMillis(task.getPeriod()));

                        lock.lock();
                        pq.add(task);
                        newTasksAdded.signalAll();
                        lock.unlock();
                        break;

                    case 3:
                        executorService.submit(() -> {
                            Future<?> future = executorService.submit(task.getCommand());
                            try {
                                future.get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            long thenTime = System.currentTimeMillis();
                            task.setScheduledAt(thenTime + task.getTimeunit().toMillis(task.getPeriod()));

                            lock.lock();
                            pq.add(task);
                            newTasksAdded.signalAll();
                            lock.unlock();
                        });
                        break;
                }
            } catch (Exception e) {
                System.out.println("something went wrong");
                isRunning.set(false);
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }

    public void schedule(Runnable command, long initialDelay, TimeUnit timeUnit) {
        lock.lock();
        try {
            Task task = new Task(System.currentTimeMillis() + timeUnit.toMillis(initialDelay), null, command, timeUnit, 1);
            pq.add(task);
            newTasksAdded.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit timeUnit) {
        lock.lock();
        try {
            Task task = new Task(System.currentTimeMillis() + timeUnit.toMillis(initialDelay), period, command, timeUnit, 2);
            pq.add(task);
            newTasksAdded.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit timeUnit) {
        lock.lock();
        try {
            Task task = new Task(System.currentTimeMillis() + timeUnit.toMillis(initialDelay), delay, command, timeUnit, 3);
            pq.add(task);
            newTasksAdded.signalAll();
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler(10);

        Runnable task1 = getRunnable("Task1", 10);
        scheduler.schedule(task1, 10, TimeUnit.SECONDS);

        Runnable task2 = getRunnable("Task2", 5000);
        scheduler.scheduleAtFixedRate(task2, 5, 1, TimeUnit.SECONDS);

        Runnable task4 = getRunnable("Task4", 5000);
        scheduler.scheduleWithFixedDelay(task4, 1, 1, TimeUnit.SECONDS);

        scheduler.start();
    }

    private static Runnable getRunnable(String taskName, long sleepTime) {
        return new Runnable() {
          @Override
          public void run() {
              System.out.println("Task " + taskName  + " starts at " + new Date(System.currentTimeMillis()));
              try {
                  Thread.sleep(sleepTime);
              } catch(Exception e) {

              }
              System.out.println("Task " + taskName  + " ends at " + new Date(System.currentTimeMillis()));
          }
        };
    }


    public static class Task implements Comparable<Task> {
        private Long scheduledAt;
        private Long period;
        private Runnable command;
        private TimeUnit timeunit;
        private int taskType;

        public Task(Long scheduledAt, Long period, Runnable runnable, TimeUnit timeunit, int taskType) {
            this.scheduledAt = scheduledAt;
            this.period = period;
            this.command = runnable;
            this.timeunit = timeunit;
            this.taskType = taskType;
        }

        public Long getScheduledAt() {
            return scheduledAt;
        }

        public void setScheduledAt(Long scheduledAt) {
            this.scheduledAt = scheduledAt;
        }

        public int getTaskType() {
            return taskType;
        }

        public Runnable getCommand() {
            return command;
        }

        public TimeUnit getTimeunit() {
            return timeunit;
        }

        public Long getPeriod() {
            return period;
        }

        @Override
        public int compareTo(Task that) {
            return Long.compare(this.scheduledAt, that.scheduledAt);
        }
    }
}

