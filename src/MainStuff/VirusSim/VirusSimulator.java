package MainStuff.VirusSim;

import MainStuff.World;
import processing.core.PGraphics;

import java.util.ArrayList;

public class VirusSimulator {
    private final int TIME_REMAINING_INITIAL = 300;
    private final int POPULATION_SIZE_INITIAL = 30;
    private final int VIRUS_CLUSTER_CREATURE_COUNT = 12;

    private int currentCluster;

    ArrayList<VirusCluster> virusClustersGeneration;

    private World world;

    public VirusSimulator(int width, int height) {
        //init other vars
        world = new World(width, height);
        virusClustersGeneration = new ArrayList<>(POPULATION_SIZE_INITIAL);
        virusClustersGeneration.add(new VirusCluster(VIRUS_CLUSTER_CREATURE_COUNT, TIME_REMAINING_INITIAL, world));
        currentCluster = 0;
        virusClustersGeneration.get(0).start();
    }

    public void draw(PGraphics screenBuffer) {
        world.draw(screenBuffer);
    }

    public void run() {
        world.run();
        if (virusClustersGeneration.get(currentCluster).isDone()) {
            //setup next pair
            System.out.println("Fitness: " + virusClustersGeneration.get(currentCluster).getFitness());
            currentCluster++;
            world.clear();
        }
    }


}
