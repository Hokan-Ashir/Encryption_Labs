package ist.common;

import java.math.BigInteger;

/**
 * @author Ачкасов Антон
 */
public class AbstractEncryptionCase {
    protected final static int BIT_LENGTH = 4;
    protected static final int NUMBER_OF_ROUNDS = 4;
    protected static final int BLOCK_LENGTH = 32;

    private int[] substitution;
    private int[] reverseSubstitution;
    // {6, 5, 3, 11, 14, 10, 8, 1, 12, 2, 15, 4, 0, 13, 9, 7}; - my
    // {0, 13, 11, 8, 3, 6, 4, 1, 15, 2, 5, 14, 10, 12, 9, 7}; - workbook
    // {11, 13, 3, 7, 5, 15, 4, 8, 14, 1, 9, 12, 0, 6, 2, 10}; - andrew

    private int[] reversePermutation = new int[BLOCK_LENGTH + 1];
    private int[] permutation = new int[BLOCK_LENGTH + 1];

    public AbstractEncryptionCase() {
        createSubstitution();
        createReversedSubstitution(substitution);
        createPermutation();
        createReversedPermutation();
    }

    private void createSubstitution() {
        substitution = new int[]{ 6, 5, 3, 11, 14, 10, 8, 1, 12, 2, 15, 4, 0, 13, 9, 7 };
    }

    private void createReversedSubstitution(int[] substitution) {
        reverseSubstitution = new int[substitution.length];
    }

    private void createPermutation() {
        for (int i = 1; i < BLOCK_LENGTH + 1; ++i) {
            permutation[i] = (5 * i) % BLOCK_LENGTH;
        }
        permutation[BLOCK_LENGTH] = BLOCK_LENGTH;
    }

    private void createReversedPermutation() {
        for (int i = 1; i < BLOCK_LENGTH + 1; ++i) {
            reversePermutation[(5 * i) % BLOCK_LENGTH] = i;
        }
        reversePermutation[BLOCK_LENGTH] = BLOCK_LENGTH;
    }

    protected int[] getSubstitution() {
        return substitution;
    }

    protected int[] getReversedSubstitution() {
        return reverseSubstitution;
    }

    protected void printReversedSubstitution() {
        System.out.println("Reversed substitution");
        for (int aReverseSubstitution : reverseSubstitution) {
            System.out.print(aReverseSubstitution + " ");
        }
        System.out.println();
    }

    protected int[] getReversePermutation() {
        return reversePermutation;
    }

    protected int[] getPermutation() {
        return permutation;
    }

    protected void printPermutation() {
        System.out.println("Permutation");
        for (int i = 1; i < BLOCK_LENGTH + 1; ++i) {
            System.out.print(permutation[i] + " ");
        }
        System.out.println();
    }

    protected void printReversedPermutation() {
        System.out.println("Reversed permutation");
        for (int i = 1; i < BLOCK_LENGTH + 1; ++i) {
            System.out.print(reversePermutation[i] + " ");
        }
        System.out.println();
    }

    protected long encrypt(long vector, long key) {
        vector = encryptWithoutKey(vector, key);
        return vector ^ key;
    }

    protected long encryptWithoutKey(long vector, long key) {
        for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
            vector = encryptOneCycle(vector, key);
        }
        return vector;
    }

    protected long encryptOneCycle(long vector, long key) {
        long result = vector;
        result ^= key;
        System.out.println("After XOR key: " + result);

        int[] substitution = getSubstitution();
        //System.out.println("After XOR-key: " + result);
        long temp = 0;
        for (int j = 0; j < BLOCK_LENGTH / BIT_LENGTH; ++j) {
            temp |= ((long) substitution[(int) result & 0xF] << (j * BIT_LENGTH));
            result >>= BIT_LENGTH;
        }
        System.out.println("Before permutation: " + temp);
        temp = permutation(temp);
        System.out.println("After  permutation: " + temp);
        System.out.println("----");
        //System.out.println("Before permutation: " + temp);
        return temp;
    }

    protected long permutation(long vector) {
        long result = 0;
        for (int i = 0; i < BLOCK_LENGTH; ++i) {
            if (BigInteger.valueOf(vector).testBit(i)) {
                result |= (long) 1 << (permutation[i + 1] - 1);
            }
        }
        return result;
    }

    public long decrypt(long encryptedVector, long  key) {
        encryptedVector ^= key;
        for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
            encryptedVector = decryptOneCycle(encryptedVector, key);
        }
        return encryptedVector;
    }

    private long decryptOneCycle(long encryptedVector, long key) {
        long result = reversedPermutation(encryptedVector);

        //System.out.println("After reversed permutation: " + key);
        int[] reverseSubstitution = getReversedSubstitution();
        long temp = 0;
        for (int j = 0; j < BLOCK_LENGTH / BIT_LENGTH; ++j) {
            temp |= ((long) reverseSubstitution[(int) result & 0xF] << (j * BIT_LENGTH));
            result >>= BIT_LENGTH;
        }

        return temp ^ key;
    }

    protected long reversedPermutation(long vector) {
        long result = 0;
        for (int i = 0; i < BLOCK_LENGTH; ++i) {
            if (BigInteger.valueOf(vector).testBit(permutation[i + 1] - 1)) {
                result |= (long) 1 << i;
            }
        }
        return result;
    }

    protected long decryptOneCycleKey(long encryptedVector, long openVector) {
        long key;
        key = reversedPermutation(encryptedVector);
        //System.out.println("After reversed permutation: " + key);
        int[] reverseSubstitution = getReversedSubstitution();
        long temp = 0;
        for (int j = 0; j < BLOCK_LENGTH / BIT_LENGTH; ++j) {
            temp |= ((long) reverseSubstitution[(int) key & 0xF] << (j * BIT_LENGTH));
            key >>= BIT_LENGTH;
        }
        key = temp;
        //System.out.println("Before XOR-key: " + key);
        return key ^ openVector;
    }

    protected String toBinaryString(long value) {
        return String.format("%32s", Long.toBinaryString(value)).replace(' ', '0');
    }

    protected int NumberOfSetBits(int i) {
        i = i - ((i >> 1) & 0x55555555);
        i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
        return (((i + (i >> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
    }
}
