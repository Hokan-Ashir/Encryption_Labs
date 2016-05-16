package ist.one;

import ist.Pair;
import ist.common.AbstractEncryptionCase;

import java.util.*;

public class IST_1 extends AbstractEncryptionCase {
    private Integer [] substitution = new Integer[(int)Math.pow(2.0d, BIT_LENGTH)];
    private int [][] diffMatrix = new int[substitution.length][substitution.length];
    private List<Statistics> weightStatisticsList = new ArrayList<Statistics>();

    public IST_1() {
        for (int i = 0; i < BIT_LENGTH * 2 - 1; ++i) {
            weightStatisticsList.add(new Statistics());
        }
        generateSubstitution(true);
        generateDifferentialMatrix();
    }

    public float getHighestProbability() {
        float biggestAmount = diffMatrix[0][1];
        for (int i = 0; i < substitution.length; ++i) {
            for (int j = 0; j < substitution.length; ++j) {
                if (i != 0 && j != 0) {
                    if (diffMatrix[i][j] > biggestAmount) {
                        biggestAmount = diffMatrix[i][j];
                    }
                }
            }
        }

        return biggestAmount / substitution.length;
    }

    private void clearDifferentialMatrix() {
        for (int i = 0; i < substitution.length; ++i) {
            for (int j = 0; j < substitution.length; ++j) {
                diffMatrix[i][j] = 0;
            }
        }
    }

    private void generateDifferentialMatrix() {
        clearDifferentialMatrix();
        for (int i = 0; i < substitution.length; ++i) {
            for (int j = 0; j < substitution.length; ++j) {
                diffMatrix[i ^ j][substitution[i] ^ substitution[j]]++;
            }
        }
    }

    public void run() {
        float highestProbability = getHighestProbability();
        while (highestProbability > 0.25f) {
            generateSubstitution(false);
            generateDifferentialMatrix();
            highestProbability = getHighestProbability();
        }

        printSubstitution();

        System.out.println("Differentials table:");
        for (int i = 0; i < substitution.length; ++i) {
            for (int j = 0; j < substitution.length; ++j) {
                if (i != 0 && j != 0) {
                    if (diffMatrix[i][j] > 0) {
                        int index = NumberOfSetBits(i) + NumberOfSetBits(j);
                        float currentProbability = weightStatisticsList.get(index - 2).probability;
                        if (((float)diffMatrix[i][j] / substitution.length) > currentProbability) {
                            if (index == 2 && currentProbability > 0.0f) {
                                System.out.println("\nSome differentials have different probability");
                                return;
                            }
                            weightStatisticsList.get(index - 2).differential.clear();
                            weightStatisticsList.get(index - 2).probability = (float)diffMatrix[i][j] / substitution.length;
                        }

                        if (((float)diffMatrix[i][j] / substitution.length) == weightStatisticsList.get(index - 2).probability) {
                            weightStatisticsList.get(index - 2).differential.add(new Pair<Integer, Integer>(i, j));
                        }
                    }
                }
                System.out.print(diffMatrix[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Highest probability: " + highestProbability);
        System.out.println("Weights table:");
        for (int i = 0; i < weightStatisticsList.size(); ++i) {
            System.out.println("Weight: " + (i + 2) + " probability: " + weightStatisticsList.get(i).probability);
            for (Pair pair : weightStatisticsList.get(i).differential) {
                System.out.println("\t[" + pair.getFirst() + "," + pair.getSecond() + "]");
            }
        }
    }

    private void generateSubstitution(boolean fistRun) {
        List<Integer> randomList;
        if (fistRun) {
            randomList = new LinkedList<Integer>();
            for (int i = 0; i < substitution.length; ++i) {
                randomList.add(i);
            }
        } else {
            randomList = Arrays.asList(substitution);
        }
        Collections.shuffle(randomList);
        substitution = randomList.toArray(new Integer[0]);
        /*{0, 13, 11, 8, 3, 6, 4, 1, 15, 2, 5, 14, 10, 12, 9, 7};*/
    }

    private void printSubstitution() {
        System.out.println("Generated substitution");
        for (Integer aSubstitution : substitution) {
            System.out.print(aSubstitution + " ");
        }
        System.out.println();
    }
}

class Statistics {
    float probability;
    List<Pair<Integer, Integer>> differential = new ArrayList<Pair<Integer, Integer>>();
}
