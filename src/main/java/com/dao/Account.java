package com.dao;

public class Account implements Comparable<Account> {
    private String name;
    private Integer accountId;

    public Account(String name, Integer accountId) {
        this.name = name;
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Override
    public int compareTo(Account that) {
        if (this.name.compareTo(that.name) == 0) {
            return this.accountId.compareTo(that.accountId);
        } else {
            return this.name.compareTo(that.name);
        }
    }


    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("name=").append(this.name).append(", accountId=").append(this.accountId);
        return build.toString();
    }
}
