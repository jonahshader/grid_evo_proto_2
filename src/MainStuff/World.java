package MainStuff;

import processing.core.PGraphics;

import java.util.ArrayList;

public class World {
    private int width, height;
    private ArrayList<Creature> creatures;
    private Creature[][] creatureGrid;

    public World(int width, int height) {
        this.width = width;
        this.height = height;

        creatures = new ArrayList<>();
        creatureGrid = new Creature[width][height];
    }

    //TODO: redundancy can be reduced somehow using .class to use these add/remove methods for all future arrays

    /**
     *
     * @param creature
     * @return true = success. false = fail (desired location is not null)
     */
    public boolean addCreature(Creature creature) {
        int creatureX = creature.getX();
        int creatureY = creature.getY();

        if (creatureGrid[creatureX][creatureY] != null) {
            //There is no creature at this location, create one there
            creatures.add(creature);
            creatureGrid[creatureX][creatureY] = creature;
            return true; //success
        } else {
            //There's already a creature here, we cannot make a creature
            return false; //fail
        }
    }

    /**
     *
     * @param creature
     * @return true = success, false = fail (creature doesn't exist)
     */
    public boolean removeCreature(Creature creature) {
        if (creatures.contains(creature)) {
            //this creature exists, delete it from both arrays
            creatureGrid[creature.getX()][creature.getY()] = null;
            creatures.remove(creature);
            return true; //success
        } else {
            //this creature does not exist, can't remove it
            return false;
        }
    }

    /**
     * assume screenBuffer is already opened with .beginDraw();
     * assume screenBuffer will be closed with .endDraw();
     * @param screenBuffer -the buffer we are drawing to
     */
    public void draw(PGraphics screenBuffer) {
        for (Creature creature : creatures) {
            creature.draw(screenBuffer);
        }
    }

    public void run() {

    }
}
