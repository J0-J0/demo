package com.jojo.leetcode;

import java.util.LinkedList;
import java.util.List;

public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    List<TreeNode> children;

    TreeNode(int x) {
        val = x;
    }

    public static TreeNode breadthFill(Integer[] arr) {
        TreeNode root = new TreeNode(arr[0]);

        LinkedList<TreeNode> readyToFillList = new LinkedList<>();
        readyToFillList.add(root);

        for (int i = 0, j = 1; i < readyToFillList.size(); i++) {
            TreeNode node = readyToFillList.poll();
            if (j >= arr.length) {
                break;
            }
            node.left = new TreeNode(arr[j++]);
            readyToFillList.offer(node.left);
            if (j >= arr.length) {
                break;
            }
            node.right = new TreeNode(arr[j++]);
            readyToFillList.offer(node.right);
        }
        return root;
    }

    public static void main(String[] args) {
    }
}
