/**
 * 21.合并两个有序链表
 */
public class MergeSortedListNode_21 {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode result = new ListNode();
        ListNode pre = result;
        while (l1 != null && l2 != null) {
            int val1 = l1.val;
            int val2 = l2.val;
            if (val1>=val2){
                pre.next = l2;
            }else {
                pre.next = l1;
            }
            l1=val1>=val2?l1:l1.next;
            l2=val1<val2?l2:l2.next;
            pre = pre.next;
        }
        pre.next = l1!=null?l1:l2;
        return result.next;
    }

    /**
     * Definition for singly-linked list.
     */
    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
