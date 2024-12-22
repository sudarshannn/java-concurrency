package com.before_final;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BathroomV2 {
    private int man = 0;
    private int woman = 0;

    private Lock lock = new ReentrantLock(true);
    private Condition bathRoomCondition = lock.newCondition();
    private Condition permitCondition = lock.newCondition();

    private int capacity;
    private int cur;
    private int waiting;

    private int gender = 0;
    // 0->none, 1->men , 2->female

    public BathroomV2(int capacity) {
        this.capacity = capacity;
        this.cur = 0;
        this.waiting = 0;
    }

    public void menIn() {
        lock.lock();
        try {
            while(gender == 2) {
                waiting++;
                bathRoomCondition.await();
                waiting--;
            }
            gender = 1;
            while(cur == capacity) {
                permitCondition.await();
            }
            cur++;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void menOut() {
        lock.lock();
        try {
            cur--;
            permitCondition.signalAll();
            if (cur == 0 && waiting == 0) {
                gender = 0;
                bathRoomCondition.signalAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void womanIn() {
        lock.lock();
        try {
            while(gender == 1) {
                waiting++;
                bathRoomCondition.await();
                waiting--;
            }
            gender = 2;
            while(cur == capacity) {
                permitCondition.await();
            }
            cur++;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void womanOut() {
        lock.lock();
        try {
            cur--;
            permitCondition.signalAll();
            if (cur == 0 && waiting == 0) {
                gender = 0;
                bathRoomCondition.signalAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}
