package MainStuff.VirusSim;

import MainStuff.ICreature;
import MainStuff.World;

import java.util.ArrayList;

public class VirusCluster {
    private int creatureCount;

    private ArrayList<VirusCreature> creatures;
    private World world;


    /**
     * generate random virus cluster
     * @param creatureCount cant be greater than screen height
     * @param actions number of actions (DNA/genetics length)
     * @param world
     */
    public VirusCluster(int creatureCount, int actions, World world) {
        //pass params
        this.creatureCount = creatureCount;
        this.world = world;
        if (creatureCount > world.getHeight()) {
            this.creatureCount = world.getHeight();
            System.out.println("Creature count too high, limiting to world height.");
        }

        //init other vars
        creatures = new ArrayList<>();

        for (int i = 0; i < creatureCount; i++) {
            VirusCreature newCreature = new VirusCreature(1, (int) ((i / (float) creatureCount) * world.getHeight()), actions, world);
            creatures.add(newCreature);
        }
    }

    public void start() {
        //create the virus's stating wall
        for (int i = 0; i < world.getHeight(); i++) {
            world.addNonRunnableCreature(new VirusWall(0, i));
        }
        //add all of the pre-constructed creatures to the world
        for (VirusCreature virusCreature : creatures) {
            world.addCreature(virusCreature);
        }
    }

    /**
     *
     * @return fitness is average x distance away from left side
     */
    public float getFitness() {
        float averageX = 0;
        for (VirusCreature virusCreature : creatures) {
            averageX += virusCreature.getX();
        }
        averageX /= creatures.size();

        return averageX;
    }

    public boolean isDone() {
        boolean done = true;
        for (VirusCreature virusCreature : creatures) {
            if (!virusCreature.isDone()) {
                done = false;
            }
        }
        return done;
    }
}
