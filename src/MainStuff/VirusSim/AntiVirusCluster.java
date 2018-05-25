package MainStuff.VirusSim;

import MainStuff.World;

import java.util.ArrayList;

public class AntiVirusCluster {
    private int creatureCount;

    private ArrayList<AntiVirusCreature> creatures;
    private World world;

    /**
     * generate random antivirus cluster
     * @param creatureCount cant be greater than world height
     * @param actions number of actions (DNA/genetics length)
     * @param world
     */
    public AntiVirusCluster(int creatureCount, int actions, World world) {
        //pass params
        this.creatureCount = creatureCount;
        this.world = world;
        if (creatureCount > world.getHeight()) {
            this.creatureCount = world.getHeight();
            System.out.println("Creature count too hight, limiting to world height.");
        }

        //init other vars
        creatures = new ArrayList<>();

        for (int i = 0; i < creatureCount; i++) {
            AntiVirusCreature newCreature = new AntiVirusCreature(world.getWidth() - 2, (int) ((i / (float) creatureCount) * world.getHeight()), actions, world);
            creatures.add(newCreature);
        }
    }

    public void start() {
        //create the anti virus's stating wall
        for (int i = 0; i < world.getHeight(); i++) {
            world.addNonRunnableCreature(new AntiVirusWall(world.getWidth() - 1, i));
        }
        //add all of the pre-constructed creatures to the world
        for (AntiVirusCreature antiVirusCreature : creatures) {
            world.addCreature(antiVirusCreature);
        }
    }

    public boolean isDone() {
        boolean done = true;
        for (AntiVirusCreature creature : creatures) {
            if (!creature.isDone()) {
                done = false;
            }
        }
        return done;
    }
}
