import java.util.Arrays;

/**
 * 518. 零钱兑换 II
 * <p>
 * description medium
 * 给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
 * 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0 。
 * 假设每一种面额的硬币有无限个。
 * 题目数据保证结果符合 32 位带符号整数。
 */


/**
 * way 动态规划
 * 1. 状态转移定义
 * 2. 状态转移方程
 * 3. 考虑状态边界
 */
public class CoinsExchange_518 {
        public int change(int amount, int[] coins) {
            //dp[amount]表示总金额为amount时最大的组合数
            int[] dp = new int[amount + 1];
            dp[0] = 1;
            for (int coin : coins) {
                for (int i = coin; i <= amount; i++) {
                    dp[i] += dp[i - coin];
                }
            }
            return dp[amount];
        }

    public static void main(String[] args) {
        int[] coins = new int[]{1,2,5};
        CoinsExchange_518 coinsExchange_518 = new CoinsExchange_518();
        System.out.println(coinsExchange_518.change(5,coins));
    }

}
