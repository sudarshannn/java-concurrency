package com.lock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQ<T> {
    Queue<T> queue;
    ReentrantLock lock;
    Condition queueContainsSpace;
    Condition queueContainsElements;
    int maxSize;

    public BlockingQ(int n) {
        queue = new LinkedList<>();
        maxSize = n;
        lock = new ReentrantLock(true);
        queueContainsSpace = lock.newCondition();
        queueContainsElements = lock.newCondition();
    }

    public void add(T value) {
        lock.lock();
        try {
            while(queue.size() == maxSize) {
                System.out.println("ALL SLOTS FILLED. WAITING TO ADD " + value);
                queueContainsSpace.await();
            }
            queue.add(value);
            queueContainsElements.signalAll();
        } catch (InterruptedException e) {

        } finally {
            lock.unlock();
        }
    }

    public T pop() {
        T result = null;
        lock.lock();
        try {
            while(queue.size() == 0) {
                System.out.println("ALL SLOTS EMPTY. WAITING TO REMOVE");
                queueContainsElements.await();
            }
            result = queue.remove();
            queueContainsSpace.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
        return result;
    }
}
