
import java.util.HashSet;
import java.util.Set;

/**
 * 160. 相交链表
 * <p>
 * description
 */

public class IntersectionListNode_160 {

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {

        /**
         * way2 双指针
         */
        if (headA==null||headB==null){
            return null;
        }
        ListNode pA = headA;
        ListNode pB = headB;
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }
        return pA;

        /**
         * way1 哈希集合
         */
//        Set<ListNode> set = new HashSet<>();
//        ListNode temp = headA;
//        ListNode tempB = headB;
//        while (temp !=null){
//            set.add(temp);
//            temp = temp.next;
//        }
//        while (tempB!=null){
//            if (set.contains(tempB)){
//                return tempB;
//            }
//            tempB = tempB.next;
//        }
//        return null;
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }
}
