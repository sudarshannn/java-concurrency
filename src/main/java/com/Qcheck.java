package com;

import java.util.*;
import java.util.concurrent.*;

public class Qcheck {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> ll = new ArrayBlockingQueue<>(10);
        Queue<String> pq = new PriorityQueue<>(1, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });

        pq.offer("20");
        pq.offer("30");
        pq.offer("40");
        System.out.println(pq.offer("20"));
        System.out.println(pq.size());

        ll = new SynchronousQueue<>();
        System.out.println(ll.peek());


    }
}
