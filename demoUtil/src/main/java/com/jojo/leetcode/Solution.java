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
            if (sameNameTransactionCity.equals(transactionCity)){
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

    public static void main(String[] args) {
        Solution solution = new Solution();

        String[] transactionArr = {"bob,689,1910,barcelona","alex,696,122,bangkok","bob,832,1726,barcelona","bob,820,596,bangkok","chalicefy,217,669,barcelona","bob,175,221,amsterdam"};
        List<String> str = solution.invalidTransactions(transactionArr);

        System.out.println(Arrays.toString(transactionArr));
        System.out.println(str);
    }

}