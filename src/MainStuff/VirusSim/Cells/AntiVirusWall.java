package MainStuff.VirusSim.Cells;

import processing.core.PGraphics;

import static processing.core.PConstants.CORNER;

public class AntiVirusWall extends VirusWall {
    private final float R = 40;
    private final float G = 40;
    private final float B = 100;

    public AntiVirusWall(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(PGraphics screenBuffer) {
        screenBuffer.fill(R, G, B);
        screenBuffer.rectMode(CORNER);
        screenBuffer.noStroke();
        screenBuffer.rect(x, y, 1, 1);
    }
}
