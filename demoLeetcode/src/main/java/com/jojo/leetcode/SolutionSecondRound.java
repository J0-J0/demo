package com.jojo.leetcode;

import com.jojo.leetcode.node.ListNode;
import com.jojo.leetcode.node.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class SolutionSecondRound {

    /**
     * 方法一，但是，嵌套循环很大概率死于时间不足
     */
    public int[] l1twoSum1(int[] nums, int target) {
        if (nums == null || nums.length == 1) {
            return new int[]{};
        }

        if (nums.length == 2 && nums[0] + nums[1] == target) {
            return new int[]{0, 1};
        }

        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * 方法二，时间换空间，但是内存占用太高，而且借用了java的集合特性
     */
    public int[] l1twoSum2(int[] nums, int target) {
        if (nums == null || nums.length == 1) {
            return new int[]{};
        }

        if (nums.length == 2 && nums[0] + nums[1] == target) {
            return new int[]{0, 1};
        }

        Map<Integer, List<Integer>> valSeqMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            List<Integer> seqList;
            if (valSeqMap.get(nums[i]) == null) {
                seqList = new ArrayList<>();
            } else {
                seqList = valSeqMap.get(nums[i]);
            }
            seqList.add(i);
            valSeqMap.put(nums[i], seqList);
        }

        for (int i = 0; i < nums.length; i++) {
            List<Integer> seqList = valSeqMap.get(target - nums[i]);
            if (seqList == null) {
                continue;
            }
            seqList.remove(new Integer(i));
            if (seqList.size() == 0) {
                continue;
            }

            return new int[]{i, seqList.get(0)};
        }

        return null;
    }

    /**
     * 没有方法三，全部答案我都想到了，就是我写的代码，不够简洁，考虑补够全面，导致性能上稍差
     */
    public int[] l1twoSum3(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                int temp = map.get(target - nums[i]);
                return new int[]{i, temp};
            } else {
                map.put(nums[i], i);
            }
        }
        return new int[]{-1, -1};
    }


    public boolean l9isPalindrome(int x) {
        String xStr = x + "";
        for (int i = 0, j = xStr.length() - 1; i < xStr.length() && j > i; i++, j--) {
            char ci = xStr.charAt(i);
            char cj = xStr.charAt(j);
            if (ci != cj) {
                return false;
            }
        }
        return true;
    }


    public int l13romanToInt(String s) {
        Map<Character, Integer> charValMap = new HashMap<>();
        charValMap.put('I', 1);
        charValMap.put('V', 5);
        charValMap.put('X', 10);
        charValMap.put('L', 50);
        charValMap.put('C', 100);
        charValMap.put('D', 500);
        charValMap.put('M', 1000);

        int answer = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'I') {
                if (i + 1 < s.length() && s.charAt(i + 1) == 'V') {
                    answer += 4;
                    i++;
                } else if (i + 1 < s.length() && s.charAt(i + 1) == 'X') {
                    answer += 9;
                    i++;
                } else {
                    answer += charValMap.get(s.charAt(i));
                }
                continue;
            }
            if (s.charAt(i) == 'X') {
                if (i + 1 < s.length() && s.charAt(i + 1) == 'L') {
                    answer += 40;
                    i++;
                } else if (i + 1 < s.length() && s.charAt(i + 1) == 'C') {
                    answer += 90;
                    i++;
                } else {
                    answer += charValMap.get(s.charAt(i));
                }
                continue;
            }
            if (s.charAt(i) == 'C') {
                if (i + 1 < s.length() && s.charAt(i + 1) == 'D') {
                    answer += 400;
                    i++;
                } else if (i + 1 < s.length() && s.charAt(i + 1) == 'M') {
                    answer += 900;
                    i++;
                } else {
                    answer += charValMap.get(s.charAt(i));
                }
                continue;
            }
            answer += charValMap.get(s.charAt(i));
        }
        return answer;
    }


    public String l14longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }

        StringBuilder answer = new StringBuilder();

        // 第二层循环
        int i = 0;
        A:
        while (true) {
            Character temp = null;
            for (int j = 0; j < strs.length; j++) {
                if (i >= strs[j].length()) {
                    break A;
                }
                if (j == 0) {
                    temp = strs[j].charAt(i);
                    continue;
                }
                if (temp != strs[j].charAt(i)) {
                    break A;
                }
            }
            answer.append(temp);
            i++;
        }

        return answer.toString();
    }


    /**
     * 构建一个堆栈，字符串从左往右，左括号入栈，有括号出栈。出栈时，必须是对应的左括号，才允许出，不然就return false。
     */
    public boolean l20isValid(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }

        LinkedList<Character> stack = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.size() == 0) {
                    return false;
                }
                Character pop = stack.pop();
                if (pop != '(') {
                    return false;
                }
            } else if (c == ']') {
                if (stack.size() == 0) {
                    return false;
                }
                Character pop = stack.pop();
                if (pop != '[') {
                    return false;
                }
            } else if (c == '}') {
                if (stack.size() == 0) {
                    return false;
                }
                Character pop = stack.pop();
                if (pop != '{') {
                    return false;
                }
            }
        }

        return stack.size() == 0;
    }


    /**
     * new一个ListNode返回。
     * while循环，list1与list2只要没遍历到底，就持续复制。
     */
    public ListNode l21mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        ListNode temp = new ListNode();
        if (list1.val < list2.val) {
            temp.val = list1.val;
            list1 = list1.next;
        } else {
            temp.val = list2.val;
            list2 = list2.next;
        }
        ListNode result = temp;

        while (list1 != null || list2 != null) {
            ListNode answer = new ListNode();
            if (list1 == null) {
                answer.val = list2.val;
                list2 = list2.next;
            } else if (list2 == null) {
                answer.val = list1.val;
                list1 = list1.next;
            } else if (list1.val < list2.val) {
                answer.val = list1.val;
                list1 = list1.next;
            } else {
                answer.val = list2.val;
                list2 = list2.next;
            }
            temp.next = answer;
            temp = temp.next;
        }

        return result;
    }


    public int l26removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // sortedSet，得到绝对递增数组，然后system.arrayCopy
        Map<Integer, Integer> sortedMap = new TreeMap<>();
        for (int num : nums) {
            sortedMap.put(num, 0);
        }

        int answer = sortedMap.size();
        Set<Integer> keySet = sortedMap.keySet();
        int i = 0;
        for (Integer key : keySet) {
            nums[i++] = key;
        }

        return answer;
    }


    public int l27removeElement(int[] nums, int val) {
        int equalCount = 0;
        for (int num : nums) {
            if (num == val) {
                equalCount++;
            }
        }
        int k = nums.length - equalCount;

        for (int i = 0; i < nums.length; ) {
            if (nums[i] == val && i < k) {
                System.arraycopy(nums, i + 1, nums, i, nums.length - (i + 1));
            } else {
                i++;
            }
        }
        return k;
    }


    public int l28strStr(String haystack, String needle) {
        if (haystack.equals(needle)) {
            return 0;
        }

        return haystack.indexOf(needle);
    }


    public int l35searchInsert(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        if (target <= nums[left]) {
            return left;
        }
        if (nums[right] == target) {
            return right;
        }
        if (nums[right] < target) {
            return nums.length;
        }
        if (right - left == 1) {
            return right;
        }

        int middle = (left + right) / 2;
        while (middle > left && middle < right) {
            if (nums[middle] == target) {
                return middle;
            }
            if (nums[middle] < target) {
                left = middle;
            }
            if (nums[middle] > target) {
                right = middle;
            }
            if (right - left == 1) {
                return right;
            }
            middle = (left + right) / 2;
        }
        return middle;
    }


    public List<List<String>> l49groupAnagrams(String[] strs) {
        List<List<String>> answer = new ArrayList<>();
        if (strs.length == 1) {
            answer.add(Arrays.asList(strs));
            return answer;
        }

        List<String> firstList = new ArrayList<>();
        firstList.add(strs[0]);
        Map<Character, Integer> firstCharCountMap = this.l49strToCharCountMap(strs[0]);

        Map<Integer, List<String>> noListMap = new HashMap<>();
        noListMap.put(1, firstList);
        Map<Integer, Map<Character, Integer>> noSetMap = new HashMap<>();
        noSetMap.put(1, firstCharCountMap);

        for (int i = 1; i < strs.length; i++) {
            String str = strs[i];
            Map<Character, Integer> strCharCountMap = this.l49strToCharCountMap(str);
            boolean putNoSetMapFlag = true;
            for (Map.Entry<Integer, Map<Character, Integer>> entry : noSetMap.entrySet()) {
                Map<Character, Integer> entryValue = entry.getValue();
                Integer no = entry.getKey();
                if (entryValue.equals(strCharCountMap)) {
                    noListMap.get(no).add(str);
                    putNoSetMapFlag = false;
                    break;
                }
            }
            if (putNoSetMapFlag) {
                int size = noSetMap.size();
                noSetMap.put(size + 1, strCharCountMap);
                List<String> strList = new ArrayList<>();
                strList.add(str);
                noListMap.put(size + 1, strList);
            }
        }
        return new ArrayList<>(noListMap.values());
    }

    private Map<Character, Integer> l49strToCharCountMap(String str) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : str.toCharArray()) {
            map.merge(c, 1, Integer::sum);
        }
        return map;
    }


    public List<Integer> l94inorderTraversal(TreeNode root) {
        List<Integer> valList = new ArrayList<>();
        if (root == null) {
            return valList;
        }

        this.l94doInorderTraversal(root, valList);

        return valList;
    }

    private void l94doInorderTraversal(TreeNode root, List<Integer> list) {
        if (root == null) {
            return;
        }

        l94doInorderTraversal(root.left, list);

        list.add(root.val);

        l94doInorderTraversal(root.right, list);
    }


    public boolean l101isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }

        if (root.left == null && root.right == null) {
            return true;
        }

        if (root.left == null || root.right == null) {
            return false;
        }

        return root.left.val == root.right.val && this.l101isSymmetric(root.left) && this.l101isSymmetric(root.right);
    }


    public int l104maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        if (root.left == null && root.right == null) {
            return 1;
        }

        return Math.max(this.l104maxDepth(root.left), this.l104maxDepth(root.right)) + 1;
    }


    public int l128longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        List<Integer> list = set.stream().sorted(Integer::compareTo).collect(Collectors.toList());
        if (list.size() == 1) {
            return 1;
        }

        int answer = 1, tempCount = 1;
        for (int i = 0, j = i + 1; j < list.size(); i++, j++) {
            if (list.get(j) - list.get(i) == 1) {
                ++tempCount;
                answer = Math.max(answer, tempCount);
                continue;
            }
            tempCount = 1;
        }

        return answer;
    }


    public boolean l141hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }

        int count = 0;
        while (head != null) {
            ++count;
            head = head.next;
            if (count > 10000) {
                return true;
            }
        }

        return false;
    }


    public ListNode l160getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }

        List<ListNode> headAList = l160BuildList(headA);
        List<ListNode> headBList = l160BuildList(headB);

        int i = headAList.size() - 1, j = headBList.size() - 1;
        for (; i >= 0 && j >= 0; i--, j--) {
            if (headAList.get(i) == headBList.get(j)) {
                continue;
            }
            if (i == headAList.size() - 1) {
                return null;
            } else {
                return headAList.get(i + 1);
            }
        }

        return headAList.get(i + 1);
    }

    private List<ListNode> l160BuildList(ListNode head) {
        List<ListNode> list = new ArrayList<>();
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        return list;
    }


    public ListNode l206reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode reverseHead = head;
        while (reverseHead.next != null) {
            reverseHead = reverseHead.next;
        }

        l206doReverse(head, head.next);
        head.next = null;

        return reverseHead;
    }

    private void l206doReverse(ListNode curNode, ListNode nextNode) {
        if (nextNode == null) {
            return;
        }

        l206doReverse(nextNode, nextNode.next);
        nextNode.next = curNode;
    }


    public TreeNode l226invertTree(TreeNode root) {
        if (root == null) {
            return root;
        }

        if (root.left == null && root.right == null) {
            return root;
        }

        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;

        this.l226invertTree(root.left);
        this.l226invertTree(root.right);

        return root;
    }


    public boolean l234isPalindrome(ListNode head) {
        if (head.next == null) {
            return true;
        }

        String str = "";
        ListNode curNode = head;
        while (curNode != null) {
            str += curNode.val;
            curNode = curNode.next;
        }

        return str.equals(new StringBuilder(str).reverse().toString());
    }


    public void l283moveZeroes(int[] nums) {
        if (nums == null || nums.length == 0) {
            return;
        }
        if (nums.length == 1) {
            return;
        }

        for (int i = 0, j = 0; i < nums.length && j < nums.length; ) {
            if (nums[i] != 0) {
                i++;
                continue;
            }
            // j的首次赋值
            if (j < i) {
                j = i + 1;
                continue;
            }

            if (nums[j] == 0) {
                j++;
                continue;
            }

            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
            i++;
            j++;
        }
    }


    public static void main(String[] args) {
        SolutionSecondRound solution = new SolutionSecondRound();

        int[] nums = {0, 0, 0};
        int target = 10;

        String[] strs = new String[]{"hhhhu", "tttti", "tttit", "hhhuh", "hhuhh", "tittt"};

        ListNode head = ListNode.buildListNode(new int[]{1, 2, 2, 1});
        System.out.println(head.next.val);

        System.out.println(solution.l234isPalindrome(head));
    }


}
