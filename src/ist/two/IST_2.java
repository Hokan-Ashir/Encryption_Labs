package ist.two;

import ist.Pair;
import ist.one.IST_1;

import java.util.ArrayList;
import java.util.List;

public class IST_2 {
    private int[] substitution = {11, 13, 3, 7, 5, 15, 4, 8, 14, 1, 9, 12, 0, 6, 2, 10};
    // 6, 5, 3, 11, 14, 10, 8, 1, 12, 2, 15, 4, 0, 13, 9, 7 - my
    //{0, 13, 11, 8, 3, 6, 4, 1, 15, 2, 5, 14, 10, 12, 9, 7}; - workbook
    // 11, 13, 3, 7, 5, 15, 4, 8, 14, 1, 9, 12, 0, 6, 2, 10 - andrew
    private int[][] linearSumMatrix = new int[substitution.length][substitution.length];
    private List<Statistics> distributionList = new ArrayList<Statistics>();

    public IST_2() {
        for (int i = 0; i < Math.pow(2.0d, IST_1.BIT_LENGTH); ++i) {
            distributionList.add(new Statistics());
        }
    }

    private void createDistributionList() {
        for (int i = 0; i < substitution.length; ++i) {
            for (int j = 0; j < substitution.length; ++j) {
                if (linearSumMatrix[i][j] != 0 && linearSumMatrix[i][j] != Math.pow(2.0d, IST_1.BIT_LENGTH - 1)) {
                    int index = IST_1.NumberOfSetBits(i) + IST_1.NumberOfSetBits(j);
                    if (distributionList.get(index).dominance == 0.0d) {
                        distributionList.get(index).dominance = Math.abs(linearSumMatrix[i][j]) / Math.pow(2.0d, IST_1.BIT_LENGTH);
                    }
                    distributionList.get(index).differential.add(new Pair<Integer, Integer>(i, j));
                }
            }
        }
    }

    private void printDistributionList() {
        System.out.println("Linear sums distribution list (only non zero sums):");
        for (int i = 0; i < Math.pow(2.0d, IST_1.BIT_LENGTH); ++i) {
            if (distributionList.get(i).dominance != 0.0d) {
                System.out.println("Weight: " + i
                        + " dominance: " + distributionList.get(i).dominance
                        + " distribution list size: " + distributionList.get(i).differential.size());
                System.out.print("\t");
                for (int j = 0; j < distributionList.get(i).differential.size(); ++j) {
                    System.out.print("[" + distributionList.get(i).differential.get(j).getFirst()
                            + "," + distributionList.get(i).differential.get(j).getSecond() + "] ");
                }
                System.out.println();
            }
        }
    }

    private void createAndPrintLinearSumsMatrix() {
        System.out.println("Linear sums matrix:");
        for (int i = 0; i < substitution.length; ++i) {
            for (int j = 0; j < substitution.length; ++j) {

                for (int k = 0; k < substitution.length; ++k) {
                    if (IST_1.NumberOfSetBits(i & k) % 2 == IST_1.NumberOfSetBits(j & substitution[k]) % 2) {
                        linearSumMatrix[i][j]++;
                    }
                }

                linearSumMatrix[i][j] -= Math.pow(2.0d, IST_1.BIT_LENGTH - 1);
                System.out.print(linearSumMatrix[i][j] + " ");
            }
            System.out.println();

        }
    }

    public void run() {
        createAndPrintLinearSumsMatrix();
        createDistributionList();
        printDistributionList();
    }
}

class Statistics {
    double dominance;
    List<Pair<Integer, Integer>> differential = new ArrayList<Pair<Integer, Integer>>();
}
