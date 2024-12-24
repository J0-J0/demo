package com.jojo.leetcode.other;

public class L303NumArray {

    private int[] nums;

    private int tempLeft;
    private int tempRight;
    private int tempSum;

    public L303NumArray(int[] nums) {
        this.nums = nums;
        this.tempLeft = 0;
        this.tempRight = nums.length - 1;
        for (int i = 0; i < nums.length; i++) {
            tempSum += nums[i];
        }
    }

    public int sumRange(int left, int right) {
        if (nums == null && nums.length == 0) {
            return 0;
        }

        if (left == right) {
            return nums[left];
        }

        // 处理左边
        if (left < tempLeft) {
            for (int i = left; i < tempLeft; i++) {
                tempSum += nums[i];
            }
        } else if (left > tempLeft) {
            for (int i = tempLeft; i < left; i++) {
                tempSum -= nums[i];
            }
        }

        // 处理右边
        if (right < tempRight) {
            for (int i = tempRight; i > right; i--) {
                tempSum -= nums[i];
            }
        } else if (right > tempRight) {
            for (int i = right; i > tempRight; i--) {
                tempSum += nums[i];
            }
        }

        this.tempLeft = left;
        this.tempRight = right;
        return tempSum;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{-2, 0, 3, -5, 2, -1};
        L303NumArray solution = new L303NumArray(nums);
        System.out.println(solution.sumRange(0,2));
        System.out.println(solution.sumRange(2,5));
    }
}
