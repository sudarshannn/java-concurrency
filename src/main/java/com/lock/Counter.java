package com.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {

    private int count;
    private AtomicInteger proCounter = new AtomicInteger(0);
    private ReentrantLock lock = new ReentrantLock();

    // if synchronized in method, then method synchronization
    // block synchronization
    public void increment() {
        try {
            lock.lock();
            count++;
        } finally {
            lock.unlock();
        }
    }

    public void incrementAtomic() {
        proCounter.getAndIncrement();
        proCounter.addAndGet(4);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public AtomicInteger getProCounter() {
        return proCounter;
    }

    public void setProCounter(AtomicInteger proCounter) {
        this.proCounter = proCounter;
    }
}
