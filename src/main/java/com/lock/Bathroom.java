package com.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bathroom {
    private int capacity;
    private Lock lock;

    int cur = 0;
    int waiting = 0;

    private Condition bathroomCondition;
    private Condition permitCondition;

    private static String MEN = "MEN";
    private static String WOMEN = "WOMEN";
    private static String NONE = "NONE";

    private String gender = "NONE";

    public Bathroom(int size) {
        capacity = size;
        lock = new ReentrantLock(true);
        bathroomCondition = lock.newCondition();
        permitCondition = lock.newCondition();
    }

    public void manIn() throws InterruptedException {
        lock.lock();
        while (WOMEN.equals(gender)) {
            bathroomCondition.await();
        }
        gender = MEN;
        while (cur == capacity) {
            waiting++;
            permitCondition.await();
            waiting--;
        }
        cur++;
        debug("Men++ " + " cur occupied =" + cur);
        lock.unlock();
    }

    public void manOut() {
        lock.lock();
        cur--;
        debug("Men-- " + " cur occupied =" + cur);
        permitCondition.signalAll();
        if (cur == 0 && waiting == 0) {
            gender = NONE;
            bathroomCondition.signalAll();
            debug("Men leaving bathroom.");
        }
        lock.unlock();
    }

    public void womanIn() throws InterruptedException {
        lock.lock();
        while (MEN.equals(gender)) {
            bathroomCondition.await();
        }
        gender = WOMEN;
        while (cur == capacity) {
            waiting++;
            permitCondition.await();
            waiting--;
        }
        cur++;
        debug("Women++ " + " cur occupied =" + cur);
        lock.unlock();
    }

    public void womanOut() {
        lock.lock();
        cur--;
        debug("Women-- " + " cur occupied =" + cur);
        permitCondition.signalAll();
        if (cur == 0 && waiting == 0) {
            gender = NONE;
            bathroomCondition.signalAll();
            debug("Women leaving bathroom.");
        }
        lock.unlock();
    }

    private void debug(String s) {
        System.out.println(s);
    }
}
