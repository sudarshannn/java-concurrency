package com;

import java.net.URL;

public class UrlTest {
    public static void main(String[] args) throws Exception {
        String s = "https://leetcode.ca/2019-04-25-1242-Web-Crawler-Multithreaded/";

        int ind = s.indexOf('/', 8);


        System.out.println(s.substring(0, ind));

        URL url = new URL(s);
        System.out.println(url.getHost());
    }
}
