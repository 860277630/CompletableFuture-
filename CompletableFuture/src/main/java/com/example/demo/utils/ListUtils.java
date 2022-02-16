package com.example.demo.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    //获得从大到小顺序的  list
    public static List<Integer> AscList(int num) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            result.add(i);
        }
        return result;
    }

    //获得随机顺序的list
    public static List<Integer> randomList(int num) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            result.add((int) (Math.random() * 1000));
        }
        return result;
    }
}
