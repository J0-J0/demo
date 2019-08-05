package com.jojo.zzz;

import com.google.common.collect.Lists;

import java.util.List;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {

    public static void main(String[] args) {
        List<Integer> list = Lists.newArrayList(1,2,3,4,5,6,7);
        List<List<Integer>> listList = Lists.partition(list, 2);
        for (List<Integer> integers : listList) {
            System.out.println(integers);
        }
    }
}

