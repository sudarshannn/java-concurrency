package com.messaging;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer implements Runnable {
    private BlockingQueue<String> queue;
    private ReentrantLock lock;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public Consumer(ReentrantLock lock) {
        this.lock = lock;
    }

//    @Override
//    public void run() {
//        String msg;
//        try {
//            while(!(msg = queue.take()).equals("10")) {
//                System.out.println("Consumed: " + msg);
//                Thread.sleep(4000);
//            }
//        } catch(InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Consumer ends.");
//    }

    @Override
    public void run() {
        try {
            lock.lock();
            System.out.println("lock is with consumer");
            Thread.sleep(3000);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
