package com;

import java.util.Map;

public class SprThread extends Thread {
    public static void main(String[] args) {
        Map<String,String> mp = Map.of("DOCUMENT_CLASS", "INTENT_ID", "DOCUMENT_CONTENT", "EXPRESSIONS");
        System.out.println(mp);
    }
}
