package com.before_final;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Stack<T> {
    // push,pop,top
    private static class Node<T> {
        T value;
        Node<T> previous, next;

        public Node(T value, Node<T> previous) {
            this.value = value;
            this.previous = previous;
        }
    }

    private int size;
    private ReadWriteLock lock;
    private Lock readLock, writeLock;
    private Node<T> head;

    public Stack() {
        size = 0;
        lock = new ReentrantReadWriteLock(true);
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        head = new Node(-1, null);
    }

    public void push(T val) {
        writeLock.lock();
        try {
            Node<T> cur = new Node(val, head);
            head = cur;
            size++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public T pop() {
        T result = null;
        writeLock.lock();
        try {
            if (size == 0) return null;
            result = head.value;
            Node<T> newHead = head.previous;
            head = newHead;
            size--;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
        return result;
    }

    public T top() {
        T result = null;
        readLock.lock();
        try {
            if (size == 0) return null;
            result = head.value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return result;
    }

    public static void main(String[] args) {
        Stack<Integer> s = new Stack<>();

        Thread t1 = new Thread(() -> {
            s.push(10);
            s.push(20);
            System.out.println(s.top());
            s.pop();
            System.out.println(s.top());
            s.pop();
        });


        Thread t2 = new Thread(() -> {
            s.push(30);
            s.push(40);
            s.pop();
        });

        t1.start();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }


        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {

        }
    }

}
