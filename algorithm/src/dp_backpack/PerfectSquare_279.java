package dp_backpack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 279. 完全平方数
 *
 * description
 *
 * 给定正整数n，找到若干个完全平方数（比如1, 4, 9, 16, ...）使得它们的和等于 n。你需要让组成和的完全平方数的个数最少。
 * 给你一个整数 n ，返回和为 n 的完全平方数的 最少数量 。
 * 完全平方数 是一个整数，其值等于另一个整数的平方；换句话说，其值等于一个整数自乘的积。例如，1、4、9 和 16 都是完全平方数，而 3 和 11 不是。
 */
public class PerfectSquare_279 {
    public static int numSquares(int n) {
        /**
         * way 完全背包解法 dp
         */
        List<Integer> nums = new ArrayList<>();
        for (int i =1 ;i*i<n;i++){
            nums.add(i*i);
        }
        int[] dp = new int[n+1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int num : nums) {
            for (int j = num; j <= n; j++) {
                dp[j] = Math.min(dp[j], dp[j - num] + 1);
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        System.out.println(numSquares(12));
    }
}
