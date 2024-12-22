package com;

import com.Utils.SearchUtils;
import com.dao.Account;
import com.segment.SegmentTree;

import java.util.Comparator;
import java.util.PriorityQueue;

public class HelloWorld {
    public static void main(String[] args) {
        SegmentTree segmentTree = new SegmentTree(10);
        // 0 1 2 3 4 5 6 7 8 9
        for(int i=0;i<10;i++) {
            segmentTree.set(i, i);
        }
        System.out.println(segmentTree.query(7,10));

        PriorityQueue<Account> pq = new PriorityQueue(new Comparator<Account>() {
            @Override
            public int compare(Account a, Account b) {
                if (b.getName().compareTo(a.getName()) == 0) {
                    return b.getAccountId().compareTo(a.getAccountId());
                } else {
                    return b.getName().compareTo(a.getName());
                }
            }
        });
        pq.add(new Account("a", 5));
        pq.add(new Account("a", 2));
        pq.add(new Account("cz", 3));
        pq.add(new Account("ca", 4));

        while(!pq.isEmpty()) {
            Account user = pq.poll();
            pq.size();
            System.out.println(user);
        }
        System.out.println(SearchUtils.generate("abc"));
    }
}
