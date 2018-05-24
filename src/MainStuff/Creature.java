package MainStuff;

import Utilities.FastRand;
import processing.core.PGraphics;

import static processing.core.PConstants.CENTER;

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
        r = (float) (Math.random() * 0.5);
        g = (float) Math.random();
        b = (float) Math.random();
    }

    public void run() {
        //In the run method, if we want to change the creature's location, we must change the newX and newY variables so that the moveCreature logic at the end of run will work
        int newX = x;
        int newY = y;

//        if (FastRand.splittableRandom.nextDouble() < 0.85) {
//            newX += FastRand.splittableRandom.nextInt(3) - 1;
//            newY += FastRand.splittableRandom.nextInt(3) - 1;
//            newX += Math.round(FastRand.random.nextGaussian() * 2);
//            newY += Math.round((FastRand.random.nextGaussian() * 1.5) + FastRand.splittableRandom.nextDouble() * 0.25);
        newX += Math.round(FastRand.random.nextGaussian() * 1);
        newY += Math.round(FastRand.random.nextGaussian() * 1);
//        }

        //If the creature tried to move, tell the containingWorld that we are trying to move
        if (x != newX || y != newY) {

            //try to move the creature's grid location to the desired location
            if (containingWorld.moveCreature(this, newX, newY)) {
                //If the move was successful, update coordinates (to the wrapped coordinates)
                x = Math.floorMod(newX, containingWorld.getWidth());
                y = Math.floorMod(newY, containingWorld.getHeight());
            } else {
//                System.out.println("Creature move failed.");
            }
        }
    }

    /**
     * assume screenBuffer is already opened with .beginDraw();
     * assume screenBuffer will be closed with .endDraw();
     * @param screenBuffer -the buffer we are drawing to
     */
    public void draw(PGraphics screenBuffer) {
//        screenBuffer.stroke(r * 255, g * 255, b * 255, 10);
        screenBuffer.fill(r * 255, g * 255, b * 255, 10);
        screenBuffer.noStroke();
        screenBuffer.point(x, y);
        screenBuffer.rectMode(CENTER);
        screenBuffer.rect(x, y, 2, 2);
    }

    //Getters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
