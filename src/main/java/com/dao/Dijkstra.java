package com.dao;

import java.util.*;

public class Dijkstra {
    private List<Person> persons;
    private Map<Person, Set<Person>> adjacencyList;
    private Set<Person> visited;

    public Dijkstra() {
        adjacencyList = new HashMap<>();
        visited = new HashSet<>();
    }

    public Dijkstra(List<Person> persons) {
        if (persons == null) {
            throw new IllegalArgumentException("persons cannot be null");
        }
        this.persons = persons;
        adjacencyList = new HashMap<>();
        for(Person person : persons) {
            if (person.getFriends() == null) continue;
            for(Person friend : person.getFriends()) {
                addFriend(person, friend);
            }
        }
        visited = new HashSet<>();
    }

    public void dfs() {
        traverse(persons.get(0));
    }

    public void dfs(Person p) {
        traverse(p);
    }

    private void traverse(Person p) {
        System.out.println("dfs for " + p);
        visited.add(p);
        for(Person friend : adjacencyList.get(p)) {
            if (!visited.contains(friend)) {
                traverse(friend);
            }
        }
    }

    public void addFriend(Person p1, Person p2) {
        adjacencyList.computeIfAbsent(p1, k -> new HashSet<>());
        adjacencyList.computeIfAbsent(p2, k -> new HashSet<>());
        adjacencyList.get(p1).add(p2);
        adjacencyList.get(p2).add(p1);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("adjacencyList ").append(adjacencyList);
        return sb.toString();
    }
}
