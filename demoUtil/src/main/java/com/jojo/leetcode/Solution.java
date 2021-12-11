package com.jojo.leetcode;

import java.util.*;

class Solution {

    /**
     * 168
     * 这种感觉很奇怪，我倒是知道思路是什么，却不懂怎么把代码写出来。
     * 把计算机当成人的话，那他一定是个傻子，所以写代码就是教傻子怎么做数学题？
     * 每一次，n - mod，重新除。
     *
     * @param n
     * @return
     */
    private String convertToTitle(int n) {
        String temp = "";
        while (n > 0) {
            char s = (char) ((n - 1) % 26 + 'A');
            temp = s + temp;
            n = (n - 1) / 26;
        }
        return temp;
    }

    /**
     * 1169
     *
     * @param transactions
     * @return
     */
    public List<String> invalidTransactions(String[] transactions) {
        List<String> invalidTransactionList = new ArrayList<>();

        if (transactions == null || transactions.length == 0) {
            return invalidTransactionList;
        }

        List<String> transactionList = new ArrayList<>(Arrays.asList(transactions));
        Map<String, String> map = new HashMap<>();
        for (Iterator<String> iterator = transactionList.iterator(); iterator.hasNext(); ) {
            String transaction = iterator.next();
            String[] arr = transaction.split(",");
            int amount = Integer.parseInt(arr[2]);
            String transactionName = arr[0];
            String transactionCity = arr[3];
            int time = Integer.parseInt(arr[1]);

            // 去除大于1000的
            if (amount > 1000) {
                invalidTransactionList.add(transaction);
                iterator.remove();
                continue;
            }

            String sameNameTransaction = map.get(transactionName);
            if (sameNameTransaction == null) {
                map.put(transactionName, transaction);
                continue;
            }

            String sameNameTransactionCity = sameNameTransaction.split(",")[3];
            if (sameNameTransactionCity.equals(transactionCity)) {
                continue;
            }

            // 去除同名交易不同城市，且间隔小于60分钟的交易
            int sameCityTransactionTime = Integer.parseInt(sameNameTransaction.split(",")[1]);
            int differ = sameCityTransactionTime - time;
            if (differ <= 60 && differ >= -60) {
                invalidTransactionList.add(sameNameTransaction);
                invalidTransactionList.add(transaction);
            }
        }

        return invalidTransactionList;
    }

    public boolean canPartition(int[] nums) {
        int sumArr = 0, length = nums.length;
        if (length == 1) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            sumArr += nums[i];
        }

        if (sumArr % 2 != 0) {
            return false;
        }

        int expectedVal = sumArr / 2;
        Arrays.sort(nums);
        for (int i = 0; i < length; i++) {
            int sumA = 0;
            for (int j = i; j < length; j++) {
                sumA += nums[j];
                if (sumA == expectedVal) {
                    return true;
                }
                if (sumA > expectedVal) {
                    break;
                }
            }
        }
        return false;
    }


    public int knapsackProblem() {
        int v = 4;// 最大容积
        int[] weight = {1, 4, 3, 1};
        int[] value = {2000, 3000, 2000, 1500};

        int[][] maxVal = new int[weight.length + 1][v + 1];

        // 赋值
        for (int i = 0; i < maxVal.length; i++) {
            for (int j = 0; j < maxVal[i].length; j++) {
                maxVal[i][j] = 0;
            }
        }

        // 找出最大值
        for (int i = 1; i < maxVal.length; i++) {
            for (int j = 1; j < maxVal[i].length; j++) {
                int currentGoodsWeight = weight[i - 1];
                int currentGoodsValue = value[i - 1];
                int lastMaxVal = maxVal[i - 1][j];

                maxVal[i][j] = lastMaxVal;// 先等于上一次最大值

                if (currentGoodsWeight > j) {
                    continue;
                }

                if (currentGoodsWeight == j && currentGoodsValue >= lastMaxVal) {
                    maxVal[i][j] = currentGoodsValue;
                    continue;
                }

                if (currentGoodsValue + maxVal[i - 1][j - currentGoodsWeight] >= lastMaxVal) {
                    maxVal[i][j] = currentGoodsValue + maxVal[i - 1][j - currentGoodsWeight];
                }
            }
        }

        for (int i = 0; i < maxVal.length; i++) {
            for (int j = 0; j < maxVal[i].length; j++) {
                System.out.printf("%6d", maxVal[i][j]);
            }
            System.out.println();
        }

        return maxVal[weight.length][v];
    }

    public static int toResverseBinaryIntValue(int n) {
        System.out.println(Integer.toBinaryString(n));
        StringBuilder resverseBinaryString = new StringBuilder();
        int differ = n / 2;
        resverseBinaryString.append(n % 2);
        while (differ != 0) {
            resverseBinaryString.append(differ % 2);
            differ /= 2;
        }
        System.out.println(resverseBinaryString);
        int resverseValue = 0;
        int length = resverseBinaryString.length();
        for (int i = length - 1, j = 0; i > -1; i--, j++) {
            char ch = resverseBinaryString.charAt(i);
            resverseValue += Math.pow(2, j);
        }
        // 这里反转
        return resverseValue;
    }

    /**
     * 反转字符串
     *
     * @param str
     * @return
     */
    public String reverseStr(String str) {
        // 递归实现
//        if (str == null || str.length() == 0) {
//            return "";
//        }
//        int startIndex = str.length() - 1;
//        return str.substring(startIndex) + reverseStr(str.substring(0, startIndex));


        // 数组反转
//        if (str == null || str.length() == 0) {
//            return "";
//        }
//        char[] chars = str.toCharArray();
//        int length = chars.length;
//        String resultStr = "";
//        for (int i = length - 1; i >= 0; i--) {
//            resultStr += chars[i];
//        }
//        return resultStr;

        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 203
     *
     * @param head
     * @param val
     * @return
     */
    public ListNode removeElements(ListNode head, int val) {
        if (head == null) {
            return head;
        }
        head.next = removeElements(head.next, val);
        return head.val == val ? head.next : head;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node2 = new ListNode(1);
        ListNode node3 = new ListNode(1);
        ListNode node4 = new ListNode(1);
        ListNode node5 = new ListNode(1);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;


        Solution solution = new Solution();
        head = solution.removeElements(head, 1);

        if (head == null) {
            return;
        }
        head.print();
    }

}