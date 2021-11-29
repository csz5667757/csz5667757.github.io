/**
 * 25. K 个一组翻转链表 difficult
 * <p>
 * description
 * 1.k 是一个正整数，它的值小于或等于链表的长度。
 * 2.如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 */
public class reverseKListNode_25 {

    public ListNode reverseKListNode(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode pre = dummy;
        ListNode end = dummy;

        // 如果 end=null 则完成对链表的遍历
        while (end.next != null) {
            // 找到当前 k 段的末尾节点
            for (int i = 0; i < k && end != null; i++) end = end.next;
            // 不够 k 就不翻转
            if (end == null) break;
            // 需要翻转的内容的开始部分是前驱节点的 next 部分
            ListNode start = pre.next;
            // 暂存后驱节点，因为要剪断
            ListNode next = end.next;
            // 剪断当前 k 段
            end.next = null;
            // 翻转
            pre.next = reverseListNode(start);
            // 此时 start 是尾部 null->start，接上后驱节点
            start.next = next;
            // 重置 pre 与 end 的位置
            pre = start;
            end = pre;
        }
        // 借助虚拟头返回
        return dummy.next;
    }

    public ListNode reverseListNode(ListNode head){
        ListNode pre =null;
        ListNode heads =head;
        while (heads!=null){
            ListNode next = heads.next;
            heads.next = pre;
            pre = heads;
            heads = next;
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
