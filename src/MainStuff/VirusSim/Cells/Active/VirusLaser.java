package MainStuff.VirusSim.Cells.Active;

import MainStuff.VirusSim.Cells.NonActive.VirusWall;
import MainStuff.World;
import processing.core.PGraphics;

import static processing.core.PConstants.CORNER;

public class VirusLaser extends VirusWall {
    private final float R = 180;
    private final float G = 40;
    private final float B = 30;

    private int age;

    private World containingWorld;

    public VirusLaser(int x, int y, World containingWorld) {
        super(x, y);
        this.containingWorld = containingWorld;
        age = 0;
    }

    @Override
    public void draw(PGraphics screenBuffer) {
        screenBuffer.fill(R, G, B);
        screenBuffer.rectMode(CORNER);
        screenBuffer.noStroke();
        screenBuffer.rect(x, y, 1, 1);
    }

    @Override
    public void run() {
        if (age > 3) {
            containingWorld.removeCreature(this);
        }
        age++;
    }
}
