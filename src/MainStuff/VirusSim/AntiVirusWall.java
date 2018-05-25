package MainStuff.VirusSim;

import MainStuff.ICreature;
import processing.core.PGraphics;

import static processing.core.PConstants.CORNER;

public class AntiVirusWall implements ICreature {
    private final float R = 40;
    private final float G = 40;
    private final float B = 100;

    private int x, y;

    public AntiVirusWall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void run() {

    }

    @Override
    public void draw(PGraphics screenBuffer) {
        screenBuffer.fill(R, G, B);
        screenBuffer.rectMode(CORNER);
        screenBuffer.noStroke();
        screenBuffer.rect(x, y, 1, 1);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public CreatureType getCreatureType() {
        return null;
    }
}