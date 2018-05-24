package Utilities;

import MainStuff.Creature;

public class WrappingCreatureArray {
    private Creature[][] creatureArray;

    public WrappingCreatureArray(Creature[][] creatureArray) {
        this.creatureArray = creatureArray;
    }

    public Creature get(int x, int y) {
        x = Math.floorMod(x, creatureArray.length);
        y = Math.floorMod(y, creatureArray[0].length);
        return creatureArray[x][y];
    }

    public void set(int x, int y, Creature creature) {
        x = Math.floorMod(x, creatureArray.length);
        y = Math.floorMod(y, creatureArray[0].length);
        creatureArray[x][y] = creature;
    }
}
