/**
 * 206. 反转链表 easy
 * <p> pass
 * description:
 * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
 */

public class reverseListNode_206 {

    /**
     * way 使用迭代的方式
     */
    public ListNode reverseListNode(ListNode head) {
        if (head.next == null) return head;
        //创建一个新的链表用于存储翻转后的节点
        ListNode pre = null;
        ListNode node = head;
        while (node != null) {
            ListNode next = node.next;
            ListNode current = node;
            current.next = pre;
            pre = current;
            node = next;
        }
        return pre;
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

