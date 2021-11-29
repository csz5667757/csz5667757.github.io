/**
 * 213. 打家劫舍 II
 * <p>
 * description
 * 你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。这个地方所有的房屋都 围成一圈 ，
 * 这意味着第一个房屋和最后一个房屋是紧挨着的。同时，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警 。
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你 在不触动警报装置的情况下 ，今晚能够偷窃到的最高金额。
 */
public class Thief2_213 {
    public int rob(int[] nums) {
        int first = nums[0];
        if (nums.length == 1) {
            return first;
        }
        int second = Math.max(first, nums[1]);
        if (nums.length == 2) {
            return second;
        }
        int third = Math.max(nums[1], nums[2]);
        int s2 = nums[1];
        return Math.max(getNums(nums, 2, nums.length - 2, first, second), getNums(
                nums, 3, nums.length - 1, s2, third
        ));
    }

    public int getNums(int[] nums, int start, int end, int first, int second) {
        for (int i = start; i <= end; i++) {
            int temp = Math.max(first + nums[i], second);
            first = second;
            second = temp;
        }
        return second;
    }

    public static void main(String[] args) {
        Thief2_213 thief2_213 = new Thief2_213();
        System.out.println(thief2_213.rob(new int[]{2, 1, 1, 2}));
    }
}
