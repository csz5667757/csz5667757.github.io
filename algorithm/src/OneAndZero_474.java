import java.util.*;

/**
 * 474. 一和零
 *
 * description medium
 *
 * 给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
 * 请你找出并返回 strs 的最大子集的大小，该子集中 最多 有 m 个 0 和 n 个 1 。
 * 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。
 */

public class OneAndZero_474 {
    public static int findMaxForm(String[] strs, int m, int n) {
        Set<String> result =new HashSet<>();
        for (String str : strs) {
            int a0 = 0;
            int a1 = 0;
            char[] chars = str.toCharArray();
            for (char aChar : chars) {
                if (aChar=='0'){
                    a0++;
                    if (a0>m){
                        break;
                    }
                }
                if (aChar=='1'){
                    a1++;
                    if (a1>n){
                        break;
                    }
                }
            }
            if (a0>m||a1>n){
                continue;
            }
            result.add(Arrays.toString(chars));
        }
        return result.size();
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());

    }
}
