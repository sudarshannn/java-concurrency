package com.lock;

import java.util.concurrent.*;

public class PoolTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(2),
                new SprRejectionPolicy());

        int n = 10;
        MyBlockingQueue<Long> queue = new MyBlockingQueue(5);

        Thread t1 = new Thread(() -> {
           for (int i=0;i<n;i++) {
               try {
                   queue.add((long) i);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });

        Thread t2 = new Thread(() -> {
            for (int i=0;i<n;i++) {
                try {
                    System.out.println("poped is " + queue.pop());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
//        t2.start();
    }

    public static class SprRejectionPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor p) {
            throw new RejectedExecutionException("dcl bhai runnable " + r);
        }
    }

    public static class Task implements Runnable {
        @Override
        public void run() {
            try {
                for(int i=0;i<1000;i++) {

                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Task is running by " +Thread.currentThread().getName());
        }

        @Override
        public String toString() {
            return "dcl-task";
        }
    }

    public static class CallTask implements Callable<String> {
        @Override
        public String call() throws InterruptedException {
            Thread.sleep(2000);
            return "DCL";
        }
    }
}


