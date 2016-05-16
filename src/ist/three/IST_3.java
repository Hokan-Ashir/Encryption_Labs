package ist.three;

import ist.common.AbstractEncryptionCase;

public class IST_3 extends AbstractEncryptionCase {
    public static final int NUMBER_OF_ROUNDS = 4;
    public static final int BLOCK_LENGTH = 32;

    private long generateKey() {
        return (long)(Math.random() * (Math.pow(2.0d, BLOCK_LENGTH) + 1));
    }

    public void run() {
        for (int i = 1; i < BLOCK_LENGTH + 1; ++i) {
            System.out.print((5 * i) % BLOCK_LENGTH + " ");
        }

        int[] substitution = getSubstitution();
        int[][] diffMatrix = new int[substitution.length][substitution.length];
        System.out.println(permutation(2566497465l));
            for (int i = 0; i < substitution.length; ++i) {
                for (int j = 0; j < substitution.length; ++j) {
                    diffMatrix[i ^ j][substitution[i] ^ substitution[j]]++;
                }
            }

        for (int i = 0; i < substitution.length; ++i) {
            for (int j = 0; j < substitution.length; ++j) {
                System.out.print(diffMatrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.print(generateKey());
    }
}
