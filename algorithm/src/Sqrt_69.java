/**
 * 69. x 的平方根
 * <p>
 * description easy
 * 实现int sqrt(int x)函数。
 * 计算并返回x的平方根，其中x 是非负整数。
 * 由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
 */
public class Sqrt_69 {
    /**
     * way 二分查找
     */
    public int mySqrt(int x) {
        int left = 0, right = x, result = -1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if ((long)mid * mid <= x) {
                result = mid;
                left = mid+1;
            } else {
                right = mid;
            }
        }
        return result;
    }

    /**
     * 牛顿迭代法
     * @param x
     * @return
     */
    public int mySqrt1(int x) {
        long a = x;
        while (a *a > x){
            a = (a+x/a)/2;
        }
        return (int)a;
    }


        public static void main(String[] args) {
        Sqrt_69 sqrt_69 = new Sqrt_69();
        System.out.println(sqrt_69.mySqrt(32432423));
        System.out.println(sqrt_69.mySqrt1(32432423));
        System.out.println(1024>>>1);
        System.out.println(1024>>1);
    }
}
