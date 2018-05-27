package Utilities;

import java.util.Random;
import java.util.SplittableRandom;

public class FastRand {
////    private static long x = 123456789, y = 362436069, z = 521288629;
//    private static long x = System.nanoTime(), y = 362436069, z = 521288629;
//
//    public static long xorshf96() {
//        long t;
//        x ^= x << 16;
//        x ^= x >> 5;
//        x ^= x << 1;
//
//        t = x;
//        x = y;
//        y = z;
//        z = t ^ x ^ y;
//
//        return z;
//    }

    public static SplittableRandom splittableRandom = new SplittableRandom();
    public static Random random = new Random();

    public static int selectRandomWeighted(double[] wts) {
        int selected = 0;
        double total = wts[0];

        for (int i = 1; i < wts.length; i++) {
            total += wts[i];
            if (splittableRandom.nextDouble() <= (wts[i] / total)) selected = i;
        }

        return selected;
    }
}
