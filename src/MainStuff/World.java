package MainStuff;

import Utilities.WrappingCreatureArray;
import processing.core.PGraphics;

import java.util.ArrayList;

public class World {
    private int width, height;
    private ArrayList<ICreature> creatures, nonRunnableCreatures, creaturesRemoveQueue, nonRunnableCreaturesRemoveQueue;
    private WrappingCreatureArray creatureGrid;

    public World(int width, int height) {
        this.width = width;
        this.height = height;

        creatures = new ArrayList<>();
        nonRunnableCreatures = new ArrayList<>();
        creaturesRemoveQueue = new ArrayList<>();
        nonRunnableCreaturesRemoveQueue = new ArrayList<>();
        creatureGrid = new WrappingCreatureArray(new ICreature[width][height]);
    }

    //TODO: redundancy can be reduced somehow using .class to use these add/remove methods for all future arrays

    /**
     * @param creature
     * @return true = success. false = fail (desired location is not null)
     */
    public boolean addCreature(ICreature creature, boolean active) {
        int creatureX = creature.getX();
        int creatureY = creature.getY();

        if (creatureGrid.get(creatureX, creatureY) == null) {
            //There is no creature at this location, create one there
            if (active) {
                creatures.add(creature);
//                creaturesAddQueue.add(creature);
            } else {
                nonRunnableCreatures.add(creature);
//                nonRunnableCreaturesAddQueue.add(creature);
            }

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
        if (creatures.contains(creature) || nonRunnableCreatures.contains(creature)) {
            //this creature exists, delete it from both arrays
            creatureGrid.set(creature.getX(), creature.getY(), null);
//            creatures.remove(creature);
//            nonRunnableCreatures.remove(creature);
            creaturesRemoveQueue.add(creature);
            nonRunnableCreaturesRemoveQueue.add(creature);
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

    public ICreature getCreature(int x, int y) {
        return creatureGrid.get(x, y);
    }

    public void clear() {
        creatureGrid = new WrappingCreatureArray(new ICreature[width][height]);
        creatures.clear();
        nonRunnableCreatures.clear();
        creaturesRemoveQueue.clear();
        nonRunnableCreaturesRemoveQueue.clear();
    }

    /**
     * assume screenBuffer is already opened with .beginDraw();
     * assume screenBuffer will be closed with .endDraw();
     * @param screenBuffer -the buffer we are drawing to
     */
    public void draw(PGraphics screenBuffer) {
        creatures.forEach(creature -> creature.draw(screenBuffer));
        nonRunnableCreatures.forEach(creature -> creature.draw(screenBuffer));
    }

    public void run() {
        creatures.removeAll(creaturesRemoveQueue);
        nonRunnableCreatures.removeAll(nonRunnableCreaturesRemoveQueue);
//        for (ICreature creature : creaturesRemoveQueue) {
//            creatureGrid.set(creature.getX(), creature.getY(), null);
//        }
//        for (ICreature creature : nonRunnableCreaturesRemoveQueue) {
//            creatureGrid.set(creature.getX(), creature.getY(), null);
//        }
        creaturesRemoveQueue.clear();
        nonRunnableCreaturesRemoveQueue.clear();

        for (int i = 0, creaturesSize = creatures.size(); i < creaturesSize; i++) {
            creatures.get(i).run();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
