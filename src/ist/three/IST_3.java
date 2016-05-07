package ist.three;

import ist.one.IST_1;

public class IST_3 {
    public static final int NUMBER_OF_ROUNDS = 4;
    public static final int BLOCK_LENGTH = 32;

    private int[] substitution = {6, 5, 3, 11, 14, 10, 8, 1, 12, 2, 15, 4, 0, 13, 9, 7};//{0, 13, 11, 8, 3, 6, 4, 1, 15, 2, 5, 14, 10, 12, 9, 7};
    private int[][] diffMatrix = new int[substitution.length][substitution.length];

    public static long permutation(long vector) {
        long result = 0;
        int i = 1;
        while (i < BLOCK_LENGTH) {
            result |= (vector & 1) << ((5 * i) % BLOCK_LENGTH - 1);
            vector >>= 1;
            i++;
        }
        result |= vector << (i - 1);
        return result;
    }

    private long encrypt(long vector, long key) {
        long result = vector;
        for (int i = 0; i < NUMBER_OF_ROUNDS; ++i) {
            result ^= key;
            long temp = 0;
            int index = 0xF;
            for (int j = 0; j < BLOCK_LENGTH / IST_1.BIT_LENGTH; ++j) {
                temp |= substitution[(int)result & index];
                index <<= IST_1.BIT_LENGTH;
            }
            result = temp;
            result = permutation(result);
        }
        return result ^ key;
    }

    private long generateKey() {
        return (long)(Math.random() * (Math.pow(2.0d, BLOCK_LENGTH) + 1));
    }

    public void run() {
        for (int i = 1; i < BLOCK_LENGTH + 1; ++i) {
            System.out.print((5 * i) % BLOCK_LENGTH + " ");
        }
        /*System.out.println(permutation(2566497465l));
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
        }*/
        System.out.print(generateKey());
    }
}
