import java.util.*;
import java.util.stream.Collectors;

/**
 * 155.最小栈
 *
 * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
 * push(x) —— 将元素 x 推入栈中。
 * pop()—— 删除栈顶的元素。
 * top()—— 获取栈顶元素。
 * getMin() —— 检索栈中的最小元素。
 */
public class minStack_155 {
    Deque<Integer> a;
    Deque<Integer> minStack;
    public minStack_155() {
        a = new LinkedList<>();
        minStack = new LinkedList<>();
        minStack.push(Integer.MAX_VALUE);
    }

    public void push(int val) {
        a.push(val);
        minStack.push(Math.min(val,minStack.peek()));
    }

    public void pop() {
        a.pop();
        minStack.pop();
    }

    public int top() {
        return a.peek();
    }

    public int getMin() {
        return minStack.peek();
    }
}
