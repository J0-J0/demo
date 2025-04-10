package com.jojo.leetcode;

import com.jojo.leetcode.node.ListNode;
import com.jojo.leetcode.node.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 默认刷题的类
 */
@Deprecated
@SuppressWarnings("unused")
class SolutionFirstRound {

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

    public boolean l416canPartition(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return false;
        }
        int sum = 0, maxNum = 0;
        for (int num : nums) {
            sum += num;
            maxNum = Math.max(maxNum, num);
        }
        if (sum % 2 != 0) {
            return false;
        }
        int target = sum / 2;
        if (maxNum > target) {
            return false;
        }
        boolean[][] dp = new boolean[n][target + 1];
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }
        dp[0][nums[0]] = true;
        for (int i = 1; i < n; i++) {
            int num = nums[i];
            for (int j = 1; j <= target; j++) {
                if (j >= num) {
                    dp[i][j] = dp[i - 1][j] | dp[i - 1][j - num];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[n - 1][target];
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
        int availableCount = 0, left = 0, right = 0, position = 0;

        // 数组左
        // 0 0 1 = 1; 0 0 0 0 1 = 2; 偶数情况下，available = count/2
        // 0 1 = 0； 0 0 0 1 = 1; 0 0 0 0 0 1 = 2; 奇数情况下，available = (count - 1) / 2
        for (; position < flowerbed.length; position++) {
            if (flowerbed[position] == 0) {
                left++;
            } else {
                break;
            }
        }
        if (position == flowerbed.length) {
            availableCount = (left % 2 == 0) ? (left / 2) : ((left + 1) / 2);
            return availableCount >= n;
        }

        availableCount = (left % 2 == 0) ? (left / 2) : ((left - 1) / 2);

        // 数组右, 和数组左一样
        // 1 0 0 = 1; 1 0 0 0 0 = 2
        // 1 0 = 0; 1 0 0 0 = 1; 1 0 0 0 0 0 = 2
        int rightStart = flowerbed.length - 1;
        for (; rightStart >= 0; rightStart--) {
            if (flowerbed[rightStart] == 0) {
                right++;
            } else {
                break;
            }
        }
        availableCount += (right % 2 == 0) ? (right / 2) : ((right - 1) / 2);

        // 1 0 0 1 = 0; 1 0 0 0 0 1 = 1; 1 0 0 0 0 0 0 1 = 2; 1 0 0 0 0 0 0 0 0 1 = 3;
        // 1 0 1 = 0; 1 0 0 0 1 = 1; 1 0 0 0 0 0 1 = 2;
        int middle = 0;
        for (; position <= rightStart; position++) {
            if (middle == 0 && flowerbed[position] == 1) continue;

            if (flowerbed[position] == 0) {
                middle++;
                continue;
            }

            if (middle != 0 && flowerbed[position] == 1) {
                availableCount += (middle % 2 == 0) ? ((middle - 2) / 2) : ((middle - 1) / 2);
                middle = 0;
            }
        }

        return availableCount >= n;
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
            return new ArrayList<>();
        }

        List<String> resultList = new ArrayList<>();

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
            return l278isBadVersion(n) ? n : 0;
        }

        int left = 1, right = n;

        int firstBadVersion = left + (right - left) / 2;
        while (left < right) {
            if (left + 1 == right) {
                firstBadVersion = l278isBadVersion(left) ? left : right;
                break;
            }
            if (l278isBadVersion(firstBadVersion)) {
                right = firstBadVersion;
            } else {
                left = firstBadVersion;
            }
            firstBadVersion = left + (right - left) / 2;
        }

        return firstBadVersion;
    }

    private boolean l278isBadVersion(int version) {
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

    private List<Integer> l94treeNodeValList = new ArrayList<>();

    /**
     * 中序遍历
     *
     * @param root
     * @return
     */
    public List<Integer> l94InorderTraversal(TreeNode root) {
        if (root == null) {
            return l94treeNodeValList;
        }

        l94InorderTraversal(root.left);
        l94treeNodeValList.add(root.val);
        l94InorderTraversal(root.right);

        return l94treeNodeValList;
    }

    /**
     * 后序遍历
     *
     * @param root
     * @return
     */
    public List<Integer> l145PostorderTraversal(TreeNode root) {
        if (root == null) {
            return l94treeNodeValList;
        }

        l145PostorderTraversal(root.left);
        l145PostorderTraversal(root.right);
        l94treeNodeValList.add(root.val);

        return l94treeNodeValList;
    }

    /**
     * 前序遍历
     *
     * @param root
     * @return
     */
    public List<Integer> l144PreorderTraversal(TreeNode root) {
        if (root == null) {
            return l94treeNodeValList;
        }

        l94treeNodeValList.add(root.val);

        l144PreorderTraversal(root.left);
        l144PreorderTraversal(root.right);

        return l94treeNodeValList;
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

    private List<List<Integer>> l113pathListList = new ArrayList<>();

    public List<List<Integer>> l113PathSum(TreeNode root, int targetSum) {
        List<Integer> onePathList = new ArrayList<>();

        return l113findAvailablePath(root, targetSum, onePathList);
    }

    private List<List<Integer>> l113findAvailablePath(TreeNode root, int targetSum, List<Integer> treeNodeValList) {
        List<Integer> resultList = new ArrayList<>();
        resultList.addAll(treeNodeValList);

        if (root == null) {
            return l113pathListList;
        }

        resultList.add(root.val);
        if (root.left == null && root.right == null) {
            if (root.val == targetSum) {
                l113pathListList.add(resultList);
                return l113pathListList;
            }
        }

        l113findAvailablePath(root.left, targetSum - root.val, resultList);
        l113findAvailablePath(root.right, targetSum - root.val, resultList);

        return l113pathListList;
    }

    public int l437PathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return 0;
        }

        int result = l437FindAvailablePath(root, targetSum);

        int leftResult = l437PathSum(root.left, targetSum);
        int rightResult = l437PathSum(root.right, targetSum);

        return result + leftResult + rightResult;
    }

    private int l437FindAvailablePath(TreeNode root, int targetSum) {
        if (root == null) {
            return 0;
        }

        int result = (root.val == targetSum) ? 1 : 0;

        int leftResult = l437FindAvailablePath(root.left, targetSum - root.val);
        int rightResult = l437FindAvailablePath(root.right, targetSum - root.val);

        return result + leftResult + rightResult;
    }

    public TreeNode l897IncreasingBST(TreeNode root) {
        if (root == null) {
            return root;
        }

        List<TreeNode> nodeList = l897InorderTraversal(root);

        TreeNode temp = nodeList.get(0);
        root = temp;
        for (int i = 1; i < nodeList.size(); i++) {
            temp.right = nodeList.get(i);
            temp.left = null;
            temp = nodeList.get(i);
        }
        temp.left = null;
        temp.right = null;
        return root;
    }

    private List<TreeNode> l897nodeList = new ArrayList<>();

    private List<TreeNode> l897InorderTraversal(TreeNode root) {
        if (root == null) {
            return l897nodeList;
        }

        l897InorderTraversal(root.left);
        l897nodeList.add(root);
        l897InorderTraversal(root.right);

        return l897nodeList;
    }

    public int l1302DeepestLeavesSum(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int height = l1302FindDeepestNode(root, 1);
        return l1302nodeValMap.get(height);
    }

    private Map<Integer, Integer> l1302nodeValMap = new HashMap<>();

    private int l1302FindDeepestNode(TreeNode root, int height) {
        if (root == null) {
            return height;
        }

        if (root.left == null && root.right == null) {
            if (l1302nodeValMap.get(height) == null) {
                l1302nodeValMap.put(height, root.val);
            } else {
                l1302nodeValMap.put(height, l1302nodeValMap.get(height) + root.val);
            }
            return height;
        }

        int leftHeight = l1302FindDeepestNode(root.left, height + 1);
        int rightHeight = l1302FindDeepestNode(root.right, height + 1);

        return leftHeight > rightHeight ? leftHeight : rightHeight;
    }

    /**
     * 530
     * 先取出全部节点值，然后排序，最后拿两个最小的值相减
     *
     * @param root
     * @return
     */
    public int l530getMinimumDifference(TreeNode root) {
        if (root == null) {
            return 0;
        }
        l530DFS(root);// 取出全部节点值

        if (l530NodeValList.size() == 1) {
            return l530NodeValList.get(0);
        }

        int minDiff = Math.abs(l530NodeValList.get(0) - l530NodeValList.get(1));
        for (int i = 1; i < l530NodeValList.size() - 1; i++) {
            int diff = Math.abs(l530NodeValList.get(i) - l530NodeValList.get(i + 1));
            if (minDiff > diff) {
                minDiff = diff;
            }
        }

        return minDiff;
    }

    private List<Integer> l530NodeValList = new ArrayList<>();

    private void l530DFS(TreeNode root) {
        if (root == null) {
            return;
        }

        l530DFS(root.left);
        l530NodeValList.add(root.val);
        l530DFS(root.right);
    }

    public String[] l1078findOcurrences(String text, String first, String second) {
        if (text == null || "".equals(text)) {
            return new String[]{};
        }

        String[] arr = text.split(" ");
        if (arr.length < 3) {
            return new String[]{};
        }

        List<String> list = new ArrayList<>();
        for (int i = 0; i < arr.length - 2; i++) {
            if (arr[i].equals(first) && arr[i + 1].equals(second)) {
                list.add(arr[i + 2]);
            }
        }

        String[] result = list.toArray(new String[]{});
        return result;
    }

    public String l541reverseStr(String s, int k) {
        if (s == null || "".equals(s) || k == 0) {
            return s;
        }

        StringBuffer result = new StringBuffer();

        char[] arr = s.toCharArray();
        int condition = 2 * k;
        int divide = s.length() / condition, remainder = s.length() % condition;
        if (divide > 0) {// 起码要覆盖一些2k
            for (int i = 0; i < s.length() - remainder; i += condition) {
                for (int j = (i + k - 1); j >= i; j--) {// 倒序
                    result.append(arr[j]);
                }
                for (int j = i + k; j < i + condition; j++) {
                    result.append(arr[j]);
                }
            }
        }

        if (remainder > 0 && remainder <= k) {
            for (int i = s.length() - 1; i >= s.length() - remainder; i--) {
                result.append(arr[i]);
            }
        } else if (remainder > 0 && remainder > k) {
            for (int i = s.length() - (remainder - k) - 1; i >= s.length() - remainder; i--) {
                result.append(arr[i]);
            }
            for (int i = s.length() - (remainder - k); i < s.length(); i++) {
                result.append(arr[i]);
            }
        }
        return result.toString();
    }

    public String l5longestPalindrome(String s) {
        int len = s.length();
        if (len < 2) {
            return s;
        }

        int maxLen = 1;
        int begin = 0;
        // dp[i][j] 表示 s[i..j] 是否是回文串
        boolean[][] dp = new boolean[len][len];
        // 初始化：所有长度为 1 的子串都是回文串
        for (int i = 0; i < len; i++) {
            dp[i][i] = true;
        }

        char[] charArray = s.toCharArray();
        // 递推开始
        for (int L = 2; L <= len; L++) {
            for (int i = 0; i < len; i++) {
                // 由 L 和 i 可以确定右边界，即 j - i + 1 = L 得
                int j = L + i - 1;
                // 如果右边界越界，就可以退出当前循环
                if (j >= len) {
                    break;
                }

                if (charArray[i] != charArray[j]) {
                    dp[i][j] = false;
                } else {
                    if (j - i < 3) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];// 已经推导过了
                    }
                }

                // 只要 dp[i][L] == true 成立，就表示子串 s[i..L] 是回文，此时记录回文长度和起始位置
                if (dp[i][j] && j - i + 1 > maxLen) {
                    maxLen = j - i + 1;
                    begin = i;
                }
            }
        }
        return s.substring(begin, begin + maxLen);
    }

    public boolean l392isSubsequence(String s, String t) {
        if (s.length() == 0 && t.length() == 0) {
            return true;
        }

        if (s.length() > t.length() || (s.length() == 0 && t.length() != 0)) {
            return false;
        }

        char[] sArr = s.toCharArray(), tArr = t.toCharArray();
        int j = 0;
        for (int i = 0; i < t.length(); i++) {
            if (j == s.length()) {
                break;
            }
            if (sArr[j] == tArr[i]) {
                j++;
                continue;
            }
        }

        return j == s.length();
    }

    public List<String> l401readBinaryWatch(int turnedOn) {
        List<String> list = new ArrayList<>();
        if (turnedOn == 0) {
            list.add("0:00");
            return list;
        }

        if (turnedOn > 8) {
            return list;
        }

        for (int hour = 0; hour < 12; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                int hourCount = l401getHourCount(hour);
                int minuteCount = l401getMinuteCount(minute);

                if ((hourCount + minuteCount) == turnedOn) {
                    if (minute < 10) {
                        list.add(hour + ":0" + minute);
                    } else {
                        list.add(hour + ":" + minute);
                    }
                }
            }
        }

        return list;
    }

    private int[] l401hourArr = new int[]{8, 4, 2, 1};
    private int[] l401minuteArr = new int[]{32, 16, 8, 4, 2, 1};

    private int l401getHourCount(int hour) {
        int hourCount = 0;
        for (int i = 0; i < l401hourArr.length; i++) {
            int diff = hour - l401hourArr[i];
            if (diff >= 0) {
                hourCount++;
                hour = diff;
            }
        }
        return hourCount;
    }

    private int l401getMinuteCount(int minute) {
        int minuteCount = 0;
        for (int i = 0; i < l401minuteArr.length; i++) {
            int diff = minute - l401minuteArr[i];
            if (diff >= 0) {
                minuteCount++;
                minute = diff;
            }
        }
        return minuteCount;
    }

    public int[] l501findMode(TreeNode root) {
        l501dfs(root);

        int[] result = new int[]{};
        if (l501valFrequenceMap.isEmpty()) {
            return result;
        }

        int maxFrequency = 0;
        List<Integer> resultList = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : l501valFrequenceMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                resultList.clear();
                resultList.add(entry.getKey());
                maxFrequency = entry.getValue();
            } else if (entry.getValue() == maxFrequency) {
                resultList.add(entry.getKey());
            }
        }
        result = new int[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i);
        }
        return result;
    }

    private Map<Integer, Integer> l501valFrequenceMap = new HashMap<>();

    private void l501dfs(TreeNode root) {
        if (root == null) {
            return;
        }

        Integer frequence = l501valFrequenceMap.get(root.val);
        if (frequence == null) {
            l501valFrequenceMap.put(root.val, 1);
        } else {
            l501valFrequenceMap.put(root.val, ++frequence);
        }
        l501dfs(root.left);
        l501dfs(root.right);
    }


    public List<String> l1169invalidTransactions(String[] transactions) {
        List<String> resultList = new ArrayList<>();
        if (transactions == null || transactions.length == 0) {
            return resultList;
        }

        int id = -1;
        Map<String, List<L1169Transcation>> nameTranMap = new HashMap<>();
        boolean[] isReturn = new boolean[transactions.length];
        for (String transactionStr : transactions) {
            ++id;
            String[] tranArr = transactionStr.split(",");
            String name = tranArr[0], time = tranArr[1], money = tranArr[2], city = tranArr[3];

            if (Integer.parseInt(money) > 1000) {
                isReturn[id] = true;
                resultList.add(transactionStr);
            }

            L1169Transcation transcation = new L1169Transcation(id, name, time, money, city);
            List<L1169Transcation> transcationList = nameTranMap.computeIfAbsent(name, key -> new ArrayList<>());
            for (L1169Transcation temp : transcationList) {
                if (!temp.city.equals(transcation.city) && Math.abs(temp.time - transcation.time) <= 60) {
                    if (!isReturn[id]) {// 是否已标记返回
                        isReturn[id] = true;
                        resultList.add(transcation.toString());
                    }
                    if (!isReturn[temp.id]) {
                        isReturn[temp.id] = true;
                        resultList.add(temp.toString());
                    }
                }
            }
            transcationList.add(transcation);
        }
        return resultList;
    }

    class L1169Transcation {
        int id;
        String name;
        int time;
        String money;
        String city;

        public L1169Transcation(int id, String name, String time, String money, String city) {
            this.id = id;
            this.name = name;
            this.time = Integer.parseInt(time);
            this.money = money;
            this.city = city;
        }

        @Override
        public String toString() {
            return name + "," + time + "," + money + "," + city;
        }
    }

    public boolean l680validPalindrome(String s) {
        char[] arr = s.toCharArray();
        if (l680isPalindrome(arr, s.length())) {
            return true;
        }

        for (int i = 0; i < s.length(); i++) {
            if (l680isPalindrome(arr, i)) {
                return true;
            }
        }

        return false;
    }

    private boolean l680isPalindrome(char[] arr, int flag) {
        for (int i = 0, j = arr.length - 1; i < arr.length / 2; i++, j--) {
            if (i == flag) {
                i++;
            } else if (j == flag) {
                j--;
            }
            if (arr[i] != arr[j]) {
                return false;
            }
        }
        return true;
    }


    public int l441arrangeCoins(int n) {
        int result = 0;
        while (n > 0) {
            result++;
            n -= result;
        }

        if (n == 0) {
            return result;
        } else {
            return result - 1;
        }
    }

    public String l720longestWord(String[] words) {
        Set<String> wordSet = new HashSet<>();
        for (String word : words) {
            wordSet.add(word);
        }

        String result = "";
        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > result.length() || words[i].length() == result.length() && words[i].compareTo(result) < 0) {
                boolean overFlag = true;
                for (int j = 1; j < words[i].length(); j++) {
                    if (!wordSet.contains(words[i].substring(0, j))) {
                        overFlag = false;
                    }
                }
                if (overFlag) {
                    result = words[i];
                }
            }
        }
        return result;
    }

    public String l482licenseKeyFormatting(String s, int k) {
        StringBuffer result = new StringBuffer();
        String separator = "-";

        // 去除原本字符串中的"-"，并做简单判断
        s = s.replaceAll(separator, "");
        if (k > s.length()) {
            return s;
        }

        int length = s.length(), start = 0;
        int mod = length % k;
        if (mod != 0) {
            result.append(separator).append(s, 0, mod);
            start = mod;
        }

        for (; start < length; start += k) {
            result.append(separator).append(s, start, start + k);
        }

        return result.toString().replaceFirst(separator, "").toUpperCase();
    }

    public String[] l506findRelativeRanks(int[] score) {
        String gold = "Gold Medal", silver = "Silver Medal", bronze = "Bronze Medal";
        if (score.length == 1) {
            return new String[]{gold};
        }

        Map<Integer, Integer> valPositionMap = new HashMap<>();
        Integer[] scoreCopy = new Integer[score.length];
        for (int i = 0; i < score.length; i++) {
            valPositionMap.put(score[i], i);
            scoreCopy[i] = score[i];
        }

        Arrays.sort(scoreCopy, (o1, o2) -> o1 < o2 ? 1 : -1);

        String[] result = new String[score.length];
        for (int i = 0; i < scoreCopy.length; i++) {
            if (i == 0) {
                result[valPositionMap.get(scoreCopy[i])] = gold;
            } else if (i == 1) {
                result[valPositionMap.get(scoreCopy[i])] = silver;
            } else if (i == 2) {
                result[valPositionMap.get(scoreCopy[i])] = bronze;
            } else {
                result[valPositionMap.get(scoreCopy[i])] = i + 1 + "";
            }
        }
        return result;
    }

    public int l543diameterOfBinaryTree(TreeNode root) {
        l543dfs(root);

        return l543maxDiameter;
    }

    private int l543maxDiameter = 0;

    private int l543dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = l543dfs(root.left);
        int right = l543dfs(root.right);
        int maxLength = left > right ? left + 1 : right + 1;

        int rootDiameter = left + right;
        if (rootDiameter > l543maxDiameter) {
            l543maxDiameter = rootDiameter;
        }
        return maxLength;
    }

    public int l747dominantIndex(int[] nums) {
        if (nums.length == 1) {
            return 0;
        }

        int maxNumPosition = 0;
        int maxNum = 0, secondNum = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > maxNum) {
                secondNum = maxNum;
                maxNum = nums[i];
                maxNumPosition = i;
            } else if (nums[i] > secondNum) {
                secondNum = nums[i];
            }
        }

        if (maxNum >= (secondNum * 2)) {
            return maxNumPosition;
        } else {
            return -1;
        }
    }

    public boolean l551checkRecord(String s) {
        if (s.length() == 1) {
            return true;
        }

        char absent = 'A', late = 'L';
        int countA = 0;
        for (int i = 0; i < s.length(); i++) {
            char key = s.charAt(i);

            if (key == absent) {
                countA++;
                if (countA >= 2) {
                    return false;
                }
            }

            if ((s.length() - 3 >= i) && (key == late && s.charAt(i + 1) == late && s.charAt(i + 2) == late)) {
                return false;
            }
        }

        return true;
    }

    public int l559maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        List<TreeNode> treeNodeList = root.children;
        if (treeNodeList == null || treeNodeList.size() == 0) {
            return 1;
        }

        int maxDepth = 0;
        for (int i = 0; i < treeNodeList.size(); i++) {
            int currDepth = l559maxDepth(treeNodeList.get(i)) + 1;
            if (currDepth > maxDepth) {
                maxDepth = currDepth;
            }
        }
        return maxDepth;
    }

    public String l504convertToBase7(int num) {
        StringBuffer result = new StringBuffer();

        while (num / 7 != 0) {
            result.append(Math.abs(num % 7));
            num /= 7;
        }

        result.append(Math.abs(num % 7));

        return num < 0 ? "-" + result.reverse() : result.reverse().toString();
    }

    public int l11maxArea(int[] height) {
        int maxArea = 0;
        for (int i = 0, j = height.length - 1; i < j; ) {
            int area = Math.min(height[i], height[j]) * (j - i);
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
     * 字符          数值
     * I             1
     * V             5
     * X             10
     * L             50
     * C             100
     * D             500
     * M             1000
     * <p>
     * 例如， 罗马数字 2 写做II，即为两个并列的 1。12 写做XII，即为X+II。 27 写做XXVII, 即为XX+V+II。
     * <p>
     * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做IIII，而是IV。
     * 数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。
     * 同样地，数字 9 表示为IX。这个特殊的规则只适用于以下六种情况：
     * <p>
     * I可以放在V(5) 和X(10) 的左边，来表示 4 和 9。
     * X可以放在L(50) 和C(100) 的左边，来表示 40 和90。
     * C可以放在D(500) 和M(1000) 的左边，来表示400 和900。
     *
     * @param num
     * @return
     */
    public String l12intToRoman(int num) {
        StringBuffer result = new StringBuffer();
        // 千
        int thousandsDivider = num / 1000;
        num %= 1000;
        for (int i = 0; i < thousandsDivider; i++) {
            result.append("M");
        }
        if (num == 0) {
            return result.toString();
        }

        // 百
        int hundredsDivider = num / 100;
        num %= 100;
        if (hundredsDivider < 4) {
            for (int i = 0; i < hundredsDivider; i++) {
                result.append("C");
            }
        } else if (hundredsDivider == 4) {
            result.append("CD");
        } else if (hundredsDivider < 9) {
            result.append("D");
            for (int i = 5; i < hundredsDivider; i++) {
                result.append("C");
            }
        } else if (hundredsDivider == 9) {
            result.append("CM");
        }
        if (num == 0) {
            return result.toString();
        }

        // 十
        int tensDivider = num / 10;
        num %= 10;
        if (tensDivider < 4) {
            for (int i = 0; i < tensDivider; i++) {
                result.append("X");
            }
        } else if (tensDivider == 4) {
            result.append("XL");
        } else if (tensDivider < 9) {
            result.append("L");
            for (int i = 5; i < tensDivider; i++) {
                result.append("X");
            }
        } else if (tensDivider == 9) {
            result.append("XC");
        }
        if (num == 0) {
            return result.toString();
        }

        // 个
        if (num < 4) {
            for (int i = 0; i < num; i++) {
                result.append("I");
            }
        } else if (num == 4) {
            result.append("IV");
        } else if (num < 9) {
            result.append("V");
            for (int i = 5; i < num; i++) {
                result.append("I");
            }
        } else if (num == 9) {
            result.append("IX");
        }

        return result.toString();
    }

    /**
     * 不要动不动就想着动态规划，排序+双指针
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> l15threeSum(int[] nums) {
        int length = nums.length;
        if (nums == null || length < 3) {
            return new ArrayList<>();
        }

        Arrays.sort(nums);

        List<List<Integer>> resultList = new ArrayList<>();
        for (int first = 0; first < length - 2; first++) {
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }

            int third = length - 1;

            for (int second = first + 1; second < length - 1; second++) {
                if (second > first + 1 && nums[second] == nums[second - 1]) {
                    continue;
                }
                // 到处都是细节，唉
                while (second < third && nums[third] + nums[second] + nums[first] > 0) {
                    third--;
                }

                if (second == third) {
                    break;
                }

                if (nums[first] + nums[second] + nums[third] == 0) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[first]);
                    list.add(nums[second]);
                    list.add(nums[third]);
                    resultList.add(list);
                }
            }
        }

        return resultList;
    }

    public List<String> l17letterCombinations(String digits) {
        if (digits == null || digits.length() == 0) {
            return new ArrayList<>();
        }

        String[] arr = {"abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < digits.length(); i++) {
            String phoneNumChar = arr[digits.charAt(i) - 50];
            if (i == 0) {
                for (int j = 0; j < phoneNumChar.length(); j++) {
                    resultList.add(phoneNumChar.charAt(j) + "");
                }
                continue;
            }

            List<String> temp = new ArrayList<>();
            for (String s : resultList) {
                for (int j = 0; j < phoneNumChar.length(); j++) {
                    temp.add(s + phoneNumChar.charAt(j));
                }
            }

            resultList = temp;
        }

        return resultList;
    }

    public int l704search(int[] nums, int target) {
        if (nums.length == 1) {
            return nums[0] == target ? 0 : -1;
        }

        int left = 0, right = nums.length - 1, middle = 0;
        while (left <= right) {
            middle = (left + right) / 2;
            if (nums[middle] == target) {
                return middle;
            }

            if (nums[middle] < target) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }

        return -1;
    }

    public int l563findTilt(TreeNode root) {

        l563dfs(root);

        return l563tilt;
    }

    private int l563tilt = 0;

    private int l563dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int leftSum = l563dfs(root.left);
        int rightSum = l563dfs(root.right);

        l563tilt += Math.abs(leftSum - rightSum);

        return leftSum + root.val + rightSum;
    }

    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if (l572check(root, subRoot)) {
            return true;
        }

        return (root != null && isSubtree(root.left, subRoot)) || (root != null && isSubtree(root.right, subRoot));
    }

    private boolean l572check(TreeNode root, TreeNode subRoot) {
        if (root == null && subRoot == null) return true;

        if ((root == null && subRoot != null) || (root != null && subRoot == null)) {
            return false;
        }

        if (root.val != subRoot.val) return false;

        return l572check(root.left, subRoot.left) && l572check(root.right, subRoot.right);
    }

    public int l2047countValidWords(String sentence) {
        if (sentence == null || sentence.length() == 0) {
            return 0;
        }

        int result = 0;
        String regex1 = "[a-z]*[!,.]?", regex2 = "[a-z]+-[a-z]+[!,.]?";
        String[] arr = sentence.split(" ");
        for (String str : arr) {
            str = str.trim();
            if (str.length() == 0) {
                continue;
            }
            if (str.matches(regex1) || str.matches(regex2)) {
                result++;
            }
        }

        return result;
    }

    public int l594findLHS(int[] nums) {
        if (nums.length == 1) {
            return 0;
        }

        Arrays.sort(nums);

        int maxLength = 0;
        for (int i = 0; i < nums.length; i++) {
            while (i != 0 && i < nums.length && nums[i - 1] == nums[i]) {
                i++;
            }

            int j = i + 1;
            while (j + 1 < nums.length && (nums[j] == nums[i] || nums[j] == nums[j + 1])) {
                j++;
            }

            if (j < nums.length && nums[j] - nums[i] == 1) {
                maxLength = Math.max(j - i + 1, maxLength);
            }
        }
        return maxLength;
    }

    public List<Integer> l589preorder(TreeNode root) {
        if (root == null) {
            return l589nodeValList;
        }

        l589nodeValList.add(root.val);

        if (root.children != null && root.children.size() != 0) {
            for (TreeNode child : root.children) {
                l589preorder(child);
            }
        }
        return l589nodeValList;
    }

    private List<Integer> l589nodeValList = new ArrayList<>();

    public int l628maximumProduct(int[] nums) {
        if (nums.length == 3) {
            return nums[0] * nums[1] * nums[2];
        }

        Arrays.sort(nums);
        int length = nums.length;

        int x = nums[length - 1] * nums[length - 2] * nums[length - 3];
        if (nums[0] > 0 || nums[length - 1] < 0) {
            return x;
        }

        int y = nums[0] * nums[1] * nums[length - 1];

        return Math.max(x, y);
    }

    public int l2006countKDifference(int[] nums, int k) {
        if (nums.length == 1) {
            return 0;
        }
        int answer = 0;

        // 暴力解法
//        for (int i = 0; i < nums.length - 1; i++) {
//            for (int j = i + 1; j < nums.length; j++) {
//                if (k == Math.abs(nums[i] - nums[j])) {
//                    answer++;
//                }
//            }
//        }
//

        // 哈希表，来把这个简单题升华一下
        Map<Integer, List<Integer>> valPosMap = new HashMap<>();
        for (int pos = 0; pos < nums.length; pos++) {
            List<Integer> posList = valPosMap.getOrDefault(nums[pos], new ArrayList<>());
            posList.add(pos);
            valPosMap.put(nums[pos], posList);
        }

        for (int i = 0; i < nums.length; i++) {
            int availableJ = nums[i] - k;
            List<Integer> posList = valPosMap.get(availableJ);
            if (posList != null) {
                for (Integer pos : posList) {
                    answer = pos > i ? ++answer : answer;
                }
            }

            availableJ = nums[i] + k;
            posList = valPosMap.get(availableJ);
            if (posList != null) {
                for (Integer pos : posList) {
                    answer = pos > i ? ++answer : answer;
                }
            }
        }

        return answer;
    }

    /**
     * 不能浮躁
     *
     * @param nums
     * @param k
     * @return
     */
    public double l643findMaxAverage(int[] nums, int k) {
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += nums[i];
        }

        int maxSum = sum;
        for (int i = 1; i <= nums.length - k; i++) {
            sum = sum - nums[i - 1] + nums[i + k - 1];
            maxSum = Math.max(maxSum, sum);
        }

        return (double) maxSum / k;
    }

    public int l1984minimumDifference(int[] nums, int k) {
        if (k == 1) {
            return 0;
        }

        Arrays.sort(nums);

        int answer = nums[k - 1] - nums[0];
        for (int i = 1; i <= nums.length - k; i++) {
            answer = Math.min(answer, nums[i + k - 1] - nums[i]);
        }

        return answer;
    }

    public int[] l645findErrorNums(int[] nums) {
        Arrays.sort(nums);

        int temp = nums[0], duplicate = 0;
        for (int i = 1; i < nums.length; i++) {
            if (temp == nums[i]) {
                duplicate = temp;
                break;
            }
            temp = nums[i];
        }

        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += i + 1;
        }

        sum -= duplicate;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == duplicate) {
                continue;
            }
            sum -= nums[i];
        }

        return new int[]{duplicate, sum};
    }

    public List<Integer> l1380luckyNumbers(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix.length == 1 && matrix[0].length == 1) {
            result.add(matrix[0][0]);
            return result;
        }

        Map<Integer, Integer> columnPosMaxMap = new HashMap<>();
        for (int i = 0; i < matrix[0].length; i++) {
            int columnMax = matrix[0][i];
            for (int j = 1; j < matrix.length; j++) {
                columnMax = Math.max(columnMax, matrix[j][i]);
            }
            columnPosMaxMap.put(i, columnMax);
        }

        for (int i = 0; i < matrix.length; i++) {
            int lineMin = matrix[i][0], lineMinColumn = 0;
            for (int j = 1; j < matrix[i].length; j++) {
                if (lineMin > matrix[i][j]) {
                    lineMin = matrix[i][j];
                    lineMinColumn = j;
                }
            }

            if (columnPosMaxMap.get(lineMinColumn) == lineMin) {
                result.add(lineMin);
            }
        }

        return result;
    }

    /**
     * 所有非英文字母保留在原地，英文字母，反转
     *
     * @param s
     * @return
     */
    public String l917reverseOnlyLetters(String s) {
        // 是正确答案，但效率不算很高
//        StringBuffer descEnglishChar = new StringBuffer();
//        for (int i = s.length() - 1; i >= 0; i--) {
//            if (this.l917isEnglishChar(s.charAt(i))) {
//                descEnglishChar.append(s.charAt(i));
//            }
//        }
//
//        // 再正序
//        StringBuffer result = new StringBuffer();
//        for (int i = 0, j = 0; i < s.length(); i++) {
//            if (!this.l917isEnglishChar(s.charAt(i))) {
//                result.append(s.charAt(i));
//            } else {
//                result.append(descEnglishChar.charAt(j++));
//            }
//        }
//
//        return result.toString();

        // 只可以循环1次，而且不能占用额外的内存
        char[] answer = new char[s.length()];
        for (int i = s.length() - 1, j = 0; i >= j; i--, j++) {
            while (i >= 0 && !this.l917isEnglishChar(s.charAt(i))) {
                answer[i] = s.charAt(i);
                i--;
            }
            while (j <= s.length() - 1 && !this.l917isEnglishChar(s.charAt(j))) {
                answer[j] = s.charAt(j);
                j++;
            }

            if (i < 0 || j > s.length() - 1) {
                break;
            }

            answer[i] = s.charAt(j);
            answer[j] = s.charAt(i);
        }
        return new String(answer);
    }

    private boolean l917isEnglishChar(char c) {
        return (c > 64 && c < 91) || (c > 96 && c < 123);
    }

    public int lcp29orchestraLayout(int num, int xPos, int yPos) {
        long totalStep = 0l;
        int outLayerCount = Math.min(Math.min(Math.abs(xPos - 0), Math.abs(xPos - (num - 1))),
                Math.min(Math.abs(yPos - 0), Math.abs(yPos - (num - 1))));

        long currLayerPointCount = 0l;
        for (int i = 0; i < outLayerCount; i++) {
            currLayerPointCount = num - (i * 2) - 1;
            totalStep += (currLayerPointCount * 4);
        }

        currLayerPointCount -= 2;


        long leftStep = 0l;
        if (num - xPos == num - outLayerCount) {
            // 内圈，上边界
            leftStep = yPos - outLayerCount + 1;

        } else if (num - yPos == outLayerCount + 1) {
            // 内圈， 右边界
            leftStep = currLayerPointCount + (xPos - outLayerCount) + 1;

        } else if (num - xPos == outLayerCount + 1) {
            // 内圈，下边界
            leftStep = currLayerPointCount * 2 + (currLayerPointCount + outLayerCount - yPos) + 1;

        } else if (num - yPos == num - outLayerCount) {
            // 内圈，左边界
            leftStep = currLayerPointCount * 3 + (currLayerPointCount + outLayerCount - xPos) + 1;
        }

        totalStep += leftStep;

        int result = (int) (totalStep % 9);
        return result == 0 ? 9 : result;
    }

    @Deprecated
    private int[] lcp29instrument = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    @Deprecated
    private int lcp29pos = 0;

    @Deprecated
    private int lcp29getInstrument() {
        if (lcp29pos == 9) {
            lcp29pos = 0;
        }
        return lcp29instrument[lcp29pos++];
    }

    /**
     * 搞是搞出来了，但超出内存限制，删了怪可惜的，留着吧
     *
     * @param num
     * @return
     */
    @Deprecated
    private int[][] lcp29buildMatrix(int num) {
        int[][] matrix = new int[num][num];
        int start = 0, end = num;

        while (end - start > 0) {
            if (end - start == 1) {
                matrix[start][start] = this.lcp29getInstrument();
                break;
            }
            int i = start, j = start;
            for (; j < end - 1; j++) {
                matrix[i][j] = this.lcp29getInstrument();
            }

            for (; i < end - 1; i++) {
                matrix[i][j] = this.lcp29getInstrument();
            }

            for (; j > start; j--) {
                matrix[i][j] = this.lcp29getInstrument();
            }

            for (; i > start; i--) {
                matrix[i][j] = this.lcp29getInstrument();
            }

            start++;
            end--;
        }

        return matrix;
    }

    /**
     * 前缀最小值
     *
     * @param nums
     * @return
     */
    public int l2016maximumDifference(int[] nums) {
//        int maxDiffer = -1;
//        for (int i = 0; i < nums.length - 1; i++) {
//            for (int j = i + 1; j < nums.length; j++) {
//                if (nums[j] > nums[i]) {
//                    maxDiffer = Math.max(maxDiffer, nums[j] - nums[i]);
//                }
//            }
//        }
//        return maxDiffer;

        int maxDiffer = -1, prefix = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (prefix < nums[i]) {
                maxDiffer = Math.max(maxDiffer, nums[i] - prefix);
            } else {
                prefix = nums[i];
            }
        }
        return maxDiffer;
    }

    public boolean l657judgeCircle(String moves) {
        int x = 0, y = 0;
        char up = 'U', down = 'D', left = 'L', right = 'R';
        for (int i = 0; i < moves.length(); i++) {
            char move = moves.charAt(i);
            if (move == up) {
                x += 1;
            } else if (move == down) {
                x -= 1;
            } else if (move == left) {
                y -= 1;
            } else if (move == right) {
                y += 1;
            }
        }

        return x == 0 && y == 0;
    }

    public List<String> l22generateParenthesis(int n) {
        // 单纯自己想的，一个效率比较低的版本
//        if (n == 1) {
//            List<String> list = new ArrayList<>();
//            list.add("()");
//            return list;
//        }
//
//        Set<String> resultSet = new HashSet<>();
//        List<String> sonParenthesisList = l22generateParenthesis(n - 1);
//        for (String parenthesis : sonParenthesisList) {
//            resultSet.add("()" + parenthesis);
//            resultSet.add(parenthesis + "()");
//            resultSet.add("(" + parenthesis + ")");
//        }
//
//        for (int i = 1; i < n; i++) {
//            int j = n - i;
//            List<String> listI = l22generateParenthesis(j);
//            List<String> listJ = l22generateParenthesis(i);
//            for (String si : listI) {
//                for (String sj : listJ) {
//                    resultSet.add(si + sj);
//                    resultSet.add(sj + si);
//                }
//            }
//        }
//
//        return new ArrayList<>(resultSet);

        int length = n * 2;
        List<String> resultList = new ArrayList<>();

        String parenthesis = "";
        l22helper(resultList, length, parenthesis + "(");
        l22helper(resultList, length, parenthesis + ")");

        return resultList;
    }

    private void l22helper(List<String> resultList, int length, String str) {
        if (str.length() == length) {
            if (l22checkClosed(str)) {
                resultList.add(str);
            }
            return;
        }

        l22helper(resultList, length, str + "(");
        l22helper(resultList, length, str + ")");
    }

    private boolean l22checkClosed(String parenthesis) {
        int closed = 0, count = 0;
        for (int i = 0; i < parenthesis.length(); i++) {
            if (parenthesis.charAt(i) == '(') {
                closed++;
            } else if (parenthesis.charAt(i) == ')' && closed > 0) {
                closed--;
                count++;
            }
        }
        return closed == 0 && count == (parenthesis.length() / 2);
    }

    public boolean l653findTarget(TreeNode root, int k) {
        List<Integer> valList = new ArrayList<>();
        this.l653dfs(root, valList);

        if (valList.size() == 1) {
            return false;
        }

        for (int i = 0, j = valList.size() - 1; i < j; ) {
            int sum = valList.get(i) + valList.get(j);
            if (sum == k) {
                return true;
            }

            if (sum < k) {
                i++;
            } else {
                j--;
            }
        }

        return false;
    }

    private void l653dfs(TreeNode root, List<Integer> valList) {
        if (root == null) {
            return;
        }
        l653dfs(root.left, valList);
        valList.add(root.val);
        l653dfs(root.right, valList);
    }

    public int l671findSecondMinimumValue(TreeNode root) {
//        int secondMin = l671dfs(root, root.val);
//        return secondMin == root.val ? -1 : secondMin;

        Set<Integer> valSet = new HashSet<>();
        l671dfs(root, valSet);

        if (valSet.size() == 1) {
            return -1;
        }
        int min = Integer.MAX_VALUE, secondMin = Integer.MAX_VALUE;
        for (Integer val : valSet) {
            if (val < min) {
                secondMin = min;
                min = val;
            } else if (val < secondMin) {
                secondMin = val;
            }
        }
        return min == secondMin ? -1 : secondMin;
    }

    private void l671dfs(TreeNode root, Set<Integer> valList) {
        if (root == null) {
            return;
        }

        valList.add(root.val);
        l671dfs(root.left, valList);
        l671dfs(root.right, valList);
    }

    private int l671dfs(TreeNode root, int min) {
        if (root == null) {
            return -1;
        }

        int leftSecondMin = l671dfs(root.left, min);
        int rightSecondMin = l671dfs(root.right, min);

        if (leftSecondMin == -1 && rightSecondMin == -1) {
            return root.val;
        }

        int small = 0, big = 0;
        if (leftSecondMin > rightSecondMin) {
            big = leftSecondMin;
            small = rightSecondMin;
        } else {
            small = leftSecondMin;
            big = rightSecondMin;
        }

        return small > min ? small : big;
    }

    public int l258addDigits(int num) {
        while (num / 10 != 0) {
            int temp = num;
            num = 0;
            while (temp != 0) {
                num += temp % 10;
                temp /= 10;
            }
        }

        return num % 10;
    }

    public int[][] l661imageSmoother(int[][] img) {
        int m = img.length, n = img[0].length;
        if (m == 1 && n == 1) {
            return img;
        }

        int[][] result = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = this.l661smooth(img, i, j);
            }
        }
        return result;
    }

    private int l661smooth(int[][] img, int i, int j) {
        int m = img.length, n = img[0].length;
        int sum = 0, count = 0;

        if (i - 1 >= 0) {
            if (j - 1 >= 0) {
                sum += img[i - 1][j - 1];
                count++;
            }
            if (j >= 0 && j < n) {
                sum += img[i - 1][j];
                count++;
            }
            if (j + 1 < n) {
                sum += img[i - 1][j + 1];
                count++;
            }
        }

        if (i >= 0 && i < m) {
            if (j - 1 >= 0) {
                sum += img[i][j - 1];
                count++;
            }
            if (j >= 0 && j < n) {
                sum += img[i][j];
                count++;
            }
            if (j + 1 < n) {
                sum += img[i][j + 1];
                count++;
            }
        }

        if (i + 1 < m) {
            if (j - 1 >= 0) {
                sum += img[i + 1][j - 1];
                count++;
            }
            if (j >= 0 && j < n) {
                sum += img[i + 1][j];
                count++;
            }
            if (j + 1 < n) {
                sum += img[i + 1][j + 1];
                count++;
            }
        }

        return sum / count;
    }

    public int l674findLengthOfLCIS(int[] nums) {
        if (nums.length == 1) {
            return 1;
        }

        int maxLength = 0;
        for (int i = 1; i < nums.length; ) {
            // 找起点
            while (i < nums.length && nums[i - 1] >= nums[i]) {
                i++;
            }

            int length = 1;
            while (i < nums.length && nums[i - 1] < nums[i]) {
                length++;
                i++;
            }

            maxLength = Math.max(maxLength, length);
        }

        return maxLength;
    }

    public int[] l2055platesBetweenCandles(String s, int[][] queries) {
        int[] preSumArr = new int[s.length()];
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '*') {
                count++;
            }
            preSumArr[i] = count;
        }

        int[] lArr = new int[s.length()];
        for (int i = 0, temp = -1; i < s.length(); i++) {
            if (s.charAt(i) == '|') {
                temp = i;
            }
            lArr[i] = temp;
        }

        int[] answer = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int leftPos = queries[i][0], rightPos = queries[i][1];

            if (lArr[rightPos] == -1) {
                answer[i] = 0;
            } else if (lArr[leftPos] > 0 && lArr[rightPos] > 0) {
                answer[i] = preSumArr[lArr[rightPos]] - preSumArr[lArr[leftPos]];
            } else if (lArr[leftPos] == -1 && lArr[rightPos] > 0) {
                while (lArr[leftPos] == -1) {
                    leftPos++;
                }
                answer[i] = lArr[leftPos] < lArr[rightPos] ? preSumArr[lArr[rightPos]] - preSumArr[lArr[leftPos]] : 0;
            }
        }

        return answer;
    }

    public int l16threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);

        int result = 0, minDiffer = Integer.MAX_VALUE;

        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1, k = nums.length - 1; j < k; ) {
                int sum = nums[i] + nums[j] + nums[k];

                if (sum == target) {
                    return target;
                }

                int differ = Math.abs(target - sum);
                if (differ < minDiffer) {
                    minDiffer = differ;
                    result = sum;
                }

                if (sum >= target) {
                    k--;
                } else {
                    j++;
                }
            }
        }

        return result;
    }

    public String l606tree2str(TreeNode root) {
        if (root == null) {
            return "";
        }

        String answer = root.val + "";

        String left = l606tree2str(root.left);
        String right = l606tree2str(root.right);

        if ("".equals(left) && "".equals(right)) {
            return answer;
        } else if (!"".equals(left) && "".equals(right)) {
            return answer + "(" + left + ")";
        } else {
            return answer + "(" + left + ")" + "(" + right + ")";
        }
    }

    public int l682calPoints(String[] ops) {
        int answer = 0;

        int[] record = new int[ops.length];
        for (int i = 0, j = 0; i < ops.length; i++) {
            if (ops[i].equals("+")) {
                record[j] = record[j - 1] + record[j - 2];
                answer += record[j++];
            } else if (ops[i].equals("D")) {
                record[j] = record[j - 1] * 2;
                answer += record[j++];
            } else if (ops[i].equals("C")) {
                answer -= record[j - 1];
                j--;
            } else {
                record[j] = Integer.parseInt(ops[i]);
                answer += record[j++];
            }
        }

        return answer;
    }

    public boolean l693hasAlternatingBits(int n) {
        if (n == 1) {
            return false;
        }

        int last = -1;
        while (n != 0) {
            int remainer = n % 2;
            if (remainer != last) {
                last = remainer;
            } else if (remainer == last) {
                return false;
            }

            n /= 2;
        }
        return true;
    }

    public int l696countBinarySubstrings(String s) {
        char curChar = s.charAt(0);
        int curCount = 0, lastCount = 0;
        int answer = 0;

        for (char c : s.toCharArray()) {
            if (c == curChar) {
                curCount++;
            } else {
                answer += Math.min(curCount, lastCount);
                lastCount = curCount;
                curCount = 1;
                curChar = c;
            }
        }
        answer += Math.min(curCount, lastCount);

        return answer;
    }

    public List<Integer> l728selfDividingNumbers(int left, int right) {
        List<Integer> answerList = new ArrayList<>();
        A:
        for (int i = left; i <= right; i++) {
            int temp = i;
            while (temp > 0) {
                int remainer = temp % 10;
                if (remainer == 0 || i % remainer != 0) {
                    continue A;
                }
                temp /= 10;
            }
            answerList.add(i);
        }
        return answerList;
    }

    public boolean l367isPerfectSquare(int num) {
        for (int left = 0, right = num; left <= right; ) {
            int divider = (right + left) / 2;
            long temp = (long) divider * divider;
            if (num == temp) {
                return true;
            }

            if (temp > num) {
                right = divider - 1;
            } else {
                left = divider + 1;
            }
        }
        return false;

//        int left = 0, right = num;
//        while (left <= right) {
//            int mid = (right - left) / 2 + left;
//            long square = (long) mid * mid;
//            if (square < num) {
//                left = mid + 1;
//            } else if (square > num) {
//                right = mid - 1;
//            } else {
//                return true;
//            }
//        }
//        return false;
    }

    public char l744nextGreatestLetter(char[] letters, char target) {
        for (int i = 0; i < letters.length; i++) {
            if (letters[i] > target) {
                return letters[i];
            }
        }
        return letters[0];
    }

    public int l697findShortestSubArray(int[] nums) {
        // 寻找最大的度
        int maxDu = 0;
        Map<Integer, Integer> valCountMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer du = valCountMap.getOrDefault(nums[i], 0);
            du++;
            valCountMap.put(nums[i], du);
            maxDu = Math.max(du, maxDu);
        }
        // 特殊值直接返回
        if (maxDu == 1) {
            return 1;
        } else if (maxDu == nums.length) {
            return nums.length;
        }

        // 最大度有哪些值
        List<Integer> numList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : valCountMap.entrySet()) {
            if (entry.getValue() == maxDu) {
                numList.add(entry.getKey());
            }
        }

        // 寻找最小值
        int minLength = nums.length;
        for (Integer num : numList) {
            int maxDuCopy = maxDu, tempLength = 0;
            for (int i = 0; maxDuCopy != 0; i++) {
                if (tempLength == 0 && nums[i] == num) {
                    tempLength++;
                    maxDuCopy--;
                    continue;
                }
                if (tempLength != 0) {
                    tempLength++;
                }
                if (nums[i] == num) {
                    maxDuCopy--;
                }
            }
            minLength = Math.min(minLength, tempLength);
        }

        return minLength;
    }

    public TreeNode l700searchBST(TreeNode root, int val) {
        if (root == null || root.val == val) {
            return root;
        }

        TreeNode left = l700searchBST(root.left, val);
        TreeNode right = l700searchBST(root.right, val);

        return left != null ? left : right;
    }

    public int l804uniqueMorseRepresentations(String[] words) {
        Set<String> answerSet = new HashSet<>();
        for (String word : words) {
            String morse = "";
            for (char c : word.toCharArray()) {
                morse += l804MorseCode[c - 'a'];
            }
            answerSet.add(morse);
        }
        return answerSet.size();
    }

    private String[] l804MorseCode = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
            ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-",
            ".--", "-..-", "-.--", "--.."};

    public String l709toLowerCase(String s) {
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 65 && c <= 90) {
                answer.append((char) (c + 32));
            } else {
                answer.append(c);
            }
        }
        return answer.toString();
    }

    public boolean l717isOneBitCharacter(int[] bits) {
//        int length = bits.length;
//        if (bits[length - 1] == 1) {
//            return false;
//        }
//
//        if (length == 1) {
//            return true;
//        }
//
//        if (length == 2) {
//            if (bits[0] == 1) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//
//        if (bits[length - 1] == 0 && bits[length - 2] == 0) {
//            return true;
//        }
//
//        return false;

        int length = bits.length;
        if (bits[length - 1] == 1) {
            return false;
        }

        for (int i = 0; i < length - 1; ) {
            if (bits[i] == 1) {
                i += 2;
            } else {
                i++;
            }

            if (i > length - 1) {
                return false;
            }
        }
        return true;
    }

    public int l724pivotIndex(int[] nums) {
        if (nums.length == 1) {
            return 0;
        }

        int rightSum = 0, leftSum = 0;
        for (int i = 1; i < nums.length; i++) {
            rightSum += nums[i];
        }

        for (int i = 0; i < nums.length; i++) {
            if (leftSum == rightSum) {
                return i;
            }
            leftSum += nums[i];
            if (i + 1 < nums.length) {
                rightSum -= nums[i + 1];
            }
        }
        return -1;
    }

    public int l746minCostClimbingStairs(int[] cost) {
        int[] dp = new int[cost.length + 1];

        for (int i = 2; i <= cost.length; i++) {
            dp[i] = Math.min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2]);
        }

        return dp[cost.length];
    }

    private int l746doClimb(int pos, int step, int alreadyCost, int[] cost) {
        if (pos + step >= cost.length) {
            return alreadyCost;
        }

        alreadyCost += cost[pos + step];

        return Math.min(l746doClimb(pos + step, 1, alreadyCost, cost),
                l746doClimb(pos + step, 2, alreadyCost, cost));
    }

    public String l748shortestCompletingWord(String licensePlate, String[] words) {
        if (words.length == 1) {
            return words[0];
        }

        Arrays.sort(words, Comparator.comparingInt(String::length));
        Map<Character, Integer> charCountMap = new HashMap<>();
        for (char c : licensePlate.toCharArray()) {
            if (c == 32 || (c >= 48 && c <= 57)) {
                continue;
            }
            charCountMap.compute(Character.toLowerCase(c), (k, v) -> v == null ? 1 : v + 1);
        }

        for (String word : words) {
            Map<Character, Integer> tempMap = new HashMap<>(charCountMap);
            for (char c : word.toCharArray()) {
                c = Character.toLowerCase(c);
                if (tempMap.get(c) != null) {
                    if (tempMap.get(c) == 1) {
                        tempMap.remove(c);
                    } else {
                        tempMap.put(c, tempMap.get(c) - 1);
                    }
                }
            }

            if (tempMap.size() == 0) {
                return word;
            }
        }

        return "";
    }

    public int l783minDiffInBST(TreeNode root) {
        List<Integer> nodeValList = new ArrayList<>();
        this.l783dfs(root, nodeValList);
        nodeValList.sort(Integer::compare);

        int min = nodeValList.get(1) - nodeValList.get(0);
        for (int i = 1; i < nodeValList.size() - 1; i++) {
            min = Math.min(min, nodeValList.get(i + 1) - nodeValList.get(i));
        }
        return min;
    }

    private void l783dfs(TreeNode root, List<Integer> diffList) {
        if (root == null) {
            return;
        }
        l783dfs(root.left, diffList);
        diffList.add(root.val);
        l783dfs(root.right, diffList);
    }

    public boolean l796rotateString(String s, String goal) {
        if (s.length() != goal.length()) {
            return false;
        }
        if (goal.equals(s)) {
            return true;
        }

        String ss = s+s;

        return ss.contains(goal);
    }

    public static void main(String[] args) {
        SolutionFirstRound solution = new SolutionFirstRound();
        int[] nums = {10, 15, 20};

        System.out.println(solution.l796rotateString("abcde", "cdeab"));

    }
}