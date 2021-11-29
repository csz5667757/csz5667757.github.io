/**
 * 70. 爬楼梯
 *
 * description
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 * 注意：给定 n 是一个正整数
 */
public class ClimbStairs_70 {
    /**
     * way 动态规划 斐波那契数列问题
     * 状态方程为 f(x) = f(x-1)+f(x-2)
     */
    public int climbStairs(int n) {
        if (n<3){
            return n;
        }
        int[] dp = new int[n+1];
        dp[0]=0;
        dp[1]=1;
        dp[2]=2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i-1]+dp[i-2];
        }
        return dp[n];
    }

    /**
     * 优化——滚动数组
     */
    public int climbStairs1(int n) {
        if (n<3){
            return n;
        }
        int first=1;
        int second=2;
        for (int i = 3; i <= n; i++) {
            int temp = first+second;
            first = second;
            second =temp;
        }
        return second;
    }
}
