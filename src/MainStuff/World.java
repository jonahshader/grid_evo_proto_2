package MainStuff;

import Utilities.WrappingCreatureArray;
import processing.core.PGraphics;

import java.util.ArrayList;

public class World {
    private int width, height;
    private ArrayList<ICreature> creatures, nonRunnableCreatures;
    private WrappingCreatureArray creatureGrid;

    public World(int width, int height) {
        this.width = width;
        this.height = height;

        creatures = new ArrayList<>();
        nonRunnableCreatures = new ArrayList<>();
        creatureGrid = new WrappingCreatureArray(new ICreature[width][height]);
    }

    //TODO: redundancy can be reduced somehow using .class to use these add/remove methods for all future arrays

    /**
     * @param creature
     * @return true = success. false = fail (desired location is not null)
     */
    public boolean addCreature(ICreature creature) {
        int creatureX = creature.getX();
        int creatureY = creature.getY();

        if (creatureGrid.get(creatureX, creatureY) == null) {
            //There is no creature at this location, create one there
            creatures.add(creature);
            creatureGrid.set(creatureX, creatureY, creature);
            return true; //success
        } else {
            //There's already a creature here, we cannot make a creature
            return false; //fail
        }
    }

    /**
     * @param creature
     * @return true = success, false = fail (creature doesn't exist)
     */
    public boolean removeCreature(ICreature creature) {
        if (creatures.contains(creature)) {
            //this creature exists, delete it from both arrays
            creatureGrid.set(creature.getX(), creature.getY(), null);
            creatures.remove(creature);
            return true; //success
        } else {
            //this creature does not exist, can't remove it
            return false; //failed
        }
    }

    /**
     * @param creature
     * @param newX -desired x location
     * @param newY -desired y location
     * @return true = success, false = fail
     */
    public boolean moveCreature(ICreature creature, int newX, int newY) {
        if (creatureGrid.get(newX, newY) == null) {
            //there is no creature in the desired location, move this creature there
            creatureGrid.set(newX, newY, creature);
            creatureGrid.set(creature.getX(), creature.getY(), null);
            return true; //success
        } else {
            //there is a creature in the desired location, abort
            return false; //failed
        }
    }

    public boolean addNonRunnableCreature(ICreature creature) {
        int creatureX = creature.getX();
        int creatureY = creature.getY();

        if (creatureGrid.get(creatureX, creatureY) == null) {
            //There is no creature at this location, create one there
            nonRunnableCreatures.add(creature);
            creatureGrid.set(creatureX, creatureY, creature);
            return true; //success
        } else {
            //There's already a creature here, we cannot make a creature
            return false; //fail
        }
    }

    public ICreature getCreature(int x, int y) {
        return creatureGrid.get(x, y);
    }

    public boolean removeNonRunnableCreature(ICreature creature) {
        if (nonRunnableCreatures.contains(creature)) {
            //this creature exists, delete it from both arrays
            creatureGrid.set(creature.getX(), creature.getY(), null);
            nonRunnableCreatures.remove(creature);
            return true; //success
        } else {
            //this creature does not exist, can't remove it
            return false; //failed
        }
    }

    public void clear() {
        creatureGrid = new WrappingCreatureArray(new ICreature[width][height]);
        nonRunnableCreatures.clear();
        creatures.clear();
    }

    /**
     * assume screenBuffer is already opened with .beginDraw();
     * assume screenBuffer will be closed with .endDraw();
     * @param screenBuffer -the buffer we are drawing to
     */
    public void draw(PGraphics screenBuffer) {
        for (ICreature creature : creatures) {
            creature.draw(screenBuffer);
        }

        for (ICreature creature : nonRunnableCreatures) {
            creature.draw(screenBuffer);
        }
    }

    public void run() {
        for (ICreature creature : creatures) {
            creature.run();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
