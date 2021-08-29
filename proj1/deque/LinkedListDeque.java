package deque;

public class LinkedListDeque<Item> implements Deque<Item> {

    private class LinkNode {
        public Item item;
        public LinkNode prev;
        public LinkNode next;
        public LinkNode(LinkNode p, Item i, LinkNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    /** first item is sentinal.next, and last item is sentinel.prev */
    private LinkNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new LinkNode(null, null, null);
        sentinel = new LinkNode(sentinel.prev, sentinel.item, sentinel.next);
    }

    public LinkedListDeque(Item x) {
        size += 1;
        sentinel = new LinkNode(null, null, null);
        sentinel = new LinkNode(sentinel.prev, sentinel.item, sentinel.next);
        sentinel.next = new LinkNode(sentinel, x, sentinel);
        sentinel.prev = sentinel.next;
    }

    @Override
    public void addFirst(Item x) {
        size += 1;
        sentinel.next = new LinkNode(sentinel, x, sentinel.next);
        if (sentinel.next.next == null) {
            sentinel.next = new LinkNode(sentinel, x, sentinel);
            sentinel.prev = sentinel.next;
        } else {
            sentinel.next.next.prev = sentinel.next;
        }
    }

    @Override
    public void addLast(Item x) {
        size += 1;
        sentinel.prev = new LinkNode(sentinel.prev, x, sentinel);
        if (sentinel.prev.prev == null) {
            sentinel.prev = new LinkNode(sentinel, x, sentinel);
            sentinel.next = sentinel.prev;
        } else {
            sentinel.prev.prev.next = sentinel.prev;
        }
    }

    @Override
    public Item get(int x) {
        LinkNode p = sentinel.next;
        int index = 0;
        while (p.item != null) {
            if (index == x) {
                return p.item;
            }
            p = p.next;
            index += 1;
        }
        return null;
    }

    @Override
    public Item removeFirst() {
        Item x = null;
        if (size >= 1) {
            size -= 1;
            x = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
        }
        return x;
    }

    @Override
    public Item removeLast() {
        Item x = null;
        if (size >= 1) {
            size -= 1;
            x = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
        }
        return x;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        LinkNode p = sentinel.next;
        while (p.item != null) {
            System.out.println(p.item);
            p = p.next;
        }
    }


    public static void main(String[] args) {
        LinkedListDeque<String> k = new LinkedListDeque<>();
        k.addFirst("queen");
        k.addFirst("king");
        k.addLast("knight");
        k.removeFirst();
//        System.out.println(k.sentinel.item);
//        System.out.println(k.sentinel.next.item);
//        System.out.println(k.sentinel.next.next.item);
//        System.out.println(k.sentinel.prev.item);
//        System.out.println(k.sentinel.prev.prev.item);
//        System.out.println(k.size());
//        System.out.println(k.get(0));
        System.out.println(k.isEmpty());
//        k.printDeque();
    }



}