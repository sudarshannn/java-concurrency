package com;

class Singleton {
    private volatile static Singleton instance = null;
    // private constructor to avoid calling new from outside the class

    // declare the fields that you need in your Signleton/subclass of Singleton

    private Singleton() {
        // initialize the fields of the object
    }

    public static Singleton getInstance() {
        if(instance == null) {
            synchronized(Singleton.class) {
                if(instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}