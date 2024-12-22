package com.lock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyBlockingQueue<T> {
    private Queue<T> queue;
    private Semaphore spaces;
    private Semaphore items;
    private ReadWriteLock lock;
    private int maxSize;

    public MyBlockingQueue(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Size cannot be empty");
        }
        queue = new LinkedList<>();
        this.maxSize = maxSize;
        spaces = new Semaphore(maxSize, true);
        items = new Semaphore(0, true);
        lock = new ReentrantReadWriteLock();
    }

    public void add(T value) throws InterruptedException {
        spaces.acquire();
        synchronized(queue) {
            queue.add(value);
        }
        items.release();
    }


    public T pop() throws InterruptedException{
        T result = null;
        items.acquire();
        synchronized(queue) {
            result = queue.remove();
        }
        spaces.release();
        return result;
    }
}
