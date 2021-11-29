package dp_backpack;

import java.util.Arrays;

/**
 * 494. 目标和
 * <p>
 * 给你一个整数数组 nums 和一个整数 target 。
 * 向数组中的每个整数前添加'+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
 * 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，然后串联起来得到表达式 "+2-1" 。
 * 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。
 */
public class TargetSum_494 {
    /**
     * way 01背包的组合问题
     */
    public int findTargetSumWays(int[] nums, int a) {
        int sum = Arrays.stream(nums).sum();
        if ((sum + a) % 2 != 0 || a < 0) {
            return 0;
        }
        int target = (sum + a) / 2;
        int[] dp = new int[target + 1];
        dp[0] = 1;
        for (int num : nums) {
            for (int i = target; i >=0; i--) {
                if (i < num) continue;
                dp[i] = dp[i] + dp[i - num];
            }
        }
        return dp[target];
    }

}
