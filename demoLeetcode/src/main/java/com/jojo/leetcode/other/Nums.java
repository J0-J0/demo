package com.jojo.leetcode.other;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Nums {

    /**
     * 打印二维数组
     *
     * @param matrix
     */
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(" " + matrix[i][j]);
            }
            System.out.println();
        }
    }


    /**
     * 打印整数数组全部的排列
     *
     * @param nums
     */
    public static void printAllPermutation(int[] nums) {
        List<String> answerList = new ArrayList<>();
        LinkedList<String> permutationList = generateAllPermutation(nums);

        for (String permutation : permutationList) {
            String temp = permutation.charAt(0) + "";
            for (int i = 1; i < permutation.length(); i++) {
                temp += ("," + permutation.charAt(i));
            }
            answerList.add(temp);
            System.out.println(temp);
        }

//        System.out.println(answerList);
    }

    private static LinkedList<String> generateAllPermutation(int[] nums) {
        LinkedList<String> answerList = new LinkedList<>();
        for (int i = nums.length - 1; i >= 0; i--) {
            // 第一个数字
            if (answerList.size() == 0) {
                answerList.offer(nums[i] + "");
                continue;
            }

            // 开始循环
            LinkedList<String> tempQueue = new LinkedList<>();// 准备本次循环的容器
            while (answerList.size() != 0) {
                String currentPermutation = answerList.poll();
                tempQueue.offer(nums[i] + currentPermutation);// 当前字符插在头
                for (int j = 1; j < currentPermutation.length(); j++) {// 当前字符插在中间
                    tempQueue.offer(currentPermutation.substring(0, j) + nums[i] + currentPermutation.substring(j));
                }
                tempQueue.offer(currentPermutation + nums[i]);// 当前字符插在尾
            }
            answerList.addAll(tempQueue);
        }
        return answerList;
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4};

        printAllPermutation(nums);
    }

}
