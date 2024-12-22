package com.dao;

import java.util.Comparator;
import java.util.Objects;

public class User implements Comparator<User>, Comparable<User> {
    private String name;
    private Integer age;

    public User() {

    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append("user=").append(this.name).append(", age=").append(this.age).toString();
    }

    @Override
    public int compare(User o1, User o2) {
        if (o1.getName().compareTo(o2.getName()) == 0) {
            return Integer.compare(o1.getAge(), o2.getAge());
        } else {
            return o1.getName().compareTo(o2.getName());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        if (this.name.equals(((User) obj).name)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.age);
    }

    @Override
    public int compareTo(User that) {
        if (this.getName().compareTo(that.getName()) == 0) {
            return Integer.compare(this.getAge(), that.getAge());
        } else {
            return this.getName().compareTo(that.getName());
        }
    }
}
