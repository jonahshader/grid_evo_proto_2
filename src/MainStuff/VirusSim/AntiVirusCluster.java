package MainStuff.VirusSim;

import MainStuff.VirusSim.Genetics.DNA;
import MainStuff.VirusSim.Cells.NonActive.AntiVirusWall;
import MainStuff.World;
import Utilities.FastRand;

import java.util.ArrayList;

public class AntiVirusCluster {
    private int creatureCount;
    private int actions;
    private float fitness;
    private boolean fitnessUpToDate;

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
            this.creatureCount = world.getHeight() - 2;
            System.out.println("Creature count too hight, limiting to world height.");
        }

        //init other vars
        fitness = 0;
        fitnessUpToDate = false;
        creatures = new ArrayList<>();
        fitnesses = new ArrayList<>();
        dnaArrayList = new ArrayList<>();
        for (int i = 0; i < creatureCount; i++) {
            dnaArrayList.add(new DNA(actions, AntiVirusCreature.ACTION_ENUM_COUNT));
        }
    }

    public void start() {
        creatures.clear();
        for (int i = 0; i < creatureCount; i++) {
//            AntiVirusCreature newCreature = new AntiVirusCreature(world.getWidth() - 2, (int) ((i / (float) this.creatureCount) * world.getHeight()), actions, world);
            AntiVirusCreature newCreature = new AntiVirusCreature(world.getWidth() - 2, 1 + (int) ((i / (float) this.creatureCount) * (world.getHeight() - 1)), dnaArrayList.get(i), world);
            creatures.add(newCreature);
        }

        //create the anti virus's stating wall
        for (int i = 0; i < world.getHeight(); i++) {
            world.addCreature(new AntiVirusWall(world.getWidth() - 1, i), false);
        }
        for (int i = world.getWidth() / 2; i < world.getWidth() - 1; i++) {
            world.addCreature(new AntiVirusWall(i, 0), false);
            world.addCreature(new AntiVirusWall(i, world.getHeight() - 1), false);
        }

        //add all of the pre-constructed creatures to the world
        for (AntiVirusCreature antiVirusCreature : creatures) {
            world.addCreature(antiVirusCreature, true);
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
        fitnessUpToDate = false;
    }

    public float getFitness() {
        float averageX = 0;
        for (AntiVirusCreature creature : creatures) {
            averageX += creature.getX();
        }
        averageX /= creatures.size();

        return world.getWidth() - averageX;
    }

    public float getAverageFitness() {
        if (fitnessUpToDate) {
            return fitness;
        } else {
            if (fitnesses.size() > 0) {
                float avg = 0;
                for (Float f : fitnesses) {
                    avg += f;
                }
                avg /= fitnesses.size();
                fitness = avg;
                fitnessUpToDate = true;
                return avg;
            } else {
                return 0;
            }
        }
    }

    public ArrayList<DNA> getDnaArrayList() {
        return dnaArrayList;
    }

    public void setNewDNA(ArrayList<DNA> newDNA) {
        dnaArrayList = new ArrayList<>();
        for (int i = 0; i < newDNA.size(); i++) {
            dnaArrayList.add(new DNA(newDNA.get(i)));
        }
    }

    public void mutateDNA() {
        for (DNA dna : dnaArrayList) {
            dna.mutate();
        }
    }

    public void clearFitness() {
        fitnesses.clear();
    }
}
