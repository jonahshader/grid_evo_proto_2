package MainStuff.VirusSim;

import MainStuff.VirusSim.Cells.AntiVirusBlock;
import MainStuff.VirusSim.Genetics.DNA;
import MainStuff.World;
import Utilities.CirclePoints;
import processing.core.PGraphics;

import java.util.ArrayList;

import static processing.core.PConstants.CORNER;

public class AntiVirusCreature extends VirusCreature {
    static final int ACTION_ENUM_COUNT = 6;
    //int value actions:
    /*
    0: left
    1: right
    2: up
    3: down
    4: release stuff
    5: dont move
     */

    private final int R = 40;
    private final int G = 40;
    private final int B = 255;

    boolean blockerReleased;


    /**
     * creates an antivirus creature with random genetics
     * @param x
     * @param y
     * @param actions
     * @param containingWorld
     */
    public AntiVirusCreature(int x, int y, int actions, World containingWorld) {
        super(x, y, actions, ACTION_ENUM_COUNT, containingWorld);

        //init other vars
        blockerReleased = false;
    }

    /**
     * creates an antivirus creature with specific DNA
     * @param x
     * @param y
     * @param dna
     * @param containingWorld
     */
    public AntiVirusCreature(int x, int y, DNA dna, World containingWorld) {
        super(x, y, dna, containingWorld);

        //init other vars
        blockerReleased = false;
    }

    @Override
    public void run() {
        if (!done) {
            //In the run method, if we want to change the creature's location, we must change the newX and newY variables so that the moveCreature logic at the end of run will work
            int newX = x;
            int newY = y;

            if (age < actions) {
                switch (dna.getAction(age)) {
                    case 0: //left
                        newX--;
                        break;

                    case 1: //right
                        newX++;
                        break;

                    case 2: //up
                        newY--;
                        break;

                    case 3: //down
                        newY++;
                        break;

                    case 4: //release antivirus blocker
                        //TODO:
                        if (!blockerReleased) {
                            releaseBlocker();
                            done = true;
                            blockerReleased = true;
                        }
                        break;

                    case 5: //do nothing
                        break;
                    default: //default is do nothing
                        break;
                }
            } else {
                done = true;
            }

            //If the creature tried to move, tell the containingWorld that we are trying to move
            if (x != newX || y != newY) {
                //try to move the creature's grid location to the desired location
                if (containingWorld.moveCreature(this, newX, newY)) {
                    //If the move was successful, update coordinates (to the wrapped coordinates)
                    x = Math.floorMod(newX, containingWorld.getWidth());
                    y = Math.floorMod(newY, containingWorld.getHeight());
                }
            }
        }

        age++;
    }

    private void releaseBlocker() {
        ArrayList<CirclePoints.PointInt> points = CirclePoints.generateCircle(x, y, 4);
        for (CirclePoints.PointInt point : points) {
            int x = point.x;
            int y = point.y;

            //limit to inside the world (don't let it wrap around)
            if (x < 0) x = 0;
            if (x >= containingWorld.getWidth()) x = containingWorld.getWidth() - 1;
            if (y < 0) y = 0;
            if (y >= containingWorld.getHeight()) y = containingWorld.getHeight() - 1;

            AntiVirusBlock block = new AntiVirusBlock(x, y);

            containingWorld.addNonRunnableCreature(block);
        }
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
        return CreatureType.ANTIVIRUS;
    }
}
