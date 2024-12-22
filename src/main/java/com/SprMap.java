package com;

import com.dao.User;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SprMap {
    public static void main(String[] args) {
        TreeMap<Integer, Integer> mp = new TreeMap <>();
        mp.put(10,2);
        mp.put(6,0);
        mp.put(3,2);
        mp.put(1,4);

        mp.putIfAbsent(1,2);

        TreeMap<User,Integer> user = new TreeMap<>();
        user.put(new User("z", 10), 180);
        user.put(new User("a", 9), 170);
        user.put(new User("b", 8), 140);
        user.put(new User("a", 5), 100);

        System.out.println(user.containsKey(new User("a", 9)));
        SortedMap<User,Integer> sortedMap = user.tailMap(new User("a", 5), false);
        for(Map.Entry<User, Integer> entry : sortedMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
