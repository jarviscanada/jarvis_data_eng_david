package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class findDuplicate {

  public int findDuplicateSort(int[] nums) {
    Arrays.sort(nums);
    for (int i = 0; i < nums.length - 1; i++) {
      if(nums[i] == nums[i+1]) return nums[i];
    }
    return -1; //under assumption will always be duplicate
  }

  public int findDuplicateSet(int[] nums) {

    Set<Integer> holdValue = new HashSet<Integer>();

    for (int val : nums) {
      if (holdValue.contains(val)) return val;
      holdValue.add(val);
    }
    return -1; //under assumption will always be duplicate
  }
}
