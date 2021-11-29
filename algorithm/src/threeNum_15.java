import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 15. 三数之和
 * <p>
 * description
 * 给你一个包含 n 个整数的数组nums，判断nums中是否存在三个元素 a，b，c ，
 * 使得 + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
 */

public class threeNum_15 {
    /**
     * way 使用排序+双指针 时间复杂度为O（n2）
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int length = nums.length;
        //快排 O(nlog(n))
        Arrays.sort(nums);
        for (int first = 0; first < length; first++) {
            if (nums[first] > 0) break;
            if ((first > 0 && nums[first] == nums[first - 1])) {
                continue;
            }
            int target = -nums[first];
            int third = length - 1;
            int second = first + 1;
            while (second < third) {
                if (nums[second] + nums[third] == target) {
                    List<Integer> lists = new ArrayList<>();
                    lists.add(nums[first]);
                    lists.add(nums[second]);
                    lists.add(nums[third]);
                    result.add(lists);
                    third--;
                    second++;
                    while (second < third && nums[second] == nums[second - 1]) second++;
                    while (second < third && nums[third] == nums[third + 1]) third--;
                } else if (nums[second] + nums[third] > target) {
                    third--;
                } else if (nums[second] + nums[third] < target) {
                    second++;
                }
            }
        }
        return result;

    }
}
