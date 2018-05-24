package MainStuff;

import Utilities.FastRand;
import processing.core.PGraphics;

public class Creature {
    private final int INITIAL_HEALTH = 1000;

    private int x, y;
    private long age;
    private int health;
    private float r, g, b;

    private World containingWorld;

    public Creature(int x, int y, World containingWorld) {
        //Transfer parameters
        this.x = x;
        this.y = y;
        this.containingWorld = containingWorld;
        //Initialize other variables
        age = 0;
        health = INITIAL_HEALTH;

        //as of right now, color is random. it won't be in the future though
        r = (float) Math.random();
        g = (float) Math.random();
        b = (float) Math.random();
    }

    public void run() {
        containingWorld.removeCreature(this);
        if (FastRand.xorshf96() % 10 == 0) {
            x += (FastRand.xorshf96() % 3) - 1;
            y += (FastRand.xorshf96() % 3) - 1;
        }

        containingWorld.addCreature(this);
    }

    /**
     * assume screenBuffer is already opened with .beginDraw();
     * assume screenBuffer will be closed with .endDraw();
     * @param screenBuffer -the buffer we are drawing to
     */
    public void draw(PGraphics screenBuffer) {
        screenBuffer.stroke(r, g, b);
        screenBuffer.point(x, y);
    }

    //Getters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
