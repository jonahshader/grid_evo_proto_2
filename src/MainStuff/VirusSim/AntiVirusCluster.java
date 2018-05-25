package MainStuff.VirusSim;

import MainStuff.VirusSim.Cells.AntiVirusWall;
import MainStuff.VirusSim.Genetics.DNA;
import MainStuff.World;

import java.util.ArrayList;

public class AntiVirusCluster {
    private int creatureCount;
    private int actions;

    private ArrayList<AntiVirusCreature> creatures;
    private ArrayList<Float> fitnesses;
    private ArrayList<DNA> dnaArrayList;
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
        this.actions = actions;
        this.world = world;
        if (creatureCount >= world.getHeight()) {
            this.creatureCount = world.getHeight() - 1;
            System.out.println("Creature count too hight, limiting to world height.");
        }

        //init other vars
        creatures = new ArrayList<>();
        fitnesses = new ArrayList<>();
    }

    public void start() {
        //TODO: this is very wrong right now. need to make the virus/antivirus's carry over their genetics instead of generating new genetics here.
        creatures.clear();
        for (int i = 0; i < creatureCount; i++) {
            AntiVirusCreature newCreature = new AntiVirusCreature(world.getWidth() - 2, (int) ((i / (float) this.creatureCount) * world.getHeight()), actions, world);
            creatures.add(newCreature);
        }

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
        for (AntiVirusCreature creature : creatures) {
            if (!creature.isDone()) {
                return false;
            }
        }
        return true;
    }

    public void setFitness(float fitness, int run) {
        if (fitnesses.size() < run) {
            fitnesses.set(run, fitness);
        } else {
            fitnesses.add(run, fitness);
        }
    }
}
