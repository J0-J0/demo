package com.jojo.leetcode;

import com.alibaba.fastjson.JSON;
import com.jojo.leetcode.node.ListNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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


    public static void main(String[] args) {
        SolutionSecondRound solution = new SolutionSecondRound();

        int[] nums = {1,3};
        int target = 10;

        String[] strs = new String[]{"cir", "car"};

        ListNode list1 = solution.buildListNode1();
        ListNode list2 = solution.buildListNode2();

        System.out.println(solution.l35searchInsert(nums, 2));
    }

    private ListNode buildListNode1() {
        ListNode node = new ListNode();
        node.val = 1;
        node.next = new ListNode();
        node.next.val = 2;
        node.next.next = new ListNode();
        node.next.next.val = 4;
        return node;
    }

    private ListNode buildListNode2() {
        ListNode node = new ListNode();
        node.val = 1;
        node.next = new ListNode();
        node.next.val = 3;
        node.next.next = new ListNode();
        node.next.next.val = 4;
        return node;
    }

}
