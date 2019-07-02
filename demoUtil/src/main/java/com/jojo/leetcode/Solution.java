package com.jojo.leetcode;

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

    public static void main(String[] args) {
        Solution solution = new Solution();
        String str = solution.convertToTitle(2147483647);
        System.out.println(str);
    }

}