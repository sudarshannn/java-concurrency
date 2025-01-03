package com.before_final;

// https://leetcode.ca/2019-04-25-1242-Web-Crawler-Multithreaded/
// go to last
import java.util.*;

public class Webcrawler {
    private String startUrl;
    private Set<String> visited;


    public Webcrawler(String url) {
        startUrl = url;
        visited = Collections.synchronizedSet(new HashSet<>());
    }

    public List<String> startCrawling() {
        crawlDfs(startUrl);
        return new ArrayList<>(visited);
    }

    private void crawlDfs(String url) {
        visited.add(url);
        List<Thread> threads = new ArrayList<>();
        for (String relatedUrl : getRelatedUrls(url)) {
            if (!visited.contains(relatedUrl)) {
                visited.add(relatedUrl);
                threads.add(new Thread(() -> {
                    crawlDfs(url);
                }));
            }
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (Exception e) {
                // handling
            }
        }
    }

    // helper
    private List<String> getRelatedUrls(String url)  {
        // fetchAll and filter
        return Collections.emptyList();

    }
}
