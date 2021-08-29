package deque;

import java.util.Comparator;

public class MaxArrayDeque<Item> extends ArrayDeque {

    private Comparator comp;

    public MaxArrayDeque(Comparator<Item> c) {
        super();
        comp = c;
    }

    public int max() {
        int temp = 0;
        for (int i=0; i<this.getMax(); i++) {
            int item = comp.compare(this.get(i), this.get(i));
            if (item > temp) {
                temp = item;
            }
        }
        return temp;
    }

    public static class ArrayComp implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (o1 != null && o1 > 10) {
                return o1;
            } else {
                return 0;
            }
        }
    }

    public static class ArrayComp1 implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (o1 != null && o1 % 2 == 0) {
                return o1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) {


        ArrayComp c = new ArrayComp();
        ArrayComp1 c1 = new ArrayComp1();
        MaxArrayDeque<Integer> L = new MaxArrayDeque(c1);
        L.addFirst(21);
        L.addFirst(13);
        L.addFirst(20);
        System.out.println(L.get(0));
        System.out.println(L.max());
//        System.out.println(L.max());

    }

}