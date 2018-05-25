package MainStuff.VirusSim.Cells;

import processing.core.PGraphics;

import static processing.core.PConstants.CORNER;

public class AntiVirusBlock extends VirusWall {
    private final float R = 70;
    private final float G = 90;
    private final float B = 100;

    public AntiVirusBlock(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(PGraphics screenBuffer) {
        screenBuffer.fill(R, G, B);
        screenBuffer.rectMode(CORNER);
        screenBuffer.noStroke();
        screenBuffer.rect(x, y, 1, 1);
    }

    @Override
    public CreatureType getCreatureType() {
        return CreatureType.ANTIVIRUS_BLOCK;
    }
}
