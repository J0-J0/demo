package com.jojo.leetcode;

import com.jojo.leetcode.node.ListNode;

import java.util.HashMap;
import java.util.Map;

public class SolutionHot100 {

    public int[] h1twoSum(int[] nums, int target) {
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

    public ListNode h2addTwoNumbers(ListNode l1, ListNode l2) {
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

    public static void main(String[] args) {
        int[] nums = {3, 3};
        ListNode listNode = ListNode.buildListNode(new int[]{2,4,3});
        ListNode listNode2 = ListNode.buildListNode(new int[]{5,6,4});

        SolutionHot100 solution = new SolutionHot100();

        System.out.println(solution.h2addTwoNumbers(listNode, listNode2));
    }

}
