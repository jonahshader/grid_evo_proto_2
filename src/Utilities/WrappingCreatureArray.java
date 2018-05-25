package Utilities;

import MainStuff.ICreature;

public class WrappingCreatureArray {
    private ICreature[][] creatureArray;

    public WrappingCreatureArray(ICreature[][] creatureArray) {
        this.creatureArray = creatureArray;
    }

    public ICreature get(int x, int y) {
        x = Math.floorMod(x, creatureArray.length);
        y = Math.floorMod(y, creatureArray[0].length);
        return creatureArray[x][y];
    }

    public void set(int x, int y, ICreature creature) {
        x = Math.floorMod(x, creatureArray.length);
        y = Math.floorMod(y, creatureArray[0].length);
        creatureArray[x][y] = creature;
    }
}
