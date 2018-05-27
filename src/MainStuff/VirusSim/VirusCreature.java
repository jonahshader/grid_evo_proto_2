package MainStuff.VirusSim;

import MainStuff.ICreature;
import MainStuff.VirusSim.Cells.Active.VirusLaser;
import MainStuff.VirusSim.Genetics.DNA;
import MainStuff.World;
import processing.core.PGraphics;

import static MainStuff.ICreature.CreatureType.ANTIVIRUS_BLOCK;
import static MainStuff.ICreature.CreatureType.VIRUS;
import static processing.core.PConstants.CORNER;

public class VirusCreature implements ICreature {
    static final int ACTION_ENUM_COUNT = 6;
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

    protected int x, y;
    protected int age;
    protected int actions;
    protected boolean done;
    private boolean abilityUsed;

    protected DNA dna;
    protected World containingWorld;
    private VirusCluster containingCluster;

    /**
     * creates a virus creature with random genetics
     *
     * @param x
     * @param y
     * @param actions         number of actions in actionList
     * @param containingWorld
     */
    public VirusCreature(int x, int y, int actions, World containingWorld, VirusCluster containingCluster) {
        //Pass parameters
        this.x = x;
        this.y = y;
        this.actions = actions;
        this.containingWorld = containingWorld;
        this.containingCluster = containingCluster;

        //Initialize other variables
        age = 0;
        done = false;
        abilityUsed = false;

        //Generate random DNA
        dna = new DNA(actions, ACTION_ENUM_COUNT);
    }

    /**
     * create creature with specific DNA
     * @param x
     * @param y
     * @param dna
     * @param containingWorld
     */
    public VirusCreature(int x, int y, DNA dna, World containingWorld, VirusCluster containingCluster) {
        //pass params
        this.x = x;
        this.y = y;
        this.dna = dna;
        this.containingWorld = containingWorld;
        this.containingCluster = containingCluster;

        actions = dna.getSize();

        //init other vars
        age = 0;
        done = false;
        abilityUsed = false;
    }

    //this is for child classes
    protected VirusCreature(int x, int y, int actions, int independentActions, World containingWorld, VirusCluster containingCluster) {
        //pass params
        this.x = x;
        this.y = y;
        this.actions = actions;
        this.containingWorld = containingWorld;
        this.containingCluster = containingCluster;

        //init other vars
        age = 0;
        done = false;

        //generate random DNA
        dna = new DNA(actions, independentActions);
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

                    case 4: //explode
                        if (!abilityUsed)
                            explode();
                        abilityUsed = true;
//                        newX++;
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
        for (int i = 1; i < 3; i++) {
            ICreature explodedCreature = containingWorld.getCreature(x + i, y);
            if (explodedCreature != null) {
                if (explodedCreature.getCreatureType() == ANTIVIRUS_BLOCK) {
                    containingWorld.removeCreature(explodedCreature);
                }
            }
            ICreature virusLaser = new VirusLaser(x + i, y, containingWorld);
            containingWorld.addCreature(virusLaser, true);
        }
        containingCluster.recordLaserShot();
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
