package com;

import java.util.Random;

public class MathTest {
    public static void main(String[] args) {
        long a = 101;
        a = (long) Math.sqrt(a);

        new Random().nextInt(10);

        Character c = 'a';

        String s = "hello";
        for(int i=0;i<s.length();i++) {
            s.charAt(i);
        }
        System.out.println(a);
    }
}
