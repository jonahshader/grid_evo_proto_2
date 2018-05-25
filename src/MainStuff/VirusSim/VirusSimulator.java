package MainStuff.VirusSim;

import MainStuff.World;
import processing.core.PGraphics;

import java.util.ArrayList;

public class VirusSimulator {
    private final int TIME_REMAINING_INITIAL = 30;
    private final int POPULATION_SIZE_INITIAL = 30;
    private final int VIRUS_CLUSTER_CREATURE_COUNT = 480; //max
    private final int ANTI_VIRUS_CLUSTER_CREATURE_COUNT = 10;

    private int currentCluster;
    private int currentIteration;

    private ArrayList<VirusCluster> virusClustersGenerations;
    private ArrayList<AntiVirusCluster> antiVirusClustersGenerations;

    private World world;

    public VirusSimulator(int width, int height) {
        //init other vars
        world = new World(width, height);
        virusClustersGenerations = new ArrayList<>(POPULATION_SIZE_INITIAL);
        antiVirusClustersGenerations = new ArrayList<>(POPULATION_SIZE_INITIAL);
        for (int i = 0; i < POPULATION_SIZE_INITIAL; i++) {
            virusClustersGenerations.add(new VirusCluster(VIRUS_CLUSTER_CREATURE_COUNT, TIME_REMAINING_INITIAL, world));
            antiVirusClustersGenerations.add(new AntiVirusCluster(ANTI_VIRUS_CLUSTER_CREATURE_COUNT, TIME_REMAINING_INITIAL, world));
        }
        currentCluster = 0;
        currentIteration = 0;
        virusClustersGenerations.get(0).start();
        antiVirusClustersGenerations.get(0).start();

    }

    public void draw(PGraphics screenBuffer) {
        world.draw(screenBuffer);
    }

    public void run() {
        world.run();
        if (virusClustersGenerations.get(currentCluster).isDone() && antiVirusClustersGenerations.get(currentCluster).isDone()) {
            //setup next pair
            System.out.println("Fitness: " + virusClustersGenerations.get(currentCluster).getFitness());
            virusClustersGenerations.get(currentCluster).recordFitness(currentIteration);
            antiVirusClustersGenerations.get(currentCluster).setFitness(world.getWidth() - virusClustersGenerations.get(currentCluster).getFitness(), currentIteration);
            System.out.println("Average fitness: " + virusClustersGenerations.get(currentCluster).getAverageFitness());
            currentCluster++;
            world.clear();

            //move on to next iteration
            if (currentCluster >= POPULATION_SIZE_INITIAL) {
                currentCluster = 0;
                currentIteration++;
            }
            virusClustersGenerations.get(currentCluster).start();
            antiVirusClustersGenerations.get(currentCluster).start();
        }
    }
}
