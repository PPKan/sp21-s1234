package deque;

public class LinkedListDeque<PeteType> {

    private class LinkNode {
        public PeteType item;
        public LinkNode prev;
        public LinkNode next;
        public LinkNode(LinkNode p, PeteType i, LinkNode n) {
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

    public LinkedListDeque(PeteType x) {
        sentinel = new LinkNode(null, null, null);
        sentinel = new LinkNode(sentinel.prev, sentinel.item, sentinel.next);
        sentinel.next = new LinkNode(sentinel, x, sentinel);
        sentinel.prev = sentinel.next;
        size += 1;
    }

    public void addFirst(PeteType x) {
        sentinel.next = new LinkNode(sentinel, x, sentinel.next);
        if (sentinel.next.next == null) {
            sentinel.next = new LinkNode(sentinel, x, sentinel);
            sentinel.prev = sentinel.next;
        } else {
            sentinel.next.next.prev = sentinel.next;
        }
    }

    public void addLast(String x) {

    }

    public int getSize() {
        return 1;
    }

    public boolean isEmpty() {
        if (sentinel.next == null) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        LinkedListDeque<String> k = new LinkedListDeque<>("app");
        k.addFirst("queen");
        k.addFirst("king");
        System.out.println(k.sentinel.item);
        System.out.println(k.sentinel.next.item);
        System.out.println(k.sentinel.next.next.item);
        System.out.println(k.sentinel.prev.item);
        System.out.println(k.sentinel.prev.prev.item);
    }



}