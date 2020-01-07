package ca.jrvs.practice.codingChallenge;

public class removeElement {

  /**
   * Big-O: O(n) due to double pointer method used
   * @param nums
   * @param val
   * @return
   */
  public int remElement(int[] nums, int val) {
    if (nums.length == 0) {
      return 0;
    }
    int last = nums.length - 1, first = 0;

    while (first < last) {
      // loop last pointer and update position (O(n) partial operation)
      last = updateLast(nums, last, val);

      // O(n) partial operation
      if (nums[first] == val && last > first) {
        swap(nums, first, last);
        last = updateLast(nums, last, val);
      }
      first++;
    }

    //final confirmative check
    if (first == last && nums[last] == val) {
      last--;
    }

    return last + 1;
  }

  /**
   * Update newSize of array position from nums Array endpoint perspective
   *
   * @param nums nums Array to update in-Place
   * @param last Array last position
   * @param val  value to compare to
   * @return updated last pointer
   */
  public int updateLast(int[] nums, int last, int val) {
    while (nums[last] == val) {
      last--;
      if (last < 0) {
        break;
      }
    }
    return last;
  }

  /**
   * Swaps nums left index with nums right index
   *
   * @param nums  Array that contains all data
   * @param left  left index of Array
   * @param right right index of Array
   */
  public void swap(int[] nums, int left, int right) {
    int temp = nums[left];
    nums[left] = nums[right];
    nums[right] = temp;
  }

}
