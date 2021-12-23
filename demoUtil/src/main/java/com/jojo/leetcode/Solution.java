package com.jojo.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public String l168ConvertToTitle(int n) {
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
    public List<String> l1169InvalidTransactions(String[] transactions) {
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

    public int l190ReverseBits(int n) {
//        return Integer.reverse(n);

//        int result = 0;
//        System.out.println("n="+Integer.toBinaryString(n));
//        for (int i = 0; i < 32 && n!=0; i++) {
//            int temp = n & 1;
//            System.out.println("temp="+Integer.toBinaryString(temp));
//            result |= (temp << (31 - i));
//            System.out.println("result="+Integer.toBinaryString(result));
//            n >>>= 1;
//        }
//
//        return result;

        int rev = 0;
        System.out.println("n=" + Integer.toBinaryString(n));
        for (int i = 0; i < 32 && n != 0; ++i) {
            rev |= (n & 1) << (31 - i);
            n >>>= 1;
        }
        System.out.println("n=" + Integer.toBinaryString(rev));
        return rev;
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
    public ListNode l203RemoveElements(ListNode head, int val) {
        if (head == null) {
            return head;
        }
        head.next = l203RemoveElements(head.next, val);
        return head.val == val ? head.next : head;
    }

    /**
     * 434
     *
     * @param s
     * @return
     */
    public int l434CountSegments(String s) {
        if (s == null || "".equals(s)) {
            return 0;
        }

        char[] chars = s.toCharArray();
        int segmentsCount = 0;
        boolean firstChar = false;
        boolean hasChar = false;
        for (char aChar : chars) {
            if (aChar != 32) {
                firstChar = true;
                hasChar = true;
            }

            if (firstChar && aChar == 32) {
                segmentsCount += 1;
                firstChar = false;
            }
        }

        if (segmentsCount == 0 && hasChar) {
            ++segmentsCount;
        } else if (segmentsCount != 0 && chars[chars.length - 1] != 32) {
            ++segmentsCount;
        }

        return segmentsCount;
    }

    /**
     * 234
     *
     * @param head
     * @return
     */
    public boolean l234IsPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }

        StringBuffer sb = new StringBuffer();
        while (head != null) {
            sb.append(head.val);
            head = head.next;
        }

        StringBuffer newSb = new StringBuffer();
        for (int i = sb.length() - 1; i >= 0; i--) {
            newSb.append(sb.charAt(i));
        }

        return sb.toString().equals(newSb.toString());
    }

    /**
     * 605
     *
     * @param flowerbed
     * @param n
     * @return
     */
    public boolean l605CanPlaceFlowers(int[] flowerbed, int n) {
        // TODO
        return false;
    }

    /**
     * 268
     *
     * @param nums
     * @return
     */
    public int l268MissingNumber(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int result = 0;
        for (int i = 1; i < nums.length + 1; i++) {
            result += i;
        }

        for (int num : nums) {
            result -= num;
        }
        return result;
    }

    public List<String> l228SummaryRanges(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new ArrayList();
        }

        List<String> resultList = new ArrayList();

        if (nums.length == 1) {
            resultList.add(nums[0] + "");
            return resultList;
        }

        int prev = nums[0], prevPos = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1] + 1) {
                continue;
            }

            if (prevPos == i - 1) {
                resultList.add(prev + "");
            } else {
                resultList.add(prev + "->" + nums[i - 1]);
            }
            prev = nums[i];
            prevPos = i;
        }

        if (prevPos == nums.length - 1) {
            resultList.add(nums[prevPos] + "");
        } else {
            resultList.add(nums[prevPos] + "->" + nums[nums.length - 1]);
        }
        return resultList;
    }

    public int l495FindPoisonedDuration(int[] timeSeries, int duration) {
        if (duration == 0 || timeSeries == null || timeSeries.length == 0) {
            return 0;
        }

        if (timeSeries.length == 1) {
            return duration;
        }

        int result = 0, length = timeSeries.length;
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                result += duration;
                break;
            }

            int interval = timeSeries[i] + duration;
            if (interval < timeSeries[i + 1]) {
                result += duration;
            } else if (interval >= timeSeries[i + 1]) {
                result += timeSeries[i + 1] - timeSeries[i];
            }
        }
        return result;
    }

    public int l278FirstBadVersion(int n) {
        if (n == 1) {
            return isBadVersion(n) ? n : 0;
        }

        int left = 1, right = n;

        int firstBadVersion = left + (right - left) / 2;
        while (left < right) {
            if (left + 1 == right) {
                firstBadVersion = isBadVersion(left) ? left : right;
                break;
            }
            if (isBadVersion(firstBadVersion)) {
                right = firstBadVersion;
            } else {
                left = firstBadVersion;
            }
            firstBadVersion = left + (right - left) / 2;
        }

        return firstBadVersion;
    }

    private boolean isBadVersion(int version) {
        return version >= 1702766719;
    }


    public int l414ThirdMax(int[] nums) {
        if (nums == null) {
            return 0;
        }

        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            set.add(nums[i]);
        }

        int count = 0;
        nums = new int[set.size()];
        for (Integer i : set) {
            nums[count++] = i;
        }

        if (nums.length == 1) {
            return nums[0];
        }
        if (nums.length == 2) {
            return nums[0] > nums[1] ? nums[0] : nums[1];
        }

        Arrays.sort(nums);

        return nums[nums.length - 3];

//
//        int firstMax = nums[0];
//        for (int i = 1; i < nums.length; i++) {
//            firstMax = firstMax > nums[i] ? firstMax : nums[i];
//        }
//
//        int secondMax = nums[0];
//        for (int i = 1; i < nums.length; i++) {
//            int temp = secondMax;
//            secondMax = secondMax > nums[i] ? secondMax : nums[i];
//            if (secondMax >= firstMax) {
//                secondMax = temp;
//            }
//        }
//
//        int thirdMax = nums[0];
//        for (int i = 1; i < nums.length; i++) {
//            int temp = thirdMax;
//            thirdMax = thirdMax > nums[i] ? thirdMax : nums[i];
//            if (thirdMax >= secondMax) {
//                thirdMax = temp;
//            }
//        }
//
//        return thirdMax;
    }


    public int l1518NumWaterBottles(int numBottles, int numExchange) {
        if (numBottles == 0 || numExchange == 0) {
            return numBottles;
        }

        if (numBottles < numExchange) {
            return numBottles;
        }

        int nextRound = numBottles / numExchange;// 本轮结束后可以兑换多少瓶
        int numWaterBottles = numBottles + nextRound;// 共可以喝多少瓶
        int nextRoundIdle = numBottles % numExchange;

        while (nextRound != 0) {
            int total = nextRound + nextRoundIdle;
            nextRound = total / numExchange;
            nextRoundIdle = total % numExchange;

            numWaterBottles += nextRound;
        }

        return numWaterBottles;
    }


    public int l997FindJudge(int n, int[][] trust) {
        if (n == 0 || trust == null) {
            return -1;
        }
        if (n == 1) {
            return 1;
        }

        Map<Integer, int[]> trustCountMap = new HashMap<>();
        // int[0]，表示被信任次数，int[1]，信任人的次数
        for (int[] trustPair : trust) {
            if (trustCountMap.get(trustPair[0]) == null) {
                trustCountMap.put(trustPair[0], new int[]{0, 1});
            } else {
                trustCountMap.get(trustPair[0])[1] += 1;
            }
            if (trustCountMap.get(trustPair[1]) == null) {
                trustCountMap.put(trustPair[1], new int[]{1, 0});
            } else {
                trustCountMap.get(trustPair[1])[0] += 1;
            }
        }

        Set<Map.Entry<Integer, int[]>> entrySet = trustCountMap.entrySet();
        for (Map.Entry<Integer, int[]> entry : entrySet) {
            if (entry.getValue()[0] + 1 == n && entry.getValue()[1] == 0) {
                return entry.getKey();
            }
        }

        return -1;
    }

    private List<Integer> treeNodeValList = new ArrayList<>();

    /**
     * 中序遍历
     *
     * @param root
     * @return
     */
    public List<Integer> l94InorderTraversal(TreeNode root) {
        if (root == null) {
            return treeNodeValList;
        }

        l94InorderTraversal(root.left);
        treeNodeValList.add(root.val);
        l94InorderTraversal(root.right);

        return treeNodeValList;
    }

    /**
     * 后序遍历
     *
     * @param root
     * @return
     */
    public List<Integer> l145PostorderTraversal(TreeNode root) {
        if (root == null) {
            return treeNodeValList;
        }

        l145PostorderTraversal(root.left);
        l145PostorderTraversal(root.right);
        treeNodeValList.add(root.val);

        return treeNodeValList;
    }

    /**
     * 前序遍历
     *
     * @param root
     * @return
     */
    public List<Integer> l144PreorderTraversal(TreeNode root) {
        if (root == null) {
            return treeNodeValList;
        }

        treeNodeValList.add(root.val);

        l144PreorderTraversal(root.left);
        l144PreorderTraversal(root.right);

        return treeNodeValList;
    }

    public boolean l112HasPathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return false;
        }

        if (root.left == null && root.right == null) {
            return root.val == targetSum ? true : false;
        }

        boolean result = false;
        result |= l112HasPathSum(root.left, targetSum - root.val);
        result |= l112HasPathSum(root.right, targetSum - root.val);

        return result;
    }

    private List<List<Integer>> pathListList = new ArrayList<>();

    public List<List<Integer>> l113PathSum(TreeNode root, int targetSum) {
        List<Integer> onePathList = new ArrayList<>();

        return availablePathList(root, targetSum, onePathList);
    }

    private List<List<Integer>> availablePathList(TreeNode root, int targetSum, List<Integer> treeNodeValList) {
        List<Integer> resultList = new ArrayList<>();
        resultList.addAll(treeNodeValList);

        if (root == null) {
            return pathListList;
        }

        resultList.add(root.val);
        if (root.left == null && root.right == null) {
            if (root.val == targetSum) {
                pathListList.add(resultList);
                return pathListList;
            }
        }

        availablePathList(root.left, targetSum - root.val, resultList);
        availablePathList(root.right, targetSum - root.val, resultList);

        return pathListList;
    }

    public int pathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return 0;
        }

        int result = findAvailablePath(root, targetSum);

        int leftResult = pathSum(root.left, targetSum);
        int rightResult = pathSum(root.right, targetSum);

        return result + leftResult + rightResult;
    }

    private int findAvailablePath(TreeNode root, int targetSum) {
        if (root == null) {
            return 0;
        }

        int result = (root.val == targetSum) ? 1 : 0;

        int leftResult = findAvailablePath(root.left, targetSum - root.val);
        int rightResult = findAvailablePath(root.right, targetSum - root.val);

        return result + leftResult + rightResult;
    }

    public static void main(String[] args) {
        ListNode head = buildListNode();
        TreeNode root = buildTreeNode();
        Solution solution = new Solution();

        System.out.println(solution.pathSum(root, 8));

    }

    private static ListNode buildListNode() {
        ListNode head = new ListNode(1);
        ListNode node2 = new ListNode(1);
        ListNode node3 = new ListNode(1);
        ListNode node4 = new ListNode(1);
        ListNode node5 = new ListNode(1);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        return head;
    }

    private static TreeNode buildTreeNode() {
//        TreeNode head = new TreeNode(1);
//        head.left = new TreeNode(2);
//        head.right = new TreeNode(3);
//        head.left.left = new TreeNode(4);
//        head.left.right = new TreeNode(5);
//        head.right.left = new TreeNode(6);
//        head.right.right = new TreeNode(7);

        TreeNode head = new TreeNode(10);

        head.left = new TreeNode(5);
        head.right = new TreeNode(-3);

        head.left.left = new TreeNode(3);
        head.left.right = new TreeNode(2);
        head.right.right = new TreeNode(11);

        head.left.left.left = new TreeNode(3);
        head.left.left.right = new TreeNode(-2);
        head.left.right.right = new TreeNode(1);

        return head;
    }

}