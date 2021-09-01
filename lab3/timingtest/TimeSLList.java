package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        int[] size = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};

        SLList<Integer> n = new SLList<>();
        AList<Double> t = new AList<>();
        AList<Integer> c = new AList<>();

        for (int i=0; i<size.length; i++) {
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < size[i]; j++) {
                n.addLast(size[i]);
            }
            double timeinsec = sw.elapsedTime();
            t.addLast(timeinsec);
            c.addLast(size[i]);
        }

        printTimingTable(c, t, c);
    }

}
