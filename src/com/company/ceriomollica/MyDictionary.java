/*
 * Copyright (C) 2020 - Francesco Cerio, Francesco Mollica
 *
 * Progetto Movida.
 * Corso di Algoritmi e Strutture Dati
 * Laurea in Informatica, UniBO, a.a. 2019/2020
 *
 */
package com.company.ceriomollica;

import java.util.ArrayList;

public interface MyDictionary<K,V>{

    void insert(K key, V value);

    V search(K key);

    V delete(K key);

    ArrayList<V> values();

    int size();

}

