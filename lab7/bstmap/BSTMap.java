package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {


    private class BSTNode {

        private K key;
        private V val;
        private BSTNode left, right;

        private BSTNode(K key, V val) {
            this.key = key;
            this.val = val;
        }

        /** Good try on this, but this constructor is redundant because
         * we only construct while putting new key and value.
         * If we construct at the beginning, it will be hard to let put method work
         * on the very fist item.
         */
        private BSTNode() {
            this.key = null;
            this.val = null;
        }

    }

    /** Initializes an empty symbol table. */
    private BSTNode list;
    public BSTMap() {

    }


    private int size = 0;

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        list = null;
        size = 0;
    }


    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return containsKey(list, key);
    }

    private boolean containsKey(BSTNode node, K key) {
        if (node == null) {
            return false;
        }
        int cmp = node.key.compareTo(key);
        if (cmp > 0) {
            return containsKey(node.right, key);
        } else if (cmp < 0) {
            return containsKey(node.left, key);
        } else {
            return true;
        }
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (list == null) {
            return null;
        }
        return get(list, key);
    }

    private V get(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = node.key.compareTo(key);
        if (cmp > 0) {
            return get(node.right, key);
        } else if (cmp < 0) {
            return get(node.left, key);
        } else {
            return node.val;
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V val) {
        if (get(list, key) == null) {
            size += 1;
            list = put(list, key, val);
        }
        return;
    }

    private BSTNode put(BSTNode node, K key, V val) {
        if (node == null) {
            return new BSTNode(key, val);
        }
        int cmp = node.key.compareTo(key);
        if (cmp < 0) {
            node.left = put(node.left, key, val);
        } else if (cmp > 0) {
            node.right = put(node.right, key, val);
        } else {
            node.val = val;
        }
        return node;
    }

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    @Override
    public Set<K> keySet() {
        return null;
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();

    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }


}
