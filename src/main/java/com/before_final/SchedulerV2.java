package com.before_final;

import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SchedulerV2 {

    private static class Task implements Comparable<Task> {
        Long scheduledAt;
        Runnable command;
        Long period;
        TimeUnit timeUnit;
        TaskType taskType;

        public Long getScheduledAt() {
            return scheduledAt;
        }

        public void setScheduledAt(Long scheduledAt) {
            this.scheduledAt = scheduledAt;
        }

        public Runnable getCommand() {
            return command;
        }

        public Long getPeriod() {
            return period;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public TaskType getTaskType() {
            return taskType;
        }

        public Task(Long scheduledAt, Runnable command, Long period, TimeUnit timeUnit , TaskType taskType) {
            this.scheduledAt = scheduledAt;
            this.command = command;
            this.period = period;
            this.timeUnit = timeUnit;
            this.taskType  = taskType;
        }

        public enum TaskType {
            ONE_TIME,
            FIX_PERIOD,
            FIX_DELAY,
            FIX_DELAY_2,
        }

        @Override
        public int compareTo(Task that) {
            return Long.compare(this.scheduledAt, that.scheduledAt);
        }
    }

    private PriorityQueue<Task> taskQueue;
    private ReentrantLock lock;
    private ExecutorService executorService;
    private Condition taskAddCondition;

    public SchedulerV2(int workerThreads) {
        taskQueue = new PriorityQueue<>();
        lock = new ReentrantLock();
        executorService = Executors.newFixedThreadPool(workerThreads);
        taskAddCondition = lock.newCondition();
    }

    public void start() {
        while(true) {
            lock.lock();
            try {
                while(taskQueue.isEmpty()) {
                    try {
                        taskAddCondition.await();
                    } catch(Exception e){

                    }
                }
                while(!taskQueue.isEmpty()) {
                    long sleepTime = taskQueue.peek().getScheduledAt() - System.currentTimeMillis();
                    if (sleepTime < 0 ) {
                        break;
                    }
                    taskAddCondition.await(sleepTime, TimeUnit.MILLISECONDS);
                }
                Task curTask = taskQueue.poll();
                lock.unlock();
                switch (curTask.getTaskType()) {
                    case Task.TaskType.ONE_TIME:
//                        executorService.submit(curTask.getCommand());
                        new Thread(curTask.getCommand()).start();
                        break;

                    case Task.TaskType.FIX_PERIOD:
//                        executorService.submit(curTask.getCommand());
                        new Thread(curTask.getCommand()).start();
                        curTask.setScheduledAt(curTask.getScheduledAt() + curTask.getTimeUnit().toMillis(curTask.getPeriod()));
                        addTaskInQueue(curTask);
                        break;

                    case Task.TaskType.FIX_DELAY:
                        executorService.submit(() -> {
                            Future<?> future = executorService.submit(curTask.getCommand());
                            try {
                                future.get();
                            } catch(Exception e) {

                            }
                            long thenTime = System.currentTimeMillis();
                            curTask.setScheduledAt(thenTime + curTask.getTimeUnit().toMillis(curTask.getPeriod()));
                            addTaskInQueue(curTask);
                        });
                        break;

                    case Task.TaskType.FIX_DELAY_2:
                        new Thread(() -> {
                            Thread t1 = new Thread(curTask.getCommand());
                            t1.start();
                            try {
                                t1.join();
                            } catch(Exception e) {

                            }
                            long thenTime = System.currentTimeMillis();
                            curTask.setScheduledAt(thenTime + curTask.getTimeUnit().toMillis(curTask.getPeriod()));
                            addTaskInQueue(curTask);
                        }).start();
                        break;
                }

            } catch(Exception e) {
                break;
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        SchedulerV2 scheduler = new SchedulerV2(10);
        scheduler.schedule(getRunnable("task1", 2000), 2, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(getRunnable("task2", 5000), 2,2,  TimeUnit.SECONDS);
        scheduler.scheduleWithFixedDelay(getRunnable("task3", 2000), 4, 1, TimeUnit.SECONDS);
        scheduler.start();
    }


    private static Runnable getRunnable(String taskName, long sleepTime) {
        return () -> {
            try {
                System.out.println("task " + taskName + ", started at " + new Date());
                Thread.sleep(sleepTime);
                System.out.println("task " + taskName + ", ended at " + new Date());
            } catch(Exception e) {

            }
        };
    }


    public void schedule(Runnable command, long initialDelay, TimeUnit timeUnit) {
        Task task = new Task(System.currentTimeMillis() + timeUnit.toMillis(initialDelay), command, null, timeUnit, Task.TaskType.ONE_TIME);
        addTaskInQueue(task);
    }

    public void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit timeUnit) {
        Task task = new Task(System.currentTimeMillis() + timeUnit.toMillis(initialDelay), command, period, timeUnit, Task.TaskType.FIX_PERIOD);
        addTaskInQueue(task);
    }

    public void scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit timeUnit) {
        Task task = new Task(System.currentTimeMillis() + timeUnit.toMillis(initialDelay), command, delay, timeUnit, Task.TaskType.FIX_DELAY);
        addTaskInQueue(task);
    }

    private void addTaskInQueue(Task task) {
        lock.lock();
        taskQueue.add(task);
        taskAddCondition.signalAll();
        lock.unlock();
    }



}
