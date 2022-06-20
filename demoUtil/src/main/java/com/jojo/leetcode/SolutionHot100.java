package com.jojo.leetcode;

import com.alibaba.fastjson.JSON;
import com.jojo.leetcode.node.ListNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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


    public int[] h34searchRange(int[] nums, int target) {
        int start = -1, end = -1;
        if (nums.length == 0) {
            return new int[]{start, end};
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                start = i;
                i++;
                while (i < nums.length && nums[i] == target) {
                    i++;
                }
                end = i - 1;
                break;
            }
        }

        return new int[]{start, end};
    }


    public List<List<Integer>> h39combinationSum(int[] candidates, int target) {
        h39dfs(candidates, 0, target, new ArrayList<>());
        return h39answer;
    }

    private List<List<Integer>> h39answer = new ArrayList<>();

    private void h39dfs(int[] candidates, int i, int target, List<Integer> combine) {
        if (i == candidates.length) {
            return;
        }
        if (target == 0) {
            h39answer.add(combine);
            return;
        }

        h39dfs(candidates, i + 1, target, new ArrayList<>(combine));

        if (target - candidates[i] >= 0) {
            combine.add(candidates[i]);
            h39dfs(candidates, i, target - candidates[i], new ArrayList<>(combine));
        }
    }


    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> answer = new ArrayList<>();
        ArrayList<Integer> numList = new ArrayList<>();
        for (int num : nums) {
            numList.add(num);
        }
        List<Integer> combine = new ArrayList<>();

        h46dfs(numList, combine, answer);
        return answer;
    }

    private void h46dfs(ArrayList<Integer> numList, List<Integer> combine, List<List<Integer>> answer) {
        if (numList.size() == 0) {
            answer.add(combine);
            return;
        }

        for (int i = 0; i < numList.size(); i++) {
            Integer temp = numList.get(i);
            combine.add(temp);
            numList.remove(temp);

            h46dfs(numList, new ArrayList<>(combine), answer);

            numList.add(i, temp);
            combine.remove(temp);
        }
    }

    /**
     * 不准新建再复制，新建一个再复制，会简单吗？
     *
     * @param matrix
     */
    public void h48rotate(int[][] matrix) {
        int n = matrix.length;
        int layerCount = (n + 1) / 2;

        int[][] matrixCopy = new int[n][n];
        for (int layer = 0; layer < layerCount; layer++) {// 决定层数，从最外层往里遍历
            int start = layer, end = n - layer - 1;
            for (; start < end; start++) {// 每一层的旋转
                // matrixCopy 右，matrix 上
                matrixCopy[start][end] = matrix[layer][start];

                // martrixCopy 下，matrix 右
                matrixCopy[end][n - start - 1] = matrix[start][end];

                // martrixCopy 左，matrix 下
                matrixCopy[n - start - 1][layer] = matrix[end][n - start - 1];

                // martrixCopy 上，matrix 左
                matrixCopy[layer][start] = matrix[n - start - 1][layer];
            }
            if ((layer + 1 == layerCount) && (n % 2 != 0)) {
                matrixCopy[start][start] = matrix[start][start];
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = matrixCopy[i][j];
            }
        }
    }

    public List<List<String>> h49groupAnagrams(String[] strs) {
        List<List<String>> answer = new ArrayList<>();

        Map<String, List<String>> strListMap = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);

            List<String> list = strListMap.getOrDefault(key, new ArrayList<>());
            list.add(str);
            strListMap.put(key, list);
        }

        answer.addAll(strListMap.values());
        return answer;
    }

    public boolean h55canJump(int[] nums) {
        if (nums == null || nums.length == 0 || nums.length == 1) {
            return true;
        }

        int distance = 0;
        for (int i = nums.length - 1; i > -1; i--) {
            if (i - 1 > -1) {
                distance++;
                if (nums[i - 1] >= distance) {
                    distance = 0;
                    continue;
                }
            }
        }
        return distance == 0;
    }

    /**
     * 好是好，超时了
     *
     * @param nums
     * @param curPos
     * @return
     */
    @Deprecated
    private boolean h55dfs(int[] nums, int curPos) {
        if (curPos == nums.length - 1) {
            return true;
        } else if (curPos > nums.length - 1) {
            return false;
        }

        int moveCount = nums[curPos];
        if (moveCount == 0) {
            return false;
        }

        boolean answer = false;
        for (int i = 1; i <= moveCount; i++) {
            answer |= h55dfs(nums, curPos + i);
        }

        return answer;
    }

    public int[][] h56merge(int[][] intervals) {
        if (intervals.length == 1) {
            return intervals;
        }
        // 给区间排序
        Arrays.sort(intervals, Comparator.comparingInt(interval -> interval[0]));

        // 排序后的区间，合并
        List<int[]> list = new ArrayList<>();
        int[] curInterval = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            // 判断是否有交集
            if (curInterval[1] >= intervals[i][0]) {
                curInterval[1] = Math.max(curInterval[1], intervals[i][1]);
            } else {
                list.add(curInterval);
                curInterval = intervals[i];
            }
        }
        list.add(curInterval);

        int[][] answer = new int[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            answer[i] = list.get(i);
        }

        return answer;
    }

    public int h62uniquePaths(int m, int n) {
        return h62dfs(0, 0, m - 1, n - 1);
    }

    private int h62dfs(int x, int y, int m, int n) {
        if (x == m && y == n) {
            return 1;
        }

        if (x > m || y > n) {
            return 0;
        }

        int count = h62dfs(x + 1, y, m, n);
        count += h62dfs(x, y + 1, m, n);

        return count;
    }

    public static void main(String[] args) {
        int[] nums = {8, 2, 4, 4, 4, 9,};
        int[][] matrix = new int[][]{{1, 3}, {8, 10}, {15, 18}, {2, 6},};

        SolutionHot100 solution = new SolutionHot100();
        System.out.println(JSON.toJSONString(solution.h62uniquePaths(19, 13)));
    }

}
