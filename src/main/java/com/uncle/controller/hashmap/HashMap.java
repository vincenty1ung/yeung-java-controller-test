package com.uncle.controller.hashmap;

/**
 * @author 杨戬
 * @className HashMap
 * @email yangb@chaosource.com
 * @date 2020/5/27 22:41
 */
public class HashMap<K,V> implements Map<K,V>{

    transient Node<K,V>[] table;

    transient int size;

    public HashMap() {
        this.table = new Node[16];
    }

    @Override
    public V put(K k, V v) {
        return putVal(k, v, hash(k));
    }

    private V putVal(K k, V v, int hash) {

        Node<K, V> kvNode = table[hash];
        if (null==kvNode){
            table[hash]= new Node<>(k,v,hash,null);
            size++;
        }else {
            //如果key相同并且hashcode想用覆盖原来的值
            if(kvNode.getKey().equals(k)){
                table[hash]= new Node<>(k,v,hash,null);
            }

            table[hash]= new Node<>(k,v,hash, kvNode);
            size++;

        }
        return table[hash].getValue();
    }

    @Override
    public V get(K k) {
        return null;
    }

    @Override
    public int size() {
        return size;
    }
    static final int hash(Object key) {
        return (key == null) ? 0 : Math.abs( key.hashCode())%16;
    }

    class Node<K,V> implements Map.Entry<K,V>{
        private final K key;
        private final int hashCode;
        private  V value;
        private Node<K,V> next;

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
        public final V getValue() {
            return value;
        }

        @Override
        public final V setValue(V value) {
            return value;
        }
    }

    public static void main(String[] args) {
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("李文","李文");
        stringStringMap.put("开开","李文");
        stringStringMap.put("完成","完成");
        stringStringMap.put("王晨","王晨");
        stringStringMap.put("杨峰","杨峰");
        for(int i =0;i<100;i++){
            stringStringMap.put("杨峰"+i,"杨峰"+i);
        }
        System.out.println("stringStringMap = " + stringStringMap);

    }
}
