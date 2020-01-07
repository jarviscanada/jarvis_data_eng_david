package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class containsDuplicate {

  /**
   * Big-O: O(nlogn)
   * Justification: Use of sort here is gating factor: process takes O(nlogn)
   */
  public boolean isDuplicate(int[] inputArray) {
    Arrays.sort(inputArray);

    for (int i = 0; i < inputArray.length - 1; i++) {
      if (inputArray[i] == inputArray[i+1]) return true;
    }
    return false;
  }

  /**
   * Big-O: O(n)
   * Justification: Set lookup/insertion is O(1). Gating factor is loop of O(n).
   */
  public boolean optIsDuplicate(int[] inputArray) {
    Set<Integer> holdValue = new HashSet<Integer>();

    for (int val : inputArray) {
      if (holdValue.contains(val)) return true;
      holdValue.add(val);
    }
    return false;
  }

  public static void main(String[] args) {

  }
}
