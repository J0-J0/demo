package com.jojo.leetcode.node;

import com.google.common.collect.Lists;

import java.util.List;

public class ListNode {

    public int val;

    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
        this.next = null;
    }

    public void print() {
        List<Integer> valList = Lists.newArrayList();
        valList.add(val);

        ListNode nextNode = next;
        while (nextNode != null) {
            valList.add(nextNode.val);

            nextNode = nextNode.next;
        }

        System.out.println(valList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(val + "");
        while (next != null) {
            sb.append("->").append(next.val);
            next = next.next;
        }
        return sb.toString();
    }

    public static ListNode buildListNode(int[] nums) {
        ListNode result = new ListNode();

        ListNode temp = result;
        for (int i = 0; i < nums.length; i++) {
            temp.val = nums[i];

            if (i != nums.length - 1) {
                temp.next = new ListNode();
                temp = temp.next;
            }
        }

        return result;
    }


    public static void main(String[] args) {
        ListNode node = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        node.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        node.print();
        node2.print();
        node3.print();
    }
}
