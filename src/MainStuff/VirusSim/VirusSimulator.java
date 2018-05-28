package MainStuff.VirusSim;

import MainStuff.VirusSim.Genetics.DNA;
import MainStuff.World;
import Utilities.FastRand;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.Collections;

public class VirusSimulator {
    private final int TIME_REMAINING_INITIAL = 100;
    private final int POPULATION_SIZE_INITIAL = 500;
    private final int VIRUS_CLUSTER_CREATURE_COUNT = 10;
    private final int ANTI_VIRUS_CLUSTER_CREATURE_COUNT = 20;
    private final int ITERATIONS_PER_GENERATION = 4; //must be smaller than population size

    private int currentCluster;
    private int currentIteration;
    private int generation;

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
        generation = 0;
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
            virusClusters.get(currentCluster).recordFitness(currentIteration); //the virus cluster can determine it's own fitness.

            //the anti virus cluster's fitness depends on its own fitness and the opposite of virus's fitness
            float antiVirusFitness = antiVirusClusters.get(currentCluster).getFitness();
            antiVirusFitness *= 0.1; //25& of the antivirus's fitness depends on its own distance from the right side
            float invertedVirusFitness = world.getWidth() - virusClusters.get(currentCluster).getFitnessNoLaser();
            antiVirusFitness += invertedVirusFitness * 0.9;
            antiVirusClusters.get(currentCluster).setFitness(antiVirusFitness, currentIteration);
            System.out.print("Gen " + generation + " Virus " + currentCluster + " Average fitness: " + String.format("%.2f", virusClusters.get(currentCluster).getAverageFitness()));
            System.out.println(" AntiVirus average fitness: " + String.format("%.2f", antiVirusClusters.get(currentCluster).getAverageFitness()));
            currentCluster++;
            world.clear();

            //move on to next iteration
            if (currentCluster >= POPULATION_SIZE_INITIAL) {
                currentCluster = 0;
                currentIteration++;
                //if we ran the last iteration, setup next generation
                if (currentIteration >= ITERATIONS_PER_GENERATION) {
                    currentIteration = 0;
                    //next generation
                    generation++;
//                    VirusCluster bestVirusCluster = virusClusters.get(0); //init to first one
//                    float bestFitness = virusClusters.get(0).getAverageFitness();
//                    for (VirusCluster virusCluster : virusClusters) { //loop through all virus clusters to find the best one
//                        if (virusCluster.getAverageFitness() > bestFitness) {
//                            bestFitness = virusCluster.getAverageFitness();
//                            bestVirusCluster = virusCluster;
//                        }
//                    }
//
//                    AntiVirusCluster bestAntiVirusCluster = antiVirusClusters.get(0); //init to first one
//                    bestFitness = antiVirusClusters.get(0).getAverageFitness();
//                    for (AntiVirusCluster antiVirusCluster : antiVirusClusters) {
//                        if (antiVirusCluster.getAverageFitness() > bestFitness) {
//                            bestFitness = antiVirusCluster.getAverageFitness();
//                            bestAntiVirusCluster = antiVirusCluster;
//                        }
//                    }

                    //sort clusters from greatest to least fitness
                    virusClusters.sort(((o1, o2) -> Float.compare(o1.getAverageFitness(), o2.getAverageFitness())));
                    antiVirusClusters.sort(((o1, o2) -> Float.compare(o1.getAverageFitness(), o2.getAverageFitness())));
                    Collections.reverse(virusClusters);
                    Collections.reverse(antiVirusClusters);

                    //print virus cluster fitness'
                    for (int i = 0; i < virusClusters.size(); i++) {
                        System.out.println(virusClusters.get(i).getAverageFitness());
                    }

                    //this array is for storing the probability of any given cluster being selected for reproduction
                    ArrayList<Double> virusClusterSelectionProbabilities = new ArrayList<>(POPULATION_SIZE_INITIAL);

                    for (int i = 0; i < virusClusters.size(); i++) {
                        virusClusterSelectionProbabilities.add(virusClusters.size() - (double) i);
                    }
                    double probSum = 0;
                    for (Double num : virusClusterSelectionProbabilities) {
                        probSum += num;
                    }
                    for (int i = 0; i < virusClusterSelectionProbabilities.size(); i++) {
                        virusClusterSelectionProbabilities.set(i, virusClusterSelectionProbabilities.get(i) / probSum);
                    }
                    //create a clone of that array for the anti virus clusters
                    ArrayList<Double> antiVirusClusterSelectionProbabilities = new ArrayList<>();
                    antiVirusClusterSelectionProbabilities = (ArrayList<Double>) virusClusterSelectionProbabilities.clone();

                    //arraylist for storing all virus cluster dna from this generation
                    ArrayList<ArrayList<DNA>> virusClusterDNA = new ArrayList<>(virusClusters.size());

                    //get all virus cluster dna
                    for (VirusCluster virusCluster : virusClusters) {
                        virusClusterDNA.add(virusCluster.getDnaArrayList());
                    }

                    //arraylist for storing the virus cluster dna that made it past genetic selection
                    ArrayList<ArrayList<DNA>> newVirusDNA = new ArrayList<>();
                    //loop through and select half of the population for reproduction
                    for (int i = 0; i < POPULATION_SIZE_INITIAL / 2; i++) {
                        int nextSelected = FastRand.selectRandomWeighted(virusClusterSelectionProbabilities);
                        newVirusDNA.add(virusClusterDNA.get(nextSelected));
                        virusClusterDNA.remove(nextSelected);
                        virusClusterSelectionProbabilities.remove(nextSelected);
                    }

                    //repeating the above steps for antivirus
                    ArrayList<ArrayList<DNA>> antiVirusClusterDNA = new ArrayList<>(antiVirusClusters.size());

                    //get all anti virus cluster dna
                    for (AntiVirusCluster antiVirusCluster : antiVirusClusters) {
                        antiVirusClusterDNA.add(antiVirusCluster.getDnaArrayList());
                    }

                    //arraylist for storing the antivirus cluster dna that made it past genetic selection
                    ArrayList<ArrayList<DNA>> newAntiVirusDNA = new ArrayList<>();
                    //loop through and seleft half of the population for reproduction
                    for (int i = 0; i < POPULATION_SIZE_INITIAL / 2; i++) {
                        int nextSelected = FastRand.selectRandomWeighted(antiVirusClusterSelectionProbabilities);
                        newAntiVirusDNA.add(antiVirusClusterDNA.get(nextSelected));
                        antiVirusClusterDNA.remove(nextSelected);
                        antiVirusClusterSelectionProbabilities.remove(nextSelected);
                    }

                    //we now have half of the population from both the virus's and antivirus's that we want to duplicate and mutate
                    for (int i = 0; i < POPULATION_SIZE_INITIAL / 2; i++) {
                        newVirusDNA.add(DNA.getClusterClone(newVirusDNA.get(i)));
                        newAntiVirusDNA.add(DNA.getClusterClone(newAntiVirusDNA.get(i)));
                    }

                    //mutate half todo: need to do something about this. we don't need to re run the second half to get their fitness values (except maybe we do because iterations > 0 will evaluate differently... kinda)
                    for (int i = 0; i < POPULATION_SIZE_INITIAL / 2; i++) {
                        ArrayList<DNA> cluster = newVirusDNA.get(i);
                        for (DNA aCluster : cluster) {
                            aCluster.mutate();
                        }
                        cluster = newAntiVirusDNA.get(i);
                        for (DNA aCluster : cluster) {
                            aCluster.mutate();
                        }
                    }





//                    for (int i = 0; i < POPULATION_SIZE_INITIAL; i++) {
//                        virusClusters.get(i).clearFitness();
//                        virusClusters.get(i).setNewDNA(DNA.getClusterClone(newVirusDNA.get(i))); //TODO: might need to clone dna here
//                        antiVirusClusters.get(i).clearFitness();
//                        antiVirusClusters.get(i).setNewDNA(DNA.getClusterClone(newAntiVirusDNA.get(i)));
//                    }
//
//                    Collections.shuffle(virusClusters);
//                    Collections.shuffle(antiVirusClusters);





                    //take the DNA from the clusters
                    ArrayList<DNA> bestVirusDNA = virusClusters.get(0).getDnaArrayList();
                    ArrayList<DNA> bestAntiVirusDNA = antiVirusClusters.get(0).getDnaArrayList();

                    for (int i = 0; i < virusClusters.size(); i++) {
                        virusClusters.get(i).clearFitness();
                        virusClusters.get(i).setNewDNA(DNA.getClusterClone(bestVirusDNA));
                        if (i > 0)
                            virusClusters.get(i).mutateDNA();
                    }

                    for (int i = 0; i < antiVirusClusters.size(); i++) {
                        antiVirusClusters.get(i).clearFitness();
                        antiVirusClusters.get(i).setNewDNA(DNA.getClusterClone(bestAntiVirusDNA));
                        if (i != antiVirusClusters.size() / 2)
                            antiVirusClusters.get(i).mutateDNA();
                    }

                    ////////////////////////////////////////////////////////////////////////

                } else {
                    //shift the antivirus clusters down one
//                    System.out.println("next iteration");
//                    antiVirusClusters.add(0, antiVirusClusters.get(antiVirusClusters.size() - 1));
//                    antiVirusClusters.remove(antiVirusClusters.size() - 1);
                    Collections.shuffle(antiVirusClusters);
                }
            }
            virusClusters.get(currentCluster).start();
            antiVirusClusters.get(currentCluster).start();
        }
    }
}
