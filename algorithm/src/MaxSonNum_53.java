/**
 * 53. 最大子序和
 *
 * description
 * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 */
public class MaxSonNum_53 {
    public int maxSubArray(int[] nums) {
        /**
         * way 动态规划
         * pre 表示以当前第i个数结尾的最大子序和
         */
        int pre =0;int max = nums[0];

        for (int num : nums) {
            pre = Math.max(pre + num, num);
            max = Math.max(max, pre);
        }
        return max;
    }

    public static void main(String[] args) {
        int[] a = new int[]{321,12,3214,-10000,-54444,1000,2000,3000};
        MaxSonNum_53 maxSonNum_53 = new MaxSonNum_53();
        System.out.println(maxSonNum_53.maxSubArray(a));
    }
}
