package com;

import com.dao.User;

import java.util.*;
import java.util.stream.Collectors;

public class Sorting {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(5,4,3,2,1));
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1,o2);
            }
        });

        System.out.println(list);

        List<User> users = new ArrayList<>();
        users.add(new User("avinash", 24));
        users.add(new User("jay", 22));
        users.add(new User("sushant", 20));

//        users.sort(new Comparator<User>() {
//            @Override
//            public int compare (User o1, User o2) {
//                return Integer.compare(o1.getAge(), o2.getAge());
//            }
//        });

        Collections.sort(users);
        System.out.println(users);
        //(-(insertion point) - 1)
        int ind = Collections.binarySearch(users, new User("jay", 23));
        System.out.println(ind);
    }
}
