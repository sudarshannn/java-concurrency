package com.dao;

public class Parent extends GrandParent {
    private String name;
    public Parent() {
        System.out.println("parent constructor");
    }

    public void print() {
        System.out.println("parent says");
    }
}
