package com.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchUtils {
    public static int binarySearch(List<Integer> list, int val) {
        if (list == null || list.size() == 0) {
            return -1;
        }
        int low = 0, high = list.size()-1;
        while(low <= high) {
            int mid  = (low+high)/2;
            if (list.get(mid) == val) {
                return mid;
            } else if (list.get(mid) < val) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    private static void fun(int ind, String s, String cur, List<String> ans) {
        if (ind >= s.length()) {
            ans.add(cur);
            return;
        }
        fun(ind+1,s,cur,ans);
        fun(ind+1,s, cur + s.charAt(ind),ans);
        StringBuffer sb = new StringBuffer();
        sb.append(s.charAt(ind));
    }

    public static List<String> generate(String s) {
        List<String> ans = new ArrayList<String>();
        fun(0, s, "", ans);
        Collections.sort(ans);
        return ans;
    }
}
