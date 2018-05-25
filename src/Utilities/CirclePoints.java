package Utilities;

import java.util.ArrayList;

public class CirclePoints {

    public static ArrayList<PointInt> generateCircle(int x0, int y0, int radius) {
        ArrayList<PointInt> points = new ArrayList<>();
        int x = radius - 1;
        int y = 0;
        int dx = 1;
        int dy = 1;
        int err = dx - (radius << 1);

        while (x >= y) {
            points.add(new PointInt(x0 + x, y0 + y));
            points.add(new PointInt(x0 + y, y0 + x));
            points.add(new PointInt(x0 - y, y0 + x));
            points.add(new PointInt(x0 - x, y0 + y));
            points.add(new PointInt(x0 - x, y0 - y));
            points.add(new PointInt(x0 - y, y0 - x));
            points.add(new PointInt(x0 + y, y0 - x));
            points.add(new PointInt(x0 + x, y0 - y));

            if (err <= 0) {
                y++;
                err += dy;
                dy += 2;
            }

            if (err > 0) {
                x--;
                dx += 2;
                err += dx - (radius << 1);
            }
        }

        return points;
    }

    public static class PointInt {
        public int x, y;

        public PointInt(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "PointInt{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
