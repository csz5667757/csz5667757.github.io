import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 20.有效的括号
 * <p>
 * description
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
 * 有效字符串需满足：
 * 1. 左括号必须用相同类型的右括号闭合。
 * 2. 左括号必须以正确的顺序闭合。
 */
public class VaildBracket_20 {
    public boolean isValid(String s) {
        if (s.length() % 2 != 0) return false;
        char[] chars = s.toCharArray();
        Deque<Character> deque = new LinkedList<>();
        for (char aChar : chars) {
            if (aChar == '(' || aChar == '[' || aChar == '{') {
                deque.push(aChar);
                continue;
            }
            if (')' == aChar) {
                if (deque.size() > 0 && deque.peek() == '(') {
                    deque.pop();
                } else {
                    return false;
                }
            } else if ('}' == aChar) {
                if (deque.size() > 0 && deque.peek() == '{') {
                    deque.pop();
                } else {
                    return false;
                }
            } else if (']' == aChar) {
                if (deque.size() > 0 && deque.peek() == '[') {
                    deque.pop();
                } else {
                    return false;
                }
            }
        }
        return deque.size() == 0;
    }

    public static void main(String[] args) {
        VaildBracket_20 vaildBracket_20= new VaildBracket_20();
        System.out.println(vaildBracket_20.isValid("{[()]}"));
        System.out.println(vaildBracket_20.isValid("[]{}()"));
        System.out.println(vaildBracket_20.isValid("[{]}()"));
    }
}