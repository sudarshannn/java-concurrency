package com.dao;

import java.util.*;

public class Graph {
    private Map<Person, List<Pair<Person, Long>>> adjacencyList;
    private Set<Person> visited;

    public Graph() {
        adjacencyList = new HashMap<>();
        visited = new HashSet<>();
    }

    public Map<Person, Long> dijkstra(Person p) {
        PriorityQueue<Pair<Long, Person>> pq = new PriorityQueue<>(new Comparator<Pair<Long, Person>>() {
            @Override
            public int compare(Pair<Long, Person> p1, Pair<Long, Person> p2) {
                return Long.compare(p1.getFirst(), p2.getFirst());
            }
        });
        Map<Person, Long> distances = new HashMap<>();
        distances.put(p, 0L);
        pq.add(new Pair<>(0L, p));
        while (!pq.isEmpty()) {
            Pair<Long, Person> curPair = pq.poll();
            Person currentPerson = curPair.getSecond();
            Long currentCost = curPair.getFirst();
            for (Pair<Person, Long> friendPair : adjacencyList.get(currentPerson)) {
                Person friend = friendPair.getFirst();
                distances.computeIfAbsent(friend, k -> (long) Integer.MAX_VALUE);
                Long friendCost = distances.get(friend);
                if (friendCost > currentCost + friendPair.getSecond()) {
                    distances.put(friend, currentCost + friendPair.getSecond());
                    pq.add(new Pair<>(distances.get(friend), friend));
                }
            }
        }
        return distances;
    }

    public void addFriends(Person p1, Person p2, Long cost) {
        adjacencyList.computeIfAbsent(p1, k -> new ArrayList<>());
        adjacencyList.computeIfAbsent(p2, k -> new ArrayList<>());
        adjacencyList.get(p1).add(new Pair<>(p2, cost));
        adjacencyList.get(p2).add(new Pair<>(p1, cost));
    }
}
