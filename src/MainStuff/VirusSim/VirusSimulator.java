package MainStuff.VirusSim;

import MainStuff.VirusSim.Genetics.DNA;
import MainStuff.World;
import Utilities.FastRand;
import processing.core.PGraphics;

import java.util.ArrayList;

public class VirusSimulator {
    private final int TIME_REMAINING_INITIAL = 80;
    private final int POPULATION_SIZE_INITIAL = 300;
    private final int VIRUS_CLUSTER_CREATURE_COUNT = 20;
    private final int ANTI_VIRUS_CLUSTER_CREATURE_COUNT = 10;
    private final int ITERATIONS_PER_GENERATION = 15; //must be smaller than population size

    private int currentCluster;
    private int currentIteration;

    private ArrayList<VirusCluster> virusClusters;
    private ArrayList<AntiVirusCluster> antiVirusClusters;

    private World world;

    public VirusSimulator(int width, int height) {
        //init other vars
        world = new World(width, height);
        virusClusters = new ArrayList<>(POPULATION_SIZE_INITIAL);
        antiVirusClusters = new ArrayList<>(POPULATION_SIZE_INITIAL);
        for (int i = 0; i < POPULATION_SIZE_INITIAL; i++) {
            virusClusters.add(new VirusCluster(VIRUS_CLUSTER_CREATURE_COUNT, TIME_REMAINING_INITIAL, world));
            antiVirusClusters.add(new AntiVirusCluster(ANTI_VIRUS_CLUSTER_CREATURE_COUNT, TIME_REMAINING_INITIAL, world));
        }
        currentCluster = 0;
        currentIteration = 0;
        virusClusters.get(0).start();
        antiVirusClusters.get(0).start();
    }

    public void draw(PGraphics screenBuffer) {
        world.draw(screenBuffer);
    }

    public void run() {
        world.run();
        if (virusClusters.get(currentCluster).isDone() && antiVirusClusters.get(currentCluster).isDone()) {
            //setup next pair
//            System.out.println("Fitness: " + virusClusters.get(currentCluster).getFitness());
            virusClusters.get(currentCluster).recordFitness(currentIteration); //the virus cluster can determine it's own fitness.

            //the anti virus cluster's fitness depends on its own fitness and the opposite of virus's fitness
            float antiVirusFitness = antiVirusClusters.get(currentCluster).getFitness();
            antiVirusFitness *= 0.50; //25& of the antivirus's fitness depends on its own distance from the right side
//            antiVirusFitness += (world.getWidth() - virusClusters.get(currentCluster).getFitness()) * 0.5; //75% of its fitness depends on how close the virus is to its side
            float invertedVirusFitness = virusClusters.get(currentCluster).getFitness();
            antiVirusFitness += invertedVirusFitness * 0.50;
//            antiVirusFitness = Math.min(antiVirusFitness, invertedVirusFitness);
            antiVirusClusters.get(currentCluster).setFitness(antiVirusFitness, currentIteration);
            System.out.println("Virus " + currentCluster + " Average fitness: " + virusClusters.get(currentCluster).getAverageFitness());
            System.out.println("AntiVirus " + currentCluster + " Average fitness: " + antiVirusClusters.get(currentCluster).getAverageFitness());
            currentCluster++;
            world.clear();

            //move on to next iteration
            if (currentCluster >= POPULATION_SIZE_INITIAL) {
                currentCluster = 0;
                currentIteration++;
                //if we ran the last iteration, run
                if (currentIteration >= ITERATIONS_PER_GENERATION) {
                    currentIteration = 0;
                    //next generation
                    System.out.println("next generation");
                    VirusCluster bestVirusCluster = virusClusters.get(0); //init to first one
                    float bestFitness = virusClusters.get(0).getAverageFitness();
                    for (VirusCluster virusCluster : virusClusters) { //loop through all virus clusters to find the best one
                        if (virusCluster.getAverageFitness() > bestFitness) {
                            bestFitness = virusCluster.getAverageFitness();
                            bestVirusCluster = virusCluster;
                        }
                    }

                    AntiVirusCluster bestAntiVirusCluster = antiVirusClusters.get(0); //init to first one
                    bestFitness = antiVirusClusters.get(0).getAverageFitness();
                    for (AntiVirusCluster antiVirusCluster : antiVirusClusters) {
                        if (antiVirusCluster.getAverageFitness() > bestFitness) {
                            bestFitness = antiVirusCluster.getAverageFitness();
                            bestAntiVirusCluster = antiVirusCluster;
                        }
                    }

                    //take the DNA from the clusters
                    ArrayList<DNA> bestVirusDNA = bestVirusCluster.getDnaArrayList();
                    ArrayList<DNA> bestAntiVirusDNA = bestAntiVirusCluster.getDnaArrayList();



                    for (int i = 0; i < virusClusters.size(); i++) {
                        virusClusters.get(i).clearFitness();
                        virusClusters.get(i).setNewDNA(bestVirusDNA);
                        if (i > 0)
                            virusClusters.get(i).mutateDNA();
                    }

                    for (int i = 0; i < antiVirusClusters.size(); i++) {
                        antiVirusClusters.get(i).clearFitness();
                        antiVirusClusters.get(i).setNewDNA(bestAntiVirusDNA);
                        if (i != antiVirusClusters.size() / 2)
                            antiVirusClusters.get(i).mutateDNA();
                    }

                    ////////////////////////////////////////////////////////////////////////

                } else {
                    //shift the antivirus clusters down one
                    System.out.println("next iteration");
                    antiVirusClusters.add(0, antiVirusClusters.get(antiVirusClusters.size() - 1));
                    antiVirusClusters.remove(antiVirusClusters.size() - 1);
                }

            }
            virusClusters.get(currentCluster).start();
            antiVirusClusters.get(currentCluster).start();
        }
    }
}
