import java.util.*;

/**
 * 15.运用你所掌握的数据结构，设计和实现一个  LRU (最近最少使用) 缓存机制 。
 * <p>
 * description medium
 * <p>
 * 1. LRUCache(int capacity) 以正整数作为容量capacity 初始化 LRU 缓存
 * 2. int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 。
 * 3. void put(int key, int value)如果关键字已经存在，则变更其数据值；如果关键字不存在，则插入该组「关键字-值」。
 * 当缓存容量达到上限时，它应该在写入新数据之前删除最久未使用的数据值，从而为新的数据值留出空间。
 */
public class LRUCache_146_2 {

    /**
     * way2 哈希表+双向链表
     */
    private int capacity;
    private int size;
    private Map<Integer,LinkedNode> cache;

    private LinkedNode head;//虚拟表头
    private LinkedNode tail;//虚拟表尾

    public LRUCache_146_2(int capacity) {
        this.capacity = capacity;
        size = 0;
        cache = new HashMap<>();
        //创建虚拟表头
        head = new LinkedNode();
        //创建虚拟表尾
        tail = new LinkedNode();
        //表头和表尾互指
        head.next = tail;
        tail.pre = head;
    }

    public int get(int key) {
        if (!cache.containsKey(key)) return -1;
        LinkedNode current = cache.get(key);
        moveToHead(current);
        return current.val;
    }

    public void put(int key, int value) {
        if (!cache.containsKey(key)){
            LinkedNode LinkedNode = new LinkedNode(key, value);
            addToHead(LinkedNode);
            cache.put(key,LinkedNode);
            size++;
            if (size > capacity){
                LinkedNode removedTail = removeTailNode();
                cache.remove(removedTail.key);
                size--;
            }
        }else{
            LinkedNode LinkedNode = cache.get(key);
            LinkedNode.val=value;
            moveToHead(LinkedNode);
        }
    }

    // 移动到队列头
    void moveToHead(LinkedNode node){
        // 先删除再放在头部
        removeNode(node);
        addToHead(node);
    }

    // 删除一个缓存
    void removeNode(LinkedNode node){
        // 将 node 的 pre 指向 next
        node.pre.next = node.next;
        // 将 node 的 next 指向 pre
        node.next.pre = node.pre;
    }

    // 添加到头部
    void addToHead(LinkedNode node){
        node.next = head.next;
        node.pre = head;
        head.next.pre = node;
        head.next = node;
    }

    // 删除尾巴的缓存
    LinkedNode removeTailNode(){
        // 拿到真实的尾部 node
        LinkedNode realTailNode = tail.pre;
        // 将 node 的 pre 指向尾巴
        realTailNode.pre.next = tail;
        // 将尾巴的 pre 指向 node 的 pre
        tail.pre = realTailNode.pre;
        return realTailNode;
    }

    class LinkedNode {
        int val;
        int key;
        LinkedNode pre;
        LinkedNode next;
        LinkedNode(){}
        LinkedNode(int key, int val){
            this.key = key;
            this.val =val;
        }
    }
}
