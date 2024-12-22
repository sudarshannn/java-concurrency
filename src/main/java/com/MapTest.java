package com;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapTest {
    public static void main(String[] args) {
        Map<Integer,Integer> mp = Collections.synchronizedMap(new HashMap<>());
        mp.put(1,2);
        mp.put(3,2);

        ConcurrentMap<Integer, Integer> mp2 = new ConcurrentHashMap<>();
        mp2.put(1,3);
        mp2.get(1);

    }
}
