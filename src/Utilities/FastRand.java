package Utilities;

public class FastRand {
//    static long x = 123456789, y = 362436069, z = 521288629;
    private static long x = System.nanoTime(), y = 362436069, z = 521288629;

    public static long xorshf96() {
        long t;
        x ^= x << 16;
        x ^= x >> 5;
        x ^= x << 1;

        t = x;
        x = y;
        y = z;
        z = t ^ x ^ y;

        return z;
    }
}
