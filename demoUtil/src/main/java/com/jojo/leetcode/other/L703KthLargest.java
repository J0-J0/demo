package com.jojo.leetcode.other;

import java.awt.font.NumericShaper;
import java.util.Arrays;

public class L703KthLargest {

    private int[] nums;
    private int k;

    public L703KthLargest(int k, int[] nums) {
        this.nums = nums;
        this.k = k;
        Arrays.sort(nums);
    }

    public int add(int val) {
        int[] newNums = new int[nums.length + 1];
        int pos = nums.length;
        for (int i = 0; i < nums.length; i++) {
            if (val <= nums[i]) {
                pos = i;
                break;
            }
        }

        if (pos > 0) {
            System.arraycopy(nums, 0, newNums, 0, pos);
        }
        newNums[pos] = val;
        if (pos < nums.length) {
            System.arraycopy(nums, pos, newNums, pos + 1, nums.length - pos);
        }

        nums = newNums;
        return nums[nums.length - k];
    }

    public static void main(String[] args) {
        L703KthLargest largest = new L703KthLargest(3, new int[]{4, 5, 8, 2});
        System.out.println(largest.add(3));
        System.out.println(largest.add(5));
        System.out.println(largest.add(10));
        System.out.println(largest.add(9));
        System.out.println(largest.add(4));
    }
}
