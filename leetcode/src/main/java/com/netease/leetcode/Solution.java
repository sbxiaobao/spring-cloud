package com.netease.leetcode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/29
 */
public class Solution {

//    public int[] twoSum(int[] nums, int target) {
//        int[] ret = new int[2];
//        int length = nums.length;
//        for (int i = 0; i < length - 1; i++) {
//            for (int j = i + 1; j < length; j++) {
//                if (nums[i] + nums[j] == target) {
//                    ret[0] = nums[i];
//                    ret[1] = nums[j];
//                    return ret;
//                }
//            }
//        }
//        return ret;
//    }

//    public int[] twoSum(int[] nums, int target) {
//        Map<Integer, Integer> map = new HashMap<>();
//        for (int i = 0; i < nums.length; i++) {
//            map.put(nums[i], i);
//        }
//        for (int i = 0; i < nums.length; i++) {
//            int complement = target - nums[i];
//            if (map.containsKey(complement)) {
//                return new int[]{i, map.get(complement)};
//            }
//        }
//        throw new IllegalArgumentException("No two sum solution");
//    }

//    public int[] twoSum(int[] nums, int target) {
//        Map<Integer, Integer> map = new HashMap<>();
//        for (int i = 0; i < nums.length; i++) {
//            int complement = target - nums[i];
//            if (map.containsKey(complement)) {
//                return new int[]{map.get(complement), i};
//            }
//            map.put(nums[i], i);
//        }
//        throw new IllegalArgumentException("No two sum solution");
//    }

    public class ListNode {
        int val;
        ListNode next;

        public ListNode(int x) {
            this.val = x;
        }
    }

    public static void main(String[] args) {
//        Solution solution = new Solution();
//        int[] nums = new int[]{2, 7, 11, 15};
//        int ret[] = solution.twoSum(nums, 9);
//        for (int i = 0; i < ret.length; i++) {
//            System.out.println(ret[i]);
//        }
    }
}
