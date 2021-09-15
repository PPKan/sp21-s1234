package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @PeterKan
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
//    private Collection<Node>[] buckets;
    private Collection<Node>[] table;
    private int size;
    private int loadSize;
    private double loadFactor;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(5);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        table = new Collection[initialSize];
        loadFactor = maxLoad;
        loadSize = initialSize;
        size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    /** Removes all of the mappings from this map. */
    @Override
    public void clear(){
        size = 0;
        table = new Collection[5];
        loadSize = 5;
        loadFactor = 0.75;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key){
        int keyCode = key.hashCode();
        int position = keyCode % loadSize;
        position = Math.abs(position);
        if (table[position] == null) {
            return false;
        } else {
            for (Node n : table[position]) {
                if (n.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key){
        int keyCode = key.hashCode();
        int position = keyCode % loadSize;
        position = Math.abs(position);
        if (table[position] == null) {
            return null;
        } else {
            for (Node n : table[position]) {
                if (n.key.equals(key)) {
                    return n.value;
                }
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size(){
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value){

        if (get(key) == value) {
            return;
        }

        int keyCode = key.hashCode();
        int position = keyCode % loadSize;
        position = Math.abs(position);
        Node n = createNode(key, value);
        if (table[position] == null) {
            table[position] = new LinkedList<>();
        } else if (containsKey(key)) {
            for (Node m : table[position]) {
                if (m.key.equals(key)) {
                    m.value = value;
                    return;
                }
            }
        }
        table[position].add(n);
        size += 1;
        resize();

    }

    private void resize() {
        LinkedList<Node> tempPlace = new LinkedList<>();
        if (size / table.length > loadFactor) {
            loadSize *= 2;
            for (int i=0; i < table.length; i+=1) {
                if (table[i] != null) {
                    for (Node n : table[i]) {
                        tempPlace.add(n);
                    }
                }
            }
            table = new Collection[loadSize];
            size = 0;
            for (Node n : tempPlace) {
                put(n.key, n.value);
            }
        }
    }

    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet(){
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key){
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value){
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

}
