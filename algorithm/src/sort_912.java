import java.util.Arrays;
import java.util.Random;

/**
 * 912. 排序数组
 * <p>
 * description meduim
 * 给你一个整数数组 nums，请你将该数组升序排列。
 */

public class sort_912 {
    public int[] sortArray(int[] nums) {
        if (nums.length == 0) {
            return nums;
        }
        return quickSort(nums, 0, nums.length - 1);
    }

    public static int[] quickSort(int[] a, int l, int r) {
        if (l < r) {
            int i, j, x;

            //随机主元
            int index = new Random().nextInt(r-l+1) + l;
            swap(a,l,index);//交换后即可用传统方法

            i = l;
            j = r;
            x = a[i];
            while (i < j) {
                while (i < j && a[j] > x) j--; // 从右向左找第一个小于x的数
                if (i < j)  a[i++] = a[j];
                while (i < j && a[i] < x) i++; // 从左向右找第一个大于x的数
                if (i < j)  a[j--] = a[i];
            }
            a[i] = x;
            quickSort(a, l, i - 1); /* 递归调用 */
            quickSort(a, i + 1, r); /* 递归调用 */
        }
        return a;
    }

    public static void swap(int[]nums,int i,int j){
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void main(String[] args) {
        sort_912 sort_912 = new sort_912();
        System.out.println(Arrays.toString(sort_912.sortArray(new int[]{30, 40, 60, 10, 20, 50})));
        System.out.println(new Random().nextInt(11));
    }
}
