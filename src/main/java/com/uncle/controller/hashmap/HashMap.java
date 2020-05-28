package com.uncle.controller.hashmap;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨戬
 * @className HashMap
 * @email uncle.yeung.bo@gmail.com
 * @date 2020/5/27 22:41
 */
public class HashMap<K, V> implements Map<K, V> {
    /**
     * 初始化空间
     */
    private final static int SIZE_TMP = 16;
    /**
     * 负载因子
     */
    private final static float EXPANSION_OF_MULTIPLES = 0.75f;
    /**
     * 链表
     */
    transient Node<K, V>[] table;
    /**
     * 空间大小
     */
    transient int size;
    /**
     * 改变次数
     */
    transient int theNumberOfChanges;

    public HashMap() {
        this.theNumberOfChanges = 1;
        this.table = new Node[SIZE_TMP];
    }

    /**
     * put
     *
     * @param k
     *            k
     * @param v
     *            v
     * @return v
     */
    @Override
    public V put(K k, V v) {
        return putVal(k, v, getIndex(k, table.length));
    }

    private V putVal(K k, V v, int index) {
        // 是否需要扩容
        if (isNeedForExpansion(size)) {
            carriedOutExpansion();
        }
        Node<K, V> kvNode = table[index];
        if (null == kvNode) {
            table[index] = new Node<>(k, v, index, null);
            size++;
        } else {
            // 处理 存在next添加到链表结尾
            processorNext(k, v, index, (Node<K, V>)kvNode);
        }
        return table[index].getValue();
    }

    /**
     * 扩容方法
     */
    private void carriedOutExpansion() {
        int size = table.length + SIZE_TMP;
        this.size = 0;

        Node<K, V>[] newTable = new Node[size];
        // 得到全部的node的节点
        List<Node<K, V>> list = new ArrayList<>();
        for (Node<K, V> kvNode : table) {
            while (kvNode != null) {
                list.add(kvNode);
                kvNode = kvNode.next;
            }
        }
        this.table = newTable;
        // 将node节点全部拆散添加到新数组
        if (!CollectionUtils.isEmpty(list)) {
            for (Node<K, V> kvNode : list) {
                // 分离所有的Node
                if (kvNode.next != null) {
                    kvNode.next = null;
                }
                put(kvNode.getKey(), kvNode.getValue());
            }
            theNumberOfChanges++;
        }
    }

    /**
     * 是否需要扩容
     *
     * @param size
     *            当前链表空间
     * @return 是/否
     */
    private boolean isNeedForExpansion(int size) {
        return size > SIZE_TMP * EXPANSION_OF_MULTIPLES * theNumberOfChanges;
    }

    /**
     * 处理next节点 递归
     *
     * @param k
     *            k
     * @param v
     *            v
     * @param hash
     *            hash
     * @param kvNode
     *            当前节点
     */
    private void processorNext(K k, V v, int hash, Node<K, V> kvNode) {
        // 如果key相同并且hashcode想用覆盖原来的值
        if (kvNode.getKey().equals(k) && kvNode.hashCode == hash) {
            kvNode.value = v;
            return;
        }

        if (kvNode.next != null) {
            processorNext(k, v, hash, kvNode.next);
            return;
        }
        kvNode.next = new Node<>(k, v, hash, null);
    }

    /**
     * get
     *
     * @param k
     *            k
     * @return
     */
    @Override
    public V get(K k) {
        return null;
    }

    /**
     * 空间大小
     *
     * @return 空间
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * 计算hash值
     *
     * @param hashCode
     *            hashCode
     * @return hash值
     */
    final int hash(int hashCode) {
        hashCode = hashCode ^ ((hashCode >>> 20) ^ (hashCode >>> 12));
        return hashCode ^ ((hashCode >>> 7) ^ hashCode >>> 4);
    }

    /**
     * 模拟根据哈希值去对应哈希表下标
     *
     * @param k
     *            k
     * @param length
     *            链表长度
     * @return 下标
     */
    private int getIndex(K k, int length) {
        int m = length - 1;
        // 取模
        int index = hash(k.hashCode()) & m;
        return Math.abs(index);
    }

    /**
     * 去最大公约数
     *
     * @param a
     *            a
     * @param b
     *            b
     * @return 最大公约数
     */
    private int gCdmax(int a, int b) {
        // 判断a与b的大小
        if (a < b) {
            int temp = a;
            a = b;
            b = temp;
        }
        // 求最大公约数
        if (a % b != 0) {
            int k = a % b;
            return gCdmax(b, k);// 再次循环
        }
        return b;
    }

    /**
     * 链表节点
     *
     * @param <K>
     * @param <V>
     */
    private static class Node<K, V> implements Map.Entry<K, V> {
        /**
         * k
         */
        private final K key;
        /**
         * v
         */
        private V value;
        /**
         * hashCode
         */
        private final int hashCode;
        /**
         * 下一个节点 由于链表可能导致过长影响查询效率,在jdk1.8 当链表大于8-1个时, 会将结构转换成红黑树数据结构==>(上个节点/下个节点/是否是red节点,其余的和链表一样)
         */
        private Node<K, V> next;

        public Node(K key, V value, int hashCode, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hashCode = hashCode;
            this.next = next;
        }

        public int getHashCode() {
            return hashCode;
        }

        @Override
        public final K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return value;
        }
    }

    public static void main(String[] args) {
        // Scanner scanner=new Scanner(System.in);
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("李文", "李文");
        stringStringMap.put("开开", "李文");
        stringStringMap.put("完成", "完成");
        stringStringMap.put("王晨", "王晨");
        stringStringMap.put("杨峰", "杨峰");
        /*        stringStringMap.put("杨峰31", "31");
        stringStringMap.put("杨峰47", "47");
        stringStringMap.put("杨峰56", "56");
        stringStringMap.put("杨峰91", "91");
        stringStringMap.put("杨峰91", "杨峰91");
        stringStringMap.put("杨峰82", "82");
        stringStringMap.put("杨峰56", "杨峰56");*/
        for (int i = 0; i < 2000; i++) {
            stringStringMap.put("杨峰" + i, "杨峰" + i);
        }
        System.out.println("stringStringMap = " + stringStringMap);

    }

}
