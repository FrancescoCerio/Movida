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

/**
 * Classe definita per implmentare la funzione di dizionario HashConcatenamento.
 * Implementa l'interfaccia MyDictionary usata per gli algoritmi di dizionario.
 *
 * @param <K> chiave della chiave per ogni nodo presente nel dizionario
 * @param <V> valore di ogni chiave presente
 */
public class HashConcatenamento<K, V> implements MyDictionary<K,V>{
    // bucketArray utilizzata per mantenere in memoria la lista di HashNode
    private ArrayList<HashNode<K, V>> bucketArray;

    // Capacità attuale della tabella
    private int numBuckets;

    // Capacità attuale della lista
    private int size;

    // Costruttore per inizializzare la lista vuota con una dimansione fissa (10)
    public HashConcatenamento() {
        bucketArray = new ArrayList<>();
        numBuckets = 10;
        size = 0;

        for (int i = 0; i < numBuckets; i++)
            bucketArray.add(null);
    }

    @Override
    public int size() { return size; }
    public boolean isEmpty() { return size() == 0; }

    // Funzione ausiliaria per ricercare l'indice di una chiave
    // ritorna il suo valore in hash
    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        int index = hashCode % numBuckets;
        return Math.abs(index);
    }

    // Metodo per la rimozione di una chiave
    @Override
    public V delete(K key) {
        // Applico la funzione di hash
        int bucketIndex = getBucketIndex(key);

        // Punto alla testa della lista
        HashNode<K, V> head = bucketArray.get(bucketIndex);

        // Ricerco la chiave nella lista
        HashNode<K, V> prev = null;
        while (head != null) {
            // Se non trovo la chiave
            if (head.key.equals(key))
                break;

            // Altrimenti continuo ad iterare
            prev = head;
            head = head.next;
        }

        // Se la chiave non era presente ritorno null
        if (head == null)
            return null;

        // Decremento la dimensione
        size--;

        // Rimuovo la chiave trovata
        if (prev != null)
            prev.next = head.next;
        else
            bucketArray.set(bucketIndex, head.next);

        return head.value;
    }

    // Funzione per la ricerca del valore di una chiave
    @Override
    public V search(K key) {
        int bucketIndex = getBucketIndex(key);

        HashNode<K, V> head = bucketArray.get(bucketIndex);

        // Cerco la chiave nella tabella
        while (head != null) {
            if (head.key.equals(key))
                return head.value;
            head = head.next;
        }

        // Ritorno null se non trovo la chiave
        return null;
    }

    // Metodo utilizzato per ricavare i valori di tutte le chiavi presenti nella tabella
    @Override
    public ArrayList<V> values(){
        ArrayList<HashNode<K, V>> temp = bucketArray;
        ArrayList<V> values = new ArrayList<>();

        for (HashNode<K, V> headNode : temp) {
            while (headNode != null) {
                values.add(headNode.getValue());
                headNode = headNode.next;
            }
        }
        return values;
    }

    // Metodo per l'inserzione di una coppia <Chiave, Valore> nella tabella
    @Override
    public void insert(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        HashNode<K, V> head = bucketArray.get(bucketIndex);

        // Controllo se la chiave è già presente e in caso affermativo aggiorno il valore
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        // Caso in cui la chiave non è presente
        // Incremento la dimensione
        size++;
        head = bucketArray.get(bucketIndex);
        HashNode<K, V> newNode = new HashNode<K, V>(key, value);
        newNode.next = head;
        bucketArray.set(bucketIndex, newNode);

        // Se il loadfactor supera 0.7 aumento la dimensione della tabella per normalizzare i valori
        if ((1.0*size)/numBuckets >= 0.7) {
            ArrayList<HashNode<K, V>> temp = bucketArray;
            bucketArray = new ArrayList<>();
            numBuckets = 2 * numBuckets;
            size = 0;
            for (int i = 0; i < numBuckets; i++)
                bucketArray.add(null);

            for (HashNode<K, V> headNode : temp) {
                while (headNode != null) {
                    insert(headNode.key, headNode.value);
                    headNode = headNode.next;
                }
            }
        }
    }
}
