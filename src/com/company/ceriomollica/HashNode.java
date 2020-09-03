package com.company.ceriomollica;

/**
 * Classe ausiliaria utilizzata per l'implementazione dei nodi di HashConcatenamento
 *
 * @param <K> chiave identificativa del nodo
 * @param <V> valore di ogni nodo
 */
public class HashNode<K, V> {
    K key;
    V value;

    // Reference to next node
    HashNode<K, V> next;

    // Constructor
    public HashNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey(){
        return this.key;
    }

    public V getValue(){
        return this.value;
    }
}
