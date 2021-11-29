package dp_backpack;

/**
 * 377. 组合总和 Ⅳ
 *
 * description
 * 给你一个由 不同 整数组成的数组 nums ，和一个目标整数 target 。请你从 nums 中找出并返回总和为 target 的元素组合的个数。
 * 题目数据保证答案符合 32 位整数范围。
 */
public class Combination4_377 {
    /**
     * way 完全背包的组合问题
     */
    public int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target+1];
        dp[0] = 1;
        for (int i = 0; i <= target; i++) {
            for (int num : nums) {
                if (i< num) continue;
                dp[i] = dp[i] +dp[i-num];
            }
        }
        return dp[target];
    }
}
