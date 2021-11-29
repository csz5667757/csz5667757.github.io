/**
 * 121. 买卖股票的最佳时机
 * <p>
 * description
 * <p>
 * 给定一个数组 prices ，它的第i 个元素prices[i] 表示一支给定股票第 i 天的价格。
 * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
 */

public class stockBestChance_121 {

    public int maxProfit(int[] prices) {

        /**
         * way 总是假设自己总是在历史最低点购入股票就好，是一种动态规划的思路
         */
        int min = Integer.MAX_VALUE;
        int result = 0;
        int length = prices.length;
        for (int i = 0; i < length; i++) {
            if (prices[i]<min){
                min = prices[i];
            }
            if (prices[i] -min>result){
                result = prices[i] -min;
            }
        }
        return result;

        //暴力解法
//        int length = prices.length;
//        int max = 0;
//        for (int n = 0; n < length; n++) {
//            int afterMax = prices[n];
//            for (int i = n + 1; i < length; i++) {
//                afterMax = Math.max(prices[i], afterMax);
//            }
//            max = Math.max(prices[n] - afterMax, max);
//        }
//        return max;

    }
}
