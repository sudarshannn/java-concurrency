package com.lock;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

class ZeroEvenOdd {
    private int n;
    private int counter = 0;

    Semaphore zeroQueue;
    Semaphore evenQueue;
    Semaphore oddQueue;

    public ZeroEvenOdd(int n) {
        this.n = n;
        this.counter = 0;
        zeroQueue = new Semaphore(1);
        evenQueue = new Semaphore(0);
        oddQueue = new Semaphore(0);
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        try {
            zeroQueue.acquire();
            counter++;
            if (counter <= n) {
                printNumber.accept(0);
                oddQueue.release();
                zeroQueue.acquire();
            }
            counter++;
            if (counter <= n) {
                printNumber.accept(0);
                evenQueue.release();
                zeroQueue.acquire();
            }
            zeroQueue.release();
        } finally {

        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        if (counter > n) return;
        evenQueue.acquire();
        printNumber.accept(counter);
        zeroQueue.release();
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        if (counter > n) return;
        oddQueue.acquire();
        printNumber.accept(counter);
        zeroQueue.release();
    }
}
