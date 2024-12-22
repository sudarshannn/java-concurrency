package com.before_final;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyConcurrentHashMap <K,V> {
    private static class Node<K,V> {
        K key;
        V value;
        Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K,V>[] buckets;
    private int size = 0;
    private int capacity;
    private Lock[] locks;
    private float loadFactor = 0.75f;

    public MyConcurrentHashMap(int capacity) {
        this.capacity = capacity;
        buckets = new Node[capacity];
        locks = new Lock[capacity];
        for (int i=0;i<capacity;i++) {
            locks[i] = new ReentrantLock(true);
        }
    }

    private int getBucketIndex(K key, int capacity) {
        return (key.hashCode() % capacity);
    }

    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key, capacity);
        locks[bucketIndex].lock();
        try {
            Node<K,V> head = buckets[bucketIndex];
            Node<K,V> cur = head;
            while(cur != null) {
                if (cur.key.equals(key)) {
                    cur.value = value;
                    return;
                }
                cur = cur.next;
            }

            Node<K,V> newNode = new Node<>(key, value, head);
            buckets[bucketIndex] = newNode;
            size++;
        } catch (Exception e) {

        } finally {
            locks[bucketIndex].lock();
        }

        if (size > (loadFactor * capacity)) {
             resize();
        }
    }

    private void resize() {
        int newCapacity = 2*capacity;
        Node<K,V> newBuckets[] = new Node[newCapacity];
        Lock[] newLocks = new Lock[newCapacity];
        for (int i=0;i<newCapacity;i++) {
            newLocks[i] = new ReentrantLock(true);
        }

        for (int i=0;i<capacity;i++) {
            Node<K,V> cur = buckets[i];
            while(cur != null) {
                int newIndex = getBucketIndex(cur.key, newCapacity);
                Node<K,V> newNode = new Node<>(cur.key, cur.value, newBuckets[newIndex]);
                newBuckets[newIndex] = newNode;
                cur = cur.next;
            }
        }

        synchronized(this) {
            buckets = newBuckets;
            locks = newLocks;
            capacity = newCapacity;
        }
    }


    public int size()  {
        return size;
    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key, capacity);
        locks[bucketIndex].lock(); // we can use readLock here
        try {
            Node<K,V> head = buckets[bucketIndex];
            if (head == null) return null;

            Node<K,V> cur = head;
            while(cur != null) {
                if (cur.key.equals(key)) {
                    return cur.value;
                }
                cur = cur.next;
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            locks[bucketIndex].unlock();
        }
    }

    public void remove(K key) {
        int bucketIndex = getBucketIndex(key, capacity);
        locks[bucketIndex].lock();
        try {
            Node<K,V> head = buckets[bucketIndex];
            if (head == null) return;
            Node<K,V> cur = head;
            Node<K,V> prev = null;
            while(cur != null) {
                if (cur.key.equals(key)) {
                    if (prev == null) {
                        buckets[bucketIndex] = cur.next;
                    } else {
                        prev.next = cur.next;
                    }
                    size--;
                    return;
                }
                prev = cur;
                cur = cur.next;
            }
        } catch (Exception e) {

        } finally {
            locks[bucketIndex].unlock();
        }
    }

    public static void main(String[] args) {
        MyConcurrentHashMap<Integer, Integer> mp = new MyConcurrentHashMap<>(100);
        mp.put(10,10);
        System.out.println(mp.get(10));
        Map<Integer, Integer> mp2 = Collections.synchronizedMap(new HashMap<Integer, Integer>());
        ConcurrentHashMap<String, Integer> mp3 = new ConcurrentHashMap<>();
    }
}
