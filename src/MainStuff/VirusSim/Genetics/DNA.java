package MainStuff.VirusSim.Genetics;

import Utilities.FastRand;

import java.util.ArrayList;

import static MainStuff.Main.mutationRate;

public class DNA {
    private ArrayList<Byte> dna;
    private int independentActions;

    /**
     * generates a random sequence of DNA
     *
     * @param sequenceLength     -number of actions
     * @param independentActions -how many possible actions one give action can be
     */
    public DNA(int sequenceLength, int independentActions) {
        this.independentActions = independentActions;
        dna = new ArrayList<>(sequenceLength);
        for (int i = 0; i < sequenceLength; i++) {
            dna.add((byte) FastRand.splittableRandom.nextInt(independentActions));
        }
    }

    public DNA(DNA toClone) {
        dna = (ArrayList<Byte>) toClone.dna.clone();
//        dna = new ArrayList<>((Collection<? extends Integer>) toClone);
        this.independentActions = toClone.independentActions;
    }

    public byte getAction(int index) {
        return dna.get(index);
    }

    public void setAction(int index, byte action) {
        dna.set(index, action);
    }

    public int getSize() {
        return dna.size();
    }

    public void mutate() {
        for (int i = 0; i < dna.size(); i++) {
            if (FastRand.splittableRandom.nextDouble() < mutationRate) {
                dna.set(i, (byte) FastRand.splittableRandom.nextInt(independentActions));
            }
        }
    }

    public static ArrayList<DNA> getClusterClone(ArrayList<DNA> toClone) {
        ArrayList<DNA> dnaArrayList = new ArrayList<>();
        for (int i = 0; i < toClone.size(); i++) {
            dnaArrayList.add(new DNA(toClone.get(i)));
        }
        return dnaArrayList;
    }
}
