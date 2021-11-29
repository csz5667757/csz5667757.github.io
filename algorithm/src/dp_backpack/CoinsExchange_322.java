package dp_backpack;

import java.util.Arrays;

/**
 * 322. 零钱兑换
 * <p>
 * description medium
 * 给定不同面额的硬币 coins 和一个总金额 amount。编写一个函数来计算可以凑成总金额所需的最少的硬币个数。
 * 如果没有任何一种硬币组合能组成总金额，返回 -1。
 * 你可以认为每种硬币的数量是无限的。
 */

/**
 * 模板
 *
 * 分类解题模板
 * 背包问题大体的解题模板是两层循环，分别遍历物品nums和背包容量target，然后写转移方程，
 * 根据背包的分类我们确定物品和容量遍历的先后顺序，根据问题的分类我们确定状态转移方程的写法
 *
 * 背包问题分类：
 * 常见的背包类型主要有以下几种：
 * 1、0/1背包问题：每个元素最多选取一次
 * 2、完全背包问题：每个元素可以重复选择
 * 3、组合背包问题：背包中的物品要考虑顺序
 * 4、分组背包问题：不止一个背包，需要遍历每个背包
 *
 * 而每个背包问题要求的也是不同的，按照所求问题分类，又可以分为以下几种：
 * 1、最值问题：要求最大值/最小值
 * 2、存在问题：是否存在…………，满足…………
 * 3、组合问题：求所有满足……的排列组合
 *
 * 首先是背包分类的模板：
 * 1、0/1背包：外循环nums,内循环target,target倒序且target>=nums[i];
 * 2、完全背包：外循环nums,内循环target,target正序且target>=nums[i];
 * 3、组合背包(考虑顺序)：外循环target,内循环nums,target正序且target>=nums[i];
 * 4、分组背包：这个比较特殊，需要三重循环：外循环背包bags,内部两层循环根据题目的要求转化为1,2,3三种背包类型的模板
 *
 * 然后是问题分类的模板：
 * 1、最值问题: dp[i] = max/min(dp[i], dp[i-nums]+1)或dp[i] = max/min(dp[i], dp[i-num]+nums);
 * 2、存在问题(bool)：dp[i]=dp[i]||dp[i-num];
 * 3、组合问题：dp[i]+=dp[i-num];
 */
public class CoinsExchange_322 {
    public int coinChange(int[] coins, int amount) {
        /**
         * way 完全背包
         */
        int max = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, max);
        dp[0] = 0;
        for (int coin : coins) {
            for (int i = 0; i <= amount; i++) {
                if (coin > i) continue;
                dp[i] = Math.min(dp[i],dp[i-coin]+1);
            }
        }
        return (dp[amount]>amount)?-1:dp[amount];

    }

    public static void main(String[] args) {
        CoinsExchange_322 coinsExchange_322 = new CoinsExchange_322();
        int[] ints = {2};
        System.out.println(coinsExchange_322.coinChange(ints, 10));
    }
}
