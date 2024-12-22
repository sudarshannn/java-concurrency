package com.lock;

import java.util.stream.Stream;

public class QTest {
    public static void main(String[] args) {
        BlockingQ<Integer> myQueue = new BlockingQ<>(5);

        Thread threadA = new Thread(() -> {

            Stream.of(10, 20, 30, 40, 50).
                    forEach(myQueue::add);

            myQueue.add(200);
            myQueue.add(800);
        });


        Thread threadB = new Thread(() -> {
            Stream.iterate(0, i -> i < 7, i -> i + 1).
                    forEach((i) -> {
                        Integer value = myQueue.pop();
                        System.out.println(value);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });

        });

        threadA.start();
        threadB.start();
    }
}
