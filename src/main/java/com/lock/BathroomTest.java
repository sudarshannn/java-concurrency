package com.lock;

public class BathroomTest {
    public static void main(String[] args) throws InterruptedException {
        Bathroom bathroom = new Bathroom(3);

        Thread t1 = new Thread(() -> {
            try {
                bathroom.manIn();
                bathroom.manIn();
                bathroom.manIn();

                Thread.sleep(4000);
                bathroom.manOut();
                Thread.sleep(4000);
                bathroom.manOut();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                bathroom.manIn();
                Thread.sleep(4000);
                bathroom.manOut();
                bathroom.manOut();
                Thread.sleep(4000);
                bathroom.womanIn();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t2.start();
        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(5000);
                bathroom.womanIn();
                bathroom.womanIn();
                Thread.sleep(5000);
                bathroom.womanOut();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });

        Thread t4 = new Thread(() -> {
            try {
                Thread.sleep(5000);
                bathroom.womanIn();
                Thread.sleep(5000);
                bathroom.womanOut();
                bathroom.womanOut();
                bathroom.womanOut();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t3.start();
        t4.start();

    }
}
