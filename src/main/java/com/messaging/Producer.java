package com.messaging;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Producer implements Runnable {

    private BlockingQueue<String> queue;
    private ReentrantLock lock;

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public Producer(ReentrantLock lock) {
        this.lock = lock;
    }

//    @Override
//    public void run() {
//        for(int i=0;i<=10;i++) {
//            try {
//                System.out.println("producing: " + i);
//                queue.put("" + i);
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        System.out.println("producer ends.");
//    }


    public void run() {
        try {
            lock.lock();
            System.out.println("lock is with producer");
            Thread.sleep(3000);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
