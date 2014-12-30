import java.io.*;
public class MovingAverage {
    public static void main(String[] args) {  
        int N = Integer.parseInt(args[0]);
        double[] a = new double[N];
        double sum = 0.0;
        for (int i = 1; !StdIn.isEmpty(); i++) {
            sum -= a[i % N];
            a[i % N] = StdIn.readDouble();
            sum += a[i % N];
            if (i >= N) StdOut.print(sum/N + " ");
        }
    }
}