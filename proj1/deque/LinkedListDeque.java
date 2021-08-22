package deque;

public class LinkedListDeque<DeqType> {

    private class LinkNode {

        public LinkNode previous;
        public DeqType item;
        public LinkNode next;

        public LinkNode(LinkNode p, DeqType i, LinkNode n) {
            previous = p;
            item = i;
            next = n;
        }
    }

    public  LinkNode L;
    public LinkedListDeque(DeqType x) {
        L = new LinkNode(null, x, null);
    }


}