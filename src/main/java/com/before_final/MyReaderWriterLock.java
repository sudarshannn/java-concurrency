package com.before_final;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MyReaderWriterLock {
    private int reader = 0;
    private int writer = 0;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void readLock() throws InterruptedException {
        lock.lock();
        while(writer > 0) {
            condition.await();
        }
        reader++;
        lock.unlock();
    }

    public void readUnlock() throws InterruptedException {
        lock.lock();
        reader--;
        condition.signalAll();
        lock.unlock();
    }

    public void writeLock() throws InterruptedException {
        lock.lock();
        while(reader > 0 || writer > 0) {
            condition.await();
        }
        writer++;
        lock.unlock();
    }

    public void writeUnlock() throws InterruptedException {
        lock.lock();
        writer--;
        condition.signalAll();
        lock.unlock();
    }
}
