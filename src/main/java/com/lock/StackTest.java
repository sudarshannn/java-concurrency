package com.lock;

import com.before_final.MyReaderWriterLock;

public class StackTest {
    public static void main(String[] args) throws Exception{
        Stack<String> s = new Stack();
        s.push("1");
        s.push("2");
        s.push("3");
        s.push("4");
        s.push("5");

        System.out.println(s.top());
        System.out.println(s.size());
        MyReaderWriterLock lock = new MyReaderWriterLock();

        s.pop();

        System.out.println(s.top());
        System.out.println(s.size());
    }
}
