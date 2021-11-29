import java.util.*;
import java.util.stream.Collectors;

/**
 * 205. 数组中的第K个最大元素
 * <p>
 * description
 * 在未排序的数组中找到第 k 个最大的元素。请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 */
public class findKMax_215 {
    public int findKthLargest(int[] nums, int k) {
        /**
         * way 使用优先队列构建小顶堆
         */
        if (nums == null || nums.length == 0 || k < 1 || k > nums.length) return 0;
        PriorityQueue<Integer> queue = new PriorityQueue<>(k, Comparator.comparingInt(a -> a));
        for (int i = 0; i < k; i++) {
            queue.add(nums[i]);
        }
        for (int j = k; j < nums.length; j++) {
            Integer minElement = queue.peek();
            if (nums[j] >= minElement) {
                queue.poll();
                queue.offer(nums[j]);
            }
        }
        return queue.peek();
    }

    public int findKthLargest1(int[] nums, int k) {
        //暴力排序，不推荐
        if (nums.length < 1 || k > nums.length) return 0;
        List<Integer> collect = Arrays.stream(nums).boxed().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return collect.get(k-1);
    }

    public static void main(String[] args) {
        findKMax_215 findKMax_215 = new findKMax_215();
        int[] a = new int[]{3,2,3,1,2,4,5,5,6};
        System.out.println(findKMax_215.findKthLargest(a,4));
        System.out.println(findKMax_215.findKthLargest1(a,4));
    }
}
