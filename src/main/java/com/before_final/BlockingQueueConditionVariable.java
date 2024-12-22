package com.before_final;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueConditionVariable<T> {

    Queue<T> queue;
    Lock lock;
    Condition queueContainsSpaces, queueContainsItems;
    int capacity;

    public BlockingQueueConditionVariable(int capacity) {
        queue = new LinkedList<>();
        lock = new ReentrantLock();
        queueContainsSpaces = lock.newCondition();
        queueContainsItems = lock.newCondition();
        this.capacity = capacity;
    }

    public void put(T val) throws InterruptedException {
        lock.lock();
        try {
            while(queue.size() == capacity) {
                queueContainsSpaces.await();
            }
            queue.add(val);
            queueContainsItems.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        T result;
        lock.lock();
        try {
            while(queue.size()  == 0) {
                queueContainsItems.await();
            }
            result = queue.remove();
            queueContainsSpaces.signalAll();
        } finally {
            lock.unlock();
        }
        return result;
    }
}
