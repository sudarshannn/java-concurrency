package com;

public class Running implements Runnable {

    private String threadName;

    public Running(String threadName) {
        this.threadName = threadName;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void run() {
        for (int i=0;i<5;i++) {
            System.out.println("I am runner: " + threadName + " , this is my " + i + " round");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("thread " + threadName + " got Interrupted.");
            }
        }
        System.out.println("thread " + threadName + " got completed.");
    }
}
