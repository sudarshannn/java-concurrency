package com;

import com.dao.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DCL {
    public static void main(String[] args) {
        Person p1 = new Person("p1", 23);
        Person p2 = new Person("p2", 24);
        Person p3 = new Person("p3", 25);
        Person p4 = new Person("p4", 21);
        Person p5 = new Person("p5", 30);
        Person p6 = new Person("p6", 32);

        Graph graph = new Graph();
        graph.addFriends(p1, p5, 100L);
        graph.addFriends(p1, p2, 20L);
        graph.addFriends(p1, p3, 1L);
        graph.addFriends(p3, p4, 2L);
        graph.addFriends(p4, p5, 3L);
        graph.addFriends(p5, p6, 4L);

        Map<Person, Long> result = graph.dijkstra(p1);
        System.out.println(result);

        List<Integer> l = new ArrayList<>();
    }
}
