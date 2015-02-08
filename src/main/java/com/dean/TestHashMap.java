package com.dean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dean on 15/1/12.
 */
public class TestHashMap {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        System.out.println(map.get("key"));
    }
}
