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
     * 是否在扩容
     */
    transient boolean isCarriedOutExpansionIng;

    /**
     * 操作大小
     */
    transient int operSize;
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
        return putVal(k, v, getIndex(k, table.length), hash(k.hashCode()));
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
        // 根据 k活得当前k的 下标拿到node节点
        // 前提:k的值相等 hashcode相等 如果node节点不存在next 直接返回value
        // 如果node节点存在next节点 并且第一节点 没有找到符合的结果递归查找 找到满意的结果为止返回不存在返回null
        int hash = hash(k.hashCode());
        Node<K, V> kvNode = table[getIndex(k, table.length)];
        if (null == kvNode) {
            return null;
        }
        if (kvNode.getKey().equals(k) && hash == kvNode.hashCode) {
            return kvNode.getValue();
        }
        if (kvNode.next != null) {
            Node<K, V> next = kvNode.next;
            while (next != null) {
                if (next.getKey().equals(k) && hash == next.hashCode) {
                    return next.getValue();
                }
                next = next.next;
            }
        }
        return null;
    }

    /**
     * 空间大小
     *
     * @return 空间
     */
    @Override
    public int size() {
        return operSize;
    }

    private V putVal(K k, V v, int index, int hashCode) {
        // 是否需要扩容
        if (isNeedForExpansion(size)) {
            carriedOutExpansion();
        }
        Node<K, V> kvNode = table[index];
        if (null == kvNode) {
            table[index] = new Node<>(k, v, hashCode, null);
            size++;
            if (!isCarriedOutExpansionIng) {
                operSize++;
            }
            // System.out.println("日志：===size = " + size);
        } else {
            // 处理存在next添加到链表结尾
            processorNext(k, v, hashCode, (Node<K, V>)kvNode);
        }
        return v;
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
                // 重置next
                Node<K, V> node = new Node<>(kvNode.getKey(), kvNode.getValue(), kvNode.hashCode, null);
                list.add(node);
                kvNode = kvNode.next;
            }
        }
        this.table = newTable;
        // 将node节点全部拆散添加到新数组
        if (!CollectionUtils.isEmpty(list)) {
            this.isCarriedOutExpansionIng = true;
            theNumberOfChanges++;
            System.out.println("日志：======開始擴容 擴容集合size：" + list.size());
            for (Node<K, V> kvNode : list) {
                // 分离所有的Node
                if (kvNode.next != null) {
                    kvNode.next = null;
                }
                put(kvNode.getKey(), kvNode.getValue());
            }
            System.out.println("日志：======擴容結束");
            this.isCarriedOutExpansionIng = false;
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
        if (!isCarriedOutExpansionIng) {
            operSize++;
        }
    }

    /**
     * 计算hash值
     *
     * @param hashCode
     *            hashCode
     * @return hash值
     */
    final int hash(int hashCode) {
        return hashCode ^ (hashCode >>> 16);
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
        System.out
            .println("日志：======下鏢：" + Math.abs(index) + "====index:" + index + "=====length:" + m + "======key：" + k);
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
        stringStringMap.put("陈佩斯", "陈佩斯");
        stringStringMap.put("宋江", "宋江");
        stringStringMap.put("李逵", "李逵(鬼)");
        stringStringMap.put("郭靖", "郭靖");
        stringStringMap.put("杨峰", "杨峰");
        /*        stringStringMap.put("杨峰31", "31");
        stringStringMap.put("杨峰47", "47");
        stringStringMap.put("杨峰56", "56");
        stringStringMap.put("杨峰91", "91");
        stringStringMap.put("杨峰91", "杨峰91");
        stringStringMap.put("杨峰82", "82");
        stringStringMap.put("杨峰56", "杨峰56");*/
        for (int i = 0; i < 20000; i++) {
            stringStringMap.put("小李廣" + i, "花榮" + i);
        }
        System.out.println("stringStringMap = " + stringStringMap.get("小李廣20"));
        System.out.println("stringStringMap = " + stringStringMap.get("李逵"));
        System.out.println("size = " + stringStringMap.size());
    }

}
