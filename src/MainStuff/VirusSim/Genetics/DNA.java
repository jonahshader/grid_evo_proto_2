package MainStuff.VirusSim.Genetics;

import Utilities.FastRand;

import java.util.ArrayList;

public class DNA {
    private ArrayList<Integer> dna;

    /**
     * generates a random sequence of DNA
     * @param sequenceLength -number of actions
     * @param independentActions -how many possible actions one give action can be
     */
    public DNA(int sequenceLength, int independentActions) {
        dna = new ArrayList<>(sequenceLength);
        for (int i = 0; i < sequenceLength; i++) {
            dna.add(FastRand.splittableRandom.nextInt(independentActions));
        }
    }

    public int getAction(int index) {
        return dna.get(index);
    }

    public void setAction(int index, int action) {
        dna.set(index, action);
    }


}
