package com.dao;

public class Child extends Parent {
    String childName;
    public Child() {
        System.out.println("child constructor");
    }

    public Child(String childName) {
        this.childName = childName;
        System.out.println("child parameter constructor");
    }

    public void print() {
        System.out.println("child says");
    }
}
