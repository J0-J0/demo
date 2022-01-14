
package com.jojo.zzz;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    private static final Logger logger = LoggerFactory.getLogger(Work.class);

    static {
        val = 2;
    }

    private static int val = 1;


    public static void main(String[] args) throws Exception {
        Map<String, Integer> map = Maps.newHashMap();
        map.put("1", 2);
        map.put("1", 2);
        map.put("2", 2);

        Map<String, Integer> newMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            newMap.compute(entry.getKey(), (key, val) -> {
                System.out.println(key + " " + val);
                return val == null ? entry.getValue() : val + entry.getValue();
            });
        }

        System.out.println(newMap);
    }


    public String longestWord(String[] words) {
        Map<Integer, List<String>> map = new HashMap();
        for (String s : words) {
            List<String> list = map.get(s.length()) == null ? new ArrayList() : map.get(s.length());
            list.add(s);
            map.put(s.length(), list);
        }
        List<Integer> keyList = new ArrayList<>();
        keyList.addAll(map.keySet());
        if (keyList.size() == 1)
            return null;
        keyList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return 01 - o2;
            }
        });


        List<String> lastVaildStringList;
        List<String> currentVaildStringList = map.get(1);
        if (currentVaildStringList == null)
            return "";
        List<String> nextVaildStringList = new ArrayList<>();

        for (int i = 2; i <= keyList.size(); i++) {//长度为2 下标应该是1
            List<String> paramList = map.get(i);
            if (paramList == null)
                break;
            nextVaildStringList.clear();
            lastVaildStringList = currentVaildStringList;
            for (String s : paramList) {
                String sub = s.substring(0, s.length() - 1);
                if (lastVaildStringList.contains(sub)) {
                    nextVaildStringList.add(s);
                }
            }
            if (nextVaildStringList.size() == 0)
                break;
            currentVaildStringList = new ArrayList<>(nextVaildStringList);

        }


        String minValue = currentVaildStringList.get(0);
        for (String s : currentVaildStringList) {
            if (s.compareTo(minValue) < 0)
                minValue = s;
        }
        return minValue;


    }

}