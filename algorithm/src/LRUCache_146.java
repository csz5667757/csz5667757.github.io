import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
public class LRUCache_146 {

    /**
     * way1 使用linkedHashMap 设置key的顺序为访问顺序
     */
    private int capacity;
    public Map<Integer, Integer> cache;

    public LRUCache_146(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity,0.75F,true);
    }

    public int get(int key) {
        if (!cache.containsKey(key)) return -1;
        return cache.get(key);
    }

    public void put(int key, int value) {
        cache.put(key,value);
        if (cache.size()>capacity){
            Iterator<Integer> iterator = cache.keySet().iterator();
            cache.remove(iterator.next());
        }
    }
}
