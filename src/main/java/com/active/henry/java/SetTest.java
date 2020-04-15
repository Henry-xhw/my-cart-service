package com.active.henry.java;

import java.util.HashSet;
import java.util.Set;

public class SetTest {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("d");
        set.remove("a");
        for (String str : set) {
            System.out.println(str);
        }
    }


}
