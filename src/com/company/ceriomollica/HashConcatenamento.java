package com.company.ceriomollica;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

// Class to represent entire hash table
class HashConcatenamento<K, V> implements MyDictionary<K,V>{
    // bucketArray is used to store array of chains
    private ArrayList<HashNode<K, V>> bucketArray;

    // Current capacity of array list
    private int numBuckets;

    // Current size of array list
    private int size;

    // Constructor (Initializes capacity, size and
    // empty chains.
    public HashConcatenamento() {
        bucketArray = new ArrayList<>();
        numBuckets = 1000;
        size = 0;

        // Create empty chains
        for (int i = 0; i < numBuckets; i++)
            bucketArray.add(null);
    }

    @Override
    public int size() { return size; }
    public boolean isEmpty() { return size() == 0; }

    // This implements hash function to find index
    // for a key
    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        int index = hashCode % numBuckets;
        return Math.abs(index);
    }

    // Method to remove a given key
    @Override
    public V delete(K key) {
        // Apply hash function to find index for given key
        int bucketIndex = getBucketIndex(key);

        // Get head of chain
        HashNode<K, V> head = bucketArray.get(bucketIndex);

        // Search for key in its chain
        HashNode<K, V> prev = null;
        while (head != null) {
            // If Key found
            if (head.key.equals(key))
                break;

            // Else keep moving in chain
            prev = head;
            head = head.next;
        }

        // If key was not there
        if (head == null)
            return null;

        // Reduce size
        size--;

        // Remove key
        if (prev != null)
            prev.next = head.next;
        else
            bucketArray.set(bucketIndex, head.next);

        return head.value;
    }

    // Returns value for a key
    @Override
    public V search(K key) {
        // Find head of chain for given key
        int bucketIndex = getBucketIndex(key);

        HashNode<K, V> head = bucketArray.get(bucketIndex);

        // Search key in chain
        while (head != null) {
            if (head.key.equals(key))
                return head.value;
            head = head.next;
        }

        // If key not found
        return null;
    }

    //sistemata
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

    // Adds a key value pair to hash
    @Override
    public void insert(K key, V value) {
        // Find head of chain for given key
        int bucketIndex = getBucketIndex(key);
        HashNode<K, V> head = bucketArray.get(bucketIndex);

        // Check if key is already present
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        // Insert key in chain
        size++;
        head = bucketArray.get(bucketIndex);
        HashNode<K, V> newNode = new HashNode<K, V>(key, value);
        newNode.next = head;
        bucketArray.set(bucketIndex, newNode);

        // If load factor goes beyond threshold, then
        // double hash table size
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

/**
 *
 * VECCHIA IMPLEMENTAZIONE
 *
 public class HashConcatenamento<K extends Comparable<K>, V> {
 // Initializing a Dictionary
 //ArrayList<Hashtable<String, String>> geek = new ArrayList<Hashtable<String, String>>();
 Hashtable<String, ArrayList<Movie>> geek = new Hashtable<String, ArrayList<Movie>>();
 String sfr;

 //@Override
 public void insert(String key, ArrayList<Movie> value) {
 geek.put(key, value);
 }


 //@Override
 public ArrayList<Movie> search(String key) {
 Set<String> keys = geek.keySet();

 //Obtaining iterator over set entries
 Iterator<String> itr = keys.iterator();

 //Displaying Key and value pairs
 while (itr.hasNext()) {
 // Getting Key
 sfr = itr.next();
 if (sfr == key) {
 return (geek.get(sfr));
 }
 }
 return null;
 }
 public void delete(String key){
 geek.remove(key);
 }
 }
 **/
