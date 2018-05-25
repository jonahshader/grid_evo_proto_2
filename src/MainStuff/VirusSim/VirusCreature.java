package MainStuff.VirusSim;

import MainStuff.ICreature;
import MainStuff.World;
import Utilities.FastRand;
import processing.core.PGraphics;

import static MainStuff.ICreature.CreatureType.VIRUS;
import static processing.core.PConstants.CORNER;

public class VirusCreature implements ICreature {

    public final int ACTION_ENUM_COUNT = 6;
    //int value actions:
    /*
    0: left
    1: right
    2: up
    3: down
    4: explode
    5: dont move
     */

    private final int R = 40;
    private final int G = 255;
    private final int B = 40;

    private int x, y;
    private long age;
    private int actions;
    boolean done;

    private int[] actionList;

    private World containingWorld;

    /**
     * creates a virus creature with random genetics
     * @param x
     * @param y
     * @param actions number of actions in actionList
     * @param containingWorld
     */
    public VirusCreature(int x, int y, int actions, World containingWorld) {
        //Pass parameters
        this.x = x;
        this.y = y;
        this.actions = actions;
        this.containingWorld = containingWorld;

        //Initialize other variables
        age = 0;
        done = false;

        actionList = new int[actions];
        for (int i = 0; i < actions; i++) {
            actionList[i] = FastRand.splittableRandom.nextInt(ACTION_ENUM_COUNT);
        }
    }

    @Override
    public void run() {
        if (!done) {
            //In the run method, if we want to change the creature's location, we must change the newX and newY variables so that the moveCreature logic at the end of run will work
            int newX = x;
            int newY = y;

            if (age < actions) {
                switch (actionList[(int) age]) {
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

                    case 4: //explode
                        explode();
                        done = true;
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

    @Override
    public void draw(PGraphics screenBuffer) {
        screenBuffer.fill(R, G, B);
        screenBuffer.rectMode(CORNER);
        screenBuffer.noStroke();
        screenBuffer.rect(x, y, 1, 1);
    }

    private void explode() {
        //remove a radius of antivirus filler
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
        return VIRUS;
    }

    public boolean isDone() {
        return done;
    }
}
