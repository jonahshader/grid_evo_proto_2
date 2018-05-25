package MainStuff.VirusSim;

import MainStuff.World;

import java.util.ArrayList;

public class VirusCluster {
    private int virusCreatureCount;

    private ArrayList<VirusCreature> virusCreatures;
    private World world;


    /**
     * generate random virus cluster
     * @param virusCreatureCount
     * @param world
     */
    public VirusCluster(int virusCreatureCount, int actions, World world) {
        this.virusCreatureCount = virusCreatureCount;
        this.world = world;

        virusCreatures = new ArrayList<>();

        for (int i = 0; i < virusCreatureCount; i++) {
            VirusCreature newCreature = new VirusCreature(1, (int) ((i / (float) virusCreatureCount) * world.getHeight()), actions, world);
            virusCreatures.add(newCreature);
        }
    }

    public void start() {
        for (int i = 0; i < world.getHeight(); i++) {
            world.addNonRunnableCreature(new VirusWall(0, i));
        }
        for (VirusCreature virusCreature : virusCreatures) {
            world.addCreature(virusCreature);
        }
    }

    public float getFitness() {
        float averageX = 0;
        for (VirusCreature virusCreature : virusCreatures) {
            averageX += virusCreature.getX();
        }
        averageX /= virusCreatures.size();

        return averageX;
    }

    public boolean isDone() {
        boolean done = true;
        for (VirusCreature virusCreature : virusCreatures) {
            if (!virusCreature.isDone()) {
                done = false;
            }
        }
        return done;
    }
}
