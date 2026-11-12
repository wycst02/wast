package io.github.wycst.wast.common.beans;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 固定长度的队列Map,先进先出
 *
 * @since  2024/10/22 18:29
 * @author wangyc
 */
public final class ArrayQueueMap<K, V> {

    final Object[] keys;
    final Map<K, V> map;
    final int size;
    int count;

    public ArrayQueueMap(int size) {
        keys = new Object[size];
        map = new ConcurrentHashMap<K, V>(size);
        this.size = size;
    }

    public V get(K k) {
        return map.get(k);
    }

    public synchronized void put(K k, V v) {
        if (map.put(k, v) == null) {
            int index = count;
            if (++count == size) {
                count = 0;
            }
            Object oldKey = keys[index];
            if (oldKey != null) {
                map.remove(oldKey);
            }
            keys[index] = k;
        }
    }

    public synchronized void clear() {
        count = 0;
        map.clear();
        Arrays.fill(keys, null);
    }
}
