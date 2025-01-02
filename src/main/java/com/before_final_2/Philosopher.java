package com.before_final_2;

import java.util.*;

public class Philosopher implements Runnable {
    Object leftFork;
    Object rightFork;
    int id;

    public Philosopher(Object leftFork, Object rightFork, int id) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while(true) {
                thinking();
                synchronized(leftFork) {
                    if (id == 5) {
                        debug("Philosopher " + id + " picked up right fork");
                    } else {
                        debug("Philosopher " + id + " picked up left fork");
                    }
                    synchronized(rightFork) {
                        if (id == 5) {
                            debug("Philosopher " + id + " picked up left fork");
                        } else {
                            debug("Philosopher " + id + " picked up right fork");
                        }
                        eat();
                        debug("Philosopher " + id + " dropped right fork");
                    }
                    debug("Philosopher " + id + " dropped left fork, back to thinking");
                }
            }
        } catch (Exception e) {

        }
    }

    private void thinking() {
        try {
            debug("Philosopher " + id + " is thinking.");
            Thread.sleep(new Random().nextInt(1000,2000));
        } catch(Exception e) {

        }
    }

    private void eat() {
        try {
            debug("Philosopher " + id + " starts eating.");
            Thread.sleep(new Random().nextInt(1000,2000));
        } catch(Exception e) {

        }
    }

    private void debug(String s) {
        System.out.println(s + " " + new Date());
    }


    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] forks = new Object[philosophers.length];
        for (int i=0;i<forks.length;i++) {
            forks[i] = new Object();
        }

        for (int i=0;i<philosophers.length;i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i+1) % philosophers.length];

            if (i == philosophers.length-1) {
                philosophers[i] = new Philosopher(rightFork, leftFork, i+1);
            } else {
                philosophers[i] = new Philosopher(leftFork, rightFork, i+1);
            }
            new Thread(philosophers[i]).start();
        }
    }
}
