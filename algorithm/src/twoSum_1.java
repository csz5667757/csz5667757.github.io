import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 1.两数之和
 * <p>
 * description:
 * 给定一个整数数组 nums和一个整数目标值 target，请你在该数组中找出 和为目标值 target 的那两个整数，并返回它们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 * 你可以按任意顺序返回答案。
 */


public class twoSum_1 {
    public int[] twoSum(int[] nums, int target) {

        /**
         * way2 哈希表
         * 由于方法一在寻找target-x的时候时间复杂度过高，这里使用哈希表能够快速定位到target-x 时间复杂度为O(1)
         */
//        Map<Integer, Integer> map = new HashMap<>();
//        for (int i = 0; i < nums.length; i++) {
//            if (map.containsKey(target - nums[i])) {
//                return new int[]{map.get(target - nums[i]), i};
//            } else {
//                map.put(nums[i], i);
//            }
//        }
//        return new int[]{};


        /**
         * way3 双指针
         */
        int[] temp = nums.clone();
        int length = nums.length;
        Arrays.sort(nums);
        int left = 0;
        int right = length - 1;
        while (left < right) {
            if (nums[left] + nums[right] == target) {
                break;
            } else if (nums[left] + nums[right] < target) {
                left++;
            } else if (nums[left] + nums[right] > target) {
                right--;
            }
        }
        int[] result = new int[2];
        boolean b = true;
        for (int i = 0; i < temp.length; i++) {
            if (b && temp[i] == nums[left]) {
                b = false;
                result[0] = i;
                continue;
            }
            if (temp[i] == nums[right]) {
                result[1] = i;
            }
        }
        return result;

        /**
         * way1 暴力解法
         */
//        for (int i = 0; i < nums.length; i++) {
//            for (int i1 = i + 1; i1 < nums.length; i1++) {
//                if (nums[i] + nums[i1] == target) {
//                    return new int[]{i, i1};
//                }
//
//        }
//        return new int[]{};
    }

    public static void main(String[] args) {
        twoSum_1 twoSum_1 = new twoSum_1();
        System.out.println(twoSum_1.twoSum(new int[]{2, 5, 5, 11}, 10));
    }
}
