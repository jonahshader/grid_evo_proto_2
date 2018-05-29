package MainStuff.VirusSim;

import MainStuff.VirusSim.Cells.NonActive.VirusWall;
import MainStuff.VirusSim.Genetics.DNA;
import MainStuff.World;

import java.util.ArrayList;

public class VirusCluster {
    private int creatureCount;
    private int actions;
    private float fitness;
    private boolean fitnessUpToDate;

    private float miscFitness;

    private ArrayList<VirusCreature> creatures;
    private ArrayList<Float> fitnesses;
    private ArrayList<DNA> dnaArrayList;
    private World world;


    /**
     * generate random virus cluster
     *
     * @param creatureCount cant be greater than screen height
     * @param actions       number of actions (DNA/genetics length)
     * @param world
     */
    public VirusCluster(int creatureCount, int actions, World world) {
        //pass params
        this.creatureCount = creatureCount;
        this.actions = actions;
        this.world = world;
        if (creatureCount >= world.getHeight()) {
            this.creatureCount = world.getHeight() - 2;
            System.out.println("Creature count too high, limiting to world height.");
        }

        //init other vars
        fitness = 0;
        fitnessUpToDate = false;
        creatures = new ArrayList<>();
        fitnesses = new ArrayList<>();
        dnaArrayList = new ArrayList<>();
        for (int i = 0; i < creatureCount; i++) {
            dnaArrayList.add(new DNA(actions, VirusCreature.ACTION_ENUM_COUNT));
        }
    }

    public void start() {
        creatures.clear();
        miscFitness = 0;
        for (int i = 0; i < creatureCount; i++) {
            VirusCreature newCreature = new VirusCreature(1, 1 + (int) ((i / (float) this.creatureCount) * (world.getHeight() - 1)), dnaArrayList.get(i), world, this);
            creatures.add(newCreature);
        }


        //create the virus's stating wall
        for (int i = 0; i < world.getHeight(); i++) {
            world.addCreature(new VirusWall(0, i), false);
        }
        for (int i = 0; i < world.getWidth() / 2; i++) {
            world.addCreature(new VirusWall(i, 0), false);
            world.addCreature(new VirusWall(i, world.getHeight() - 1), false);
        }

        //add all of the pre-constructed creatures to the world
        for (VirusCreature virusCreature : creatures) {
            world.addCreature(virusCreature, true);
        }
    }

    /**
     * @return fitness is average x distance away from left side
     */
    public float getFitness() {
        float averageX = 0;
        for (VirusCreature virusCreature : creatures) {
            averageX += virusCreature.getX();
        }
        averageX += miscFitness;
        averageX /= creatures.size();
        return averageX;
    }

    public float getFitnessNoLaser() {
        float averageX = 0;
        for (VirusCreature virusCreature : creatures) {
            averageX += virusCreature.getX();
        }
//        averageX -= lasersShot * 0.5;
        averageX /= creatures.size();

        return averageX;
    }

    public void recordFitness(int run) {
        fitnessUpToDate = false;
        if (fitnesses.size() < run) {
            fitnesses.set(run, getFitness());
        } else {
            fitnesses.add(run, getFitness());
        }
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
        dnaArrayList = newDNA;
    }

    public void mutateDNA() {
        for (DNA dna : dnaArrayList) {
            dna.mutate();
        }
    }

    public void clearFitness() {
        fitnesses.clear();
        fitnessUpToDate = false;
    }

    public void recordFitnessChange(float deltaFitness) {
        miscFitness += deltaFitness;
    }
}
