package com.before_final;

import java.util.*;
import java.util.concurrent.*;

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
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        service.schedule(() -> {
            return "dcl";
        }, 100, TimeUnit.SECONDS);

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
