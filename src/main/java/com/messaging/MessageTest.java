package com.messaging;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageTest {
    public static void main(String[] args) {
        BlockingQueue<String> q = new ArrayBlockingQueue<>(5);
        Producer p = new Producer(q);
        Consumer c = new Consumer(q);

        Thread t1 = new Thread(p);
        Thread t2 = new Thread(c);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("program: ends");
    }
}
