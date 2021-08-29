package deque;

public class ArrayDeque<Item> implements Deque<Item> {
    private Item[] items;
    private static int size;
    private static int nextFirst;
    private static int nextLast;
    private static int max;

    public ArrayDeque() {
        max = 8;
        size = 0;
        nextFirst = 0;
        nextLast = max - 1;
        items = (Item []) new Object[max];
    }

    private Item[] resize() {
        if (size == items.length) {
            max = size * 2;
            Item[] a = (Item []) new Object[max];
            System.arraycopy(items, 0, a, 0, size);
            items = a;
            nextFirst = size;
            nextLast = max - 1;
            return a;
        }
        return items;
    }

    public int getMax() {
        return max;
    }

    @Override
    public void addLast(Item x) {
        items[nextLast] = x;
        size += 1;
        nextLast -= 1;
        resize();
    }

    @Override
    public void addFirst(Item x) {
        items[nextFirst] = x;
        nextFirst += 1;
        size += 1;
        resize();
    }

    @Override
    public Item removeLast() {
        Item store;
        if (size == 0) {
            return null;
        }
        if (nextLast + 1 == items.length) {
            store = items[0];
            items[0] = null;
            nextLast = 0;
        } else {
            store = items[nextLast+1];
            items[nextLast+1] = null;
            nextLast += 1;
        }
        size -= 1;
        return store;
    }

    @Override
    public Item removeFirst() {
        Item store;
        if (size == 0) {
            return null;
        }
        size -= 1;
        if (nextFirst == 0) {
            store = items[items.length-1];
            items[items.length-1] = null;
            nextFirst = items.length;
        } else {
            store = items[nextFirst-1];
            items[nextFirst-1] = null;
            nextFirst -= 1;
        }
        return store;
    }

    @Override
    public Item get(int x) {
        return items[x];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i=0; i < items.length; i++) {
            if (items[i] != null) {
                System.out.println(items[i]);
            }
        }
    }



    public static void main(String[] args) {
        ArrayDeque<Integer> k = new ArrayDeque<>();
        for (int i=1; i<3; i++) {
            k.addLast(10);
        }
        k.addLast(20);
        k.addFirst(15);
        k.addFirst(10);
        k.removeLast();
        k.removeLast();
        k.removeLast();
        k.addFirst(10);
        k.addFirst(10);
        k.addFirst(10);
        k.removeFirst();
        k.removeFirst();
        k.removeFirst();

        System.out.println(k.get(0));
        System.out.println(k.get(7));
        System.out.println(k.get(6));
        System.out.println(k.size());
    }

}