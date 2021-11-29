/**
 * 198. 打家劫舍
 *
 * description
 *
 * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
 * 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
 */
public class Thief_198 {
    public int rob(int[] nums) {
        int[] result = new int[nums.length];
        if (nums.length==1){
            return nums[0];
        }
        int second = Math.max(nums[0], nums[1]);
        if (nums.length==2){
            return second;
        }
        result[0] = nums[0];
        result[1] = second;
        for (int i = 2; i < nums.length-1; i++) {
            result[i] = Math.max(result[i-2]+nums[i],result[i-1]);
        }
        int a = result[nums.length-2];

        for (int i = 3; i < nums.length; i++) {
            result[i] = Math.max(result[i-2]+nums[i],result[i-1]);
        }
        return result[nums.length-1];
    }

    //使用滚动数组
    public int rob1(int[] nums){
        int first = nums[0];
        if (nums.length == 1) {
            return first;
        }
        int second = Math.max(first, nums[1]);
        if (nums.length == 2) {
            return second;
        }
        int results = 0;
        for (int i = 2; i < nums.length; i++) {
            results = Math.max(first + nums[i], second);
            first = second;
            second = results;
        }
        return results;
    }
}
