package dp_backpack;

import java.util.Arrays;

/**
 * 416. 分割等和子集
 * <p>
 * description medium
 * <p>
 * 给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
 */
public class SubNums_416 {
    public static boolean canPartition(int[] nums) {
        /**
         * way 01背包
         */
        int sum = Arrays.stream(nums).sum();
        //判断nums的总和是否是2的倍数
        if (nums.length == 0 || (sum & 1) > 0) {
            return false;
        }
        //将问题转化为找子数组求是否有目标值问题
        int target = sum / 2;
        //Step 01 状态转移定义
        //考虑前i个物品，背包容量是否等于给定容量(target)
        boolean[] np = new boolean[target + 1];
        //初始化，使用哨兵思想
        np[0] = true;
        for (int i = 1; i <= nums.length; i++) {
            int num = nums[i-1];
            for (int j = target; j >= 0; j--) {
                //不选择该物品
                boolean no = np[j];
                //选择该物品
                boolean yes = j >= target && np[j - num];
                np[j] = no||yes;
            }
        }
        return np[target];
    }

    public static void main(String[] args) {
        System.out.println(canPartition(new int[]{1, 5, 11, 5}));
    }
}
