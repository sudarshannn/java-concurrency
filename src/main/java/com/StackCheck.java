package com;

import java.util.Stack;

public class StackCheck {
    public static void main(String[] args) {
        Stack<String> s = new Stack<>();
        s.push("1");
        s.push("2");
        s.push("3");
        s.push("4");

        while(!s.isEmpty()) {
            System.out.println(s.pop());
        }
    }
}
