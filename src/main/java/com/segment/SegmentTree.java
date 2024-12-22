package com.segment;

import java.util.ArrayList;
import java.util.List;

public class SegmentTree {
    Integer size;
    List<Integer> sums;

    public SegmentTree(int n) {
        size = 1;
        sums = new ArrayList<>();
        while(size<n) {
            size*=2;
        }
        for(int i=0;i<(2*size);i++) {
            sums.add(0);
        }
    }

    private void set(int ind, int val ,int x, int lx, int rx) {
        if (rx-lx == 1) {
            sums.set(x, val);
            return;
        }
        int mid = (lx+rx)/2;
        if (ind < mid) {
            set(ind, val, (2*x)+1, lx, mid);
        } else {
            set(ind, val, (2*x)+2, mid, rx);
        }
        sums.set(x, sums.get((2*x)+1) + sums.get((2*x)+2));
    }

    public void set(int ind, int val) {
        set(ind, val, 0, 0, size);
    }

    private int query(int l, int r, int x, int lx, int rx) {
        if (l<=lx && rx<=r) {
            return sums.get(x);
        }
        if (rx<=l || r<=lx) {
            return 0;
        }
        int mid = (lx+rx)/2;
        return query(l,r,(2*x)+1,lx, mid) + query(l,r,(2*x)+2,mid, rx);
    }

    public int query(int l, int r) {
        return query(l,r, 0, 0, size);
    }
}
