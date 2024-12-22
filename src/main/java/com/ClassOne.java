package com;

public class ClassOne {
    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);
    volatile boolean a = false;
    public static void main(String[] args) {
        System.out.println(threadLocal.get());
        threadLocal.set(threadLocal.get() + 1);
        System.out.println(threadLocal.get());
        new Thread(() -> {
            System.out.println(threadLocal.get());
        }).start();

        threadLocal.set(threadLocal.get() + 1);
        System.out.println(threadLocal.get());
        new Thread(() -> {
            System.out.println(threadLocal.get());
        }).start();



    }
}
