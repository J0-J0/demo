package com.jojo.leetcode;

import com.jojo.leetcode.node.ListNode;

import java.util.HashMap;
import java.util.Map;

public class SolutionHot100 {

    public int[] h1L1twoSum(int[] nums, int target) {
        // 暴力做法，没什么意义
//        for (int i = 0; i < nums.length - 1; i++) {
//            for (int j = i + 1; j < nums.length; j++) {
//                int sum = nums[i] + nums[j];
//                if (sum == target) {
//                    return new int[]{i, j};
//                }
//            }
//        }
//        return new int[]{};

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int differ = target - nums[i];
            if (map.containsKey(differ)) {
                return new int[]{i, map.get(differ)};
            } else {
                map.put(nums[i], i);
            }
        }

        return new int[2];
    }

    public ListNode h2L2addTwoNumbers(ListNode l1, ListNode l2) {
        // 倒序，意味着，第一位是个位，第二位是十位，第三位是百位
        // while循环遍历链表，生成数字
        // while循环遍历sum，获得结果链表
        // 就是非常可以，这个想法是错的，会遇到int型以及long型的最大值！
//        ListNode result = new ListNode();
//        long sum = this.h2getIntVal(l1) + this.h2getIntVal(l2);
//
//        ListNode temp = result;
//        while (sum != 0) {
//            long remainder = sum % 10;
//            temp.val = (int) remainder;
//            sum /= 10l;
//            if (sum != 0) {
//                temp.next = new ListNode();
//                temp = temp.next;
//            }
//        }
//
//        return result;

        // 单while循环进位
        ListNode result = new ListNode();
        ListNode temp = result;
        int carry = 0;
        while (l1 != null || l2 != null) {
            int sum = carry;

            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }

            if (sum >= 10) {
                carry = 1;
                sum %= 10;
            } else {
                carry = 0;
            }
            temp.val = sum;

            if (l1 != null || l2 != null) {
                temp.next = new ListNode();
                temp = temp.next;
            }
        }
        if (carry != 0) {
            temp.next = new ListNode();
            temp.next.val = carry;
        }

        return result;
    }

    @Deprecated
    private long h2getIntVal(ListNode node) {
        long val = 0;
        for (int count = 0; node != null; node = node.next) {
            val += node.val * Math.pow(10, count++);
        }
        return val;
    }

    public int h3L3lengthOfLongestSubstring(String s) {
        // 成功，就是某种角度，这也算是暴力法
        int length = 0;
        int start = 0;
        for (int i = 1; i < s.length(); i++) {
            for (int j = start; j < i; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    length = Math.max(length, i - start);
                    start = j + 1;
                    break;
                }
            }
        }

        return Math.max(length, s.length() - start);
    }

    public double h4L4findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int length = m + n;

        // 归并，空间复杂度O(m+n)
        int[] mergedArr = new int[length];
        int count = 0, i = 0, j = 0;
        while (i < m && j < n) {
            if (nums1[i] <= nums2[j]) {
                mergedArr[count++] = nums1[i++];
            } else {
                mergedArr[count++] = nums2[j++];
            }
        }
        while (i < m) {
            mergedArr[count++] = nums1[i++];
        }
        while (j < n) {
            mergedArr[count++] = nums2[j++];
        }
        int middle = length / 2;
        return length % 2 != 0 ? mergedArr[middle] : ((double) mergedArr[middle - 1] + (double) mergedArr[middle]) / 2d;
    }

    public String h5L5longestPalindrome(String s) {
        int length = s.length();
        if (length < 2) {
            return s;
        }

        boolean[][] dp = new boolean[length][length];
        // 长度为1的子字符串，全都是回文串
        for (int i = 0; i < length; i++) {
            dp[i][i] = true;
        }
        // 进入大的循环
        int begin = 0, maxLength = 1;
        for (int len = 2; len <= length; len++) {
            for (int i = 0; i <= length - len; i++) {
                int j = i + len - 1;
                if (len == 2) {
                    dp[i][j] = (s.charAt(i) == s.charAt(j));
                } else {
                    dp[i][j] = (s.charAt(i) == s.charAt(j)) && dp[i + 1][j - 1];
                }

                if (dp[i][j]) {
                    if (len > maxLength) {
                        maxLength = len;
                        begin = i;
                    }
                }
            }
        }
        return s.substring(begin, begin + maxLength);
    }

    public boolean h6L10isMatch(String s, String p) {

        // 枚举可能性，但代码已经越写越复杂了，写出来估计也是错的，放弃看题解

//        int i = 0, j = 0;
//        int sLen = s.length(), pLen = p.length();
//        for (; i < sLen; i++, j++) {
//            if (i == pLen) {
//                return false;
//            }
//
//            if (p.charAt(j) == '*') {
//                while (i < sLen && (p.charAt(j - 1) == '.' || s.charAt(i) == p.charAt(j - 1))) {
//                    i++;
//                }
//                continue;
//            }
//
//            if (p.charAt(j) == '.' || s.charAt(i) == p.charAt(j)) {
//                continue;
//            }
//        }
//
//        if (j < pLen) {
//            if (p.charAt(j - 1) != '*') {
//                if ((p.charAt(j) <= 'z' && p.charAt(j) >= 'a') || p.charAt(j) == '.') {
//                    return false;
//                }
//                // 执行到此处，Pi只可能是 *，但还要判断长度
//                if (p.length() - j == 1) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//
//            // 如果说，前一个字符是 *
//            if (p.charAt(j) != '.' || p.charAt(j) != s.charAt(i - 1)) {
//                return false;
//            }
//
//            if (s.length() - j + 1 <= p.length() - j) {
//                return false;
//            }
//        }
//
//        return true;

        return false;
    }

    public int h7L11maxArea(int[] height) {
        int maxArea = 0;
        for (int i = 0, j = height.length - 1; i < j; ) {
            int area = (j - i) * Math.min(height[i], height[j]);
            maxArea = Math.max(area, maxArea);

            if (height[i] <= height[j]) {
                i++;
            } else {
                j--;
            }
        }
        return maxArea;
    }

    /**
     * 这个不是说代码有多难写，就是个思路问题，所以说，人和人的脑子，真的不一样
     *
     * @param nums
     */
    public void h31nextPermutation(int[] nums) {
        if (nums.length == 1) {
            return;
        }

        int i = nums.length - 2;
        for (; i >= 0; i--) {
            if (nums[i + 1] > nums[i]) {
                break;
            }
        }

        if (i < 0) {
            h31reverse(nums, 0);
            return;
        }

        int j = nums.length - 1;
        for (; j > i; j--) {
            if (nums[j] > nums[i]) {
                break;
            }
        }

        int temp = nums[j];
        nums[j] = nums[i];
        nums[i] = temp;

        h31reverse(nums, i + 1);
    }

    private void h31reverse(int[] nums, int start) {
        for (int end = nums.length - 1; start <= end; start++, end--) {
            int temp = nums[end];
            nums[end] = nums[start];
            nums[start] = temp;
        }
    }

    /**
     * 实在是理解不了题意
     *
     * @param nums
     * @param target
     * @return
     */
    public int h33search(int[] nums, int target) {
//        for (int i = 0; i < nums.length; i++) {
//            if (nums[i] == target) {
//                return i;
//            }
//        }
//        return -1;

        if (nums.length == 1) {
            return target == nums[0] ? 0 : -1;
        }

        if (target < nums[0]) {
            // 从尾往头遍历
            for (int i = nums.length - 1; i >= 0; i--) {
                if (nums[i] == target) {
                    return i;
                }
                if (nums[i - 1] > nums[i]) {
                    break;
                }
            }
        } else if (target > nums[0]) {
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] == target) {
                    return i;
                }
                if (nums[i] > nums[i + 1]) {
                    break;
                }
            }
        } else {
            return 0;
        }

        return -1;
    }

    public static void main(String[] args) {
        int[] nums = {4, 5, 6, 7, 0, 1, 2};

        SolutionHot100 solution = new SolutionHot100();

        System.out.println(solution.h33search(nums, 7));
    }

}
