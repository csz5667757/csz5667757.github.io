import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 415. 字符串相加 easy
 *
 * description
 * 给定两个字符串形式的非负整数 num1 和num2 ，计算它们的和。
 */

public class StringNum_415 {
    /**
     * way 模拟算数计算
     * @param num1
     * @param num2
     * @return
     */
    public String addStrings(String num1, String num2) {
        char[] char1 = num1.toCharArray();
        char[] char2 = num2.toCharArray();
        int max = Math.max(char1.length, char2.length);
        int a = char1.length==max?max-char2.length:max-char1.length;
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < a; i++) {
            s.append("0");
        }
        if (char1.length!=max){
            char1 = (s+num1).toCharArray();
        }
        if (char2.length!=max){
            char2 = (s+num2).toCharArray();
        }
        boolean isAddOne =false;
        StringBuilder result = new StringBuilder();
        for (int i = max-1; i >=0 ; i--) {
            int i1 = char1[i]-'0'+ char2[i]-'0';
            if (isAddOne) i1++;
            if (i1>=10){
                isAddOne =true;
                i1 = i1%10;
            }else {
                isAddOne =false;
            }
            result.insert(0,i1);
        }
        if (isAddOne){
            result.insert(0,"1");
        }

        return result.toString();
    }

    public static void main(String[] args) {
        StringNum_415 stringNum_415 = new StringNum_415();
        String s = stringNum_415.addStrings("321321312312321341429018098201",
                "321309821908930821809380921893812");
        System.out.println(s);
        int i = '7' - '0';
        int i1 = '7' + '9';
        System.out.println(i);
        System.out.println(i1);
    }

}
