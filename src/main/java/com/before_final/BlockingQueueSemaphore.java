package com.before_final;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueSemaphore<T> {
    Semaphore items, spaces;
    Queue<T> queue;
    Lock lock;

    public BlockingQueueSemaphore(int capacity) {
        items = new Semaphore(capacity, true);
        spaces = new Semaphore(0 , true);
        queue = new LinkedList<>();
        lock = new ReentrantLock(true);
    }

    public void put(T value) throws InterruptedException {
        items.acquire();

        lock.lock();
        queue.add(value);
        lock.unlock();

        spaces.release();
    }

    public void take(T value) throws InterruptedException {
        spaces.acquire();

        lock.lock();
        queue.add(value);
        lock.unlock();

        items.release();
    }



}
