package com.lld.lld.LRUCache;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node<K, V> {
    public K key;
    public V value;
    public Node<K, V> prev;
    public Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
