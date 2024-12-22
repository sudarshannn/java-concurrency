package com.dao;

import com.Utils.SearchUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SprBinarySearch {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(2);
        list.add(5);
        list.add(6);

        Collections.sort(list);
        int ind = SearchUtils.binarySearch(list, 2);
        System.out.println(ind);

    }
}
