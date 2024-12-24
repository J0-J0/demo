package com.jojo.leetcode.node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public List<TreeNode> children;

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


    public static void printBinaryTree(TreeNode root) {
        if (root == null) {
            return;
        }

        int depth = findDepth(root);

        List<TreeNode> printNodeValList = new ArrayList<>();
        printNodeValList.add(root);

        int level = 1;
        while (isTreeListNotEmpty(printNodeValList)) {
            StringBuffer sb = new StringBuffer();
            sb.append(getBlankSpace((int) (Math.pow(2, depth - level))));
            String middleSpace = "";
            if (level != 1) {
                middleSpace = getBlankSpace((int) (Math.pow(2, depth - level + 1)) - 1);
            }

            List<TreeNode> tempList = new ArrayList<>();

            for (int i = 0; i < printNodeValList.size(); i++) {
                TreeNode node = printNodeValList.get(i);
                if (node == null) {
                    sb.append(" ");
                    tempList.add(null);
                    tempList.add(null);
                } else {
                    sb.append(node.val);
                    tempList.add(node.left);
                    tempList.add(node.right);
                }
                sb.append(middleSpace);
            }
            System.out.println(sb);
            printNodeValList.clear();
            printNodeValList.addAll(tempList);
            level++;
        }
    }

    private static boolean isTreeListNotEmpty(List<TreeNode> list) {
        boolean result = false;
        for (TreeNode treeNode : list) {
            result |= (treeNode != null);
        }
        return result;
    }

    private static String getBlankSpace(int count) {
        String standBlankSpace = " ";

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            sb.append(standBlankSpace);
        }
        return sb.toString();
    }

    public static int findDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        return Math.max(1 + findDepth(root.left), 1 + findDepth(root.right));
    }

    public static void main(String[] args) {

    }
}
