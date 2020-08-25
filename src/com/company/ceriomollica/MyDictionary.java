package com.company.ceriomollica;

import java.util.ArrayList;

public interface MyDictionary<K,V>{

    void insert(K key, V value);

    V search(K key);

    V delete(K key);

    ArrayList<V> values();

    int size();

}

