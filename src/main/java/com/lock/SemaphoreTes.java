package com.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntConsumer;

public class SemaphoreTes {
    public static void main(String[] args) throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(10);
        int n = 10;
        ZeroEvenOdd object = new ZeroEvenOdd(n);
        IntConsumer printNumber = (value) -> System.out.println(value);


        for(int i=0;i<n;i++) {
            Thread t1 = new Thread(() -> {
                try {
                    object.zero(printNumber);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            Thread t2 = new Thread(() -> {
                try {
                    object.even((printNumber));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            Thread t3 = new Thread(() -> {
                try {
                    object.odd((printNumber));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t1.start();
            t2.start();
            t3.start();
        }
    }
}
