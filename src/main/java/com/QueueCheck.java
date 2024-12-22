package com;


import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

public class QueueCheck {
    public static void main(String[] args) {
        Deque<String> dq = new LinkedBlockingDeque<>(1);
        dq.offerFirst("first");
        dq.addFirst("second");

        Deque<String> ll = new LinkedList<>();
        System.out.println(String.class.isInstance(dq.peekFirst()));
        while(!dq.isEmpty()) {
            System.out.println(dq.pollFirst());
        }

        Iterator<String> iterator = dq.iterator();

        for(String s : dq) {
            System.out.println(s);
        }
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }



        Queue<String> q = new PriorityBlockingQueue<>();
        q.add("1");
        q.add("2");
        q.add("3");
        q.add("4");

        for(String s : q) {
            System.out.println(s);
        }

//        Iterator<String> it = q.iterator();
//
//
//        while(it.hasNext()) {
//            System.out.println(it.next());
//        }
//
//        while(!q.isEmpty()) {
//            System.out.println(q.peek());
//            System.out.println(q.poll());
//        }


        BlockingQueue<Integer> pq = new ArrayBlockingQueue<>(10); // bounded
        try {
            pq.put(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
