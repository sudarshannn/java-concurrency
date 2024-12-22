package com.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Stack<T> {

    private Node head;
    private ReadWriteLock lock;
    private Lock readLock, writeLock;
    private int size;

    public Stack() {
        head = new Node(null);
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        size = 0;
    }

    public void push(T value) throws InterruptedException {
        writeLock.lock();
        try {
            Node cur = new Node(value);
            head.setRight(cur);
            cur.setLeft(head);
            head = cur;
            size++;
        } finally {
            writeLock.unlock();
        }
    }

    public T top() {
        readLock.lock();
        try {
            if (size == 0) return null;
            return (T) head.getValue();
        } finally {
            readLock.unlock();
        }
    }

    public void pop() {
        writeLock.lock();
        try {
            if (size == 0) return;
            Node curHead = head;
            Node newHead = head.getLeft();
            head = newHead;
            head.setRight(null);
            size--;
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        int answer;
        readLock.lock();
        answer = size;
        readLock.unlock();
        return answer;
    }

    public static class Node<T> {
        private T value;
        Node left = null , right = null;

        public Node(T value) {
            this.value = value;
            left = null;
            right = null;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getLeft() {
            return this.left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
