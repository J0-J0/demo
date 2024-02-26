package com.jojo.leetcode;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionSecondRound {

    /**
     * 方法一，但是，嵌套循环很大概率死于时间不足
     */
    public int[] l1twoSum1(int[] nums, int target) {
        if (nums == null || nums.length == 1) {
            return new int[]{};
        }

        if (nums.length == 2 && nums[0] + nums[1] == target) {
            return new int[]{0, 1};
        }


        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * 方法二，时间换空间，但是内存占用太高，而且借用了java的集合特性
     */
    public int[] l1twoSum2(int[] nums, int target) {
        if (nums == null || nums.length == 1) {
            return new int[]{};
        }

        if (nums.length == 2 && nums[0] + nums[1] == target) {
            return new int[]{0, 1};
        }

        Map<Integer, List<Integer>> valSeqMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            List<Integer> seqList;
            if (valSeqMap.get(nums[i]) == null) {
                seqList = new ArrayList<>();
            } else {
                seqList = valSeqMap.get(nums[i]);
            }
            seqList.add(i);
            valSeqMap.put(nums[i], seqList);
        }

        for (int i = 0; i < nums.length; i++) {
            List<Integer> seqList = valSeqMap.get(target - nums[i]);
            if (seqList == null) {
                continue;
            }
            seqList.remove(new Integer(i));
            if (seqList.size() == 0) {
                continue;
            }

            return new int[]{i, seqList.get(0)};
        }

        return null;
    }

    /**
     * 没有方法三，全部答案我都想到了，就是我写的代码，不够简洁，考虑补够全面，导致性能上稍差
     */
    public int[] l1twoSum3(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                int temp = map.get(target - nums[i]);
                return new int[]{i, temp};
            } else {
                map.put(nums[i], i);
            }
        }
        return new int[]{-1, -1};
    }



    public boolean l9isPalindrome(int x) {
        String xStr = x + "";
        for (int i = 0, j = xStr.length() - 1; i < xStr.length() && j > i; i++,j--) {
            char ci = xStr.charAt(i);
            char cj = xStr.charAt(j);
            if (ci != cj) {
                return false;
            }
        }
        return true;
    }








    public static void main(String[] args) {
        SolutionSecondRound solution = new SolutionSecondRound();

        int[] nums = {2, 5, 5, 11};
        int target = 10;


        System.out.println(JSON.toJSONString(solution.l9isPalindrome(10)));
    }

}
