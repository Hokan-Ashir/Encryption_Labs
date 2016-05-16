package ist.eleven;

import ist.common.AbstractEncryptionCase;

import java.math.BigInteger;
import java.util.Random;

public class IST_11 extends AbstractEncryptionCase {
    private int[] defaultSubstitution = getSubstitution().clone();
    private int[] alreadySwapped = new int[getSubstitution().length];
    private long mask;

    public IST_11() {
        printPermutation();
        printReversedPermutation();
        printReversedSubstitution();

        Random random = new Random();
        mask = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Generated mask: " + mask);
    }

    protected long encrypt(long vector, long key) {
        vector = encryptWithoutKey(vector, key);
        return vector ^ key ^ mask;
    }

    protected long encryptOneCycle(long vector, long key) {
        long result = vector;
        result ^= key;
        //System.out.println("After XOR-key: " + result);
        System.out.println("After XOR-key: " + result);
        System.out.println("After XOR-key, XOR mask: " + (result ^ mask));

        int[] substitution = getSubstitution();
        long temp = 0;
        long tempMask = mask;
        for (int j = 0; j < BLOCK_LENGTH / BIT_LENGTH; ++j) {
            for (int i = 0; i < substitution.length; ++i) {
                substitution[i] = defaultSubstitution[i] ^ (int)(tempMask & 0xF);
                alreadySwapped[i] = 0;
            }
            for (int i = 0; i < substitution.length; ++i) {

                if (alreadySwapped[i] != 1 && alreadySwapped[i ^ (int)(tempMask & 0xF)] != 1) {
                    swapArrayElements(substitution, i, i ^ (int)(tempMask & 0xF));
                    alreadySwapped[i] = 1;
                    alreadySwapped[i ^ (int)(tempMask & 0xF)] = 1;
                }

            }

            temp |= ((long) substitution[(int) result & 0xF] << (j * BIT_LENGTH));
            result >>= BIT_LENGTH;
            tempMask >>= BIT_LENGTH;
        }
        System.out.println("Before permutation: " + temp);
        System.out.println("Before permutation, XOR mask: " + (temp ^ mask));

        temp = permutation(temp);
        System.out.println("After permutation: " + temp);
        System.out.println("After permutation, XOR mask: " + (temp ^ permutation(mask)));
        System.out.println("----");

        return temp ^ permutation(mask) ^ (mask & 0xFFFF0000);
    }

    private void swapArrayElements(int[] arrayToSwap, int i, int j) {
        int temp = arrayToSwap[i];
        arrayToSwap[i] = arrayToSwap[j];
        arrayToSwap[j] = temp;
    }

    private void test() {
        int[] substitution = getSubstitution();
        int xk = 2134;
        System.out.println("x ^ k (0-4): " + (xk & 0xF));
        System.out.println("S(x ^ k) (0-4): " + substitution[xk & 0xF]);
        System.out.println("S(x ^ k) ^ m (0-4): " + (substitution[xk & 0xF] ^ (mask & 0xF)));
        System.out.println("(x ^ k ^ m) (0-4): " + ((xk ^ mask) & 0xF));
        //System.out.println((substitution[xk & 0xF] & (mask & 0xF)) + " = " + "S(" + ((xk ^ mask) & 0xF) + ")");
        //xk ^= mask;

        System.out.println();
        //System.out.println(((xk & 0xF)) + " -> " + reverseSubstitution[xk & 0xF]);
        int[] alreadySwapped = new int[substitution.length];

        long tempMask = mask;
        for (int i = 0; i < substitution.length; ++i) {
            substitution[i] = defaultSubstitution[i] ^ (int)(tempMask & 0xF);
        }
        for (int i = 0; i < substitution.length / 2; ++i) {

            if (alreadySwapped[i] != 1 && alreadySwapped[i ^ (int)(tempMask & 0xF)] != 1) {
            swapArrayElements(substitution, i, i ^ (int)(tempMask & 0xF));
                alreadySwapped[i] = 1;
                alreadySwapped[i ^ (int)(tempMask & 0xF)] = 1;
            }

        }

        System.out.println(substitution[(int)((xk ^ mask) & 0xF)]);
    }

    private void performanceTest(long numberOfIterations) {
        Random random = new Random();
        long x, y, y_, key;
        long highMaskHalf = mask & 0xFFFF0000;
        long lowMaskHalf = mask & 0xFFFF;

        long beginTime = System.nanoTime();
        double defaultTime;
        for (int i = 0; i < numberOfIterations; ++i) {
            x = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            //System.out.println("Random open text (x): " + x);
            key = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            //System.out.println("Random key (k): " + key);
            y = encrypt(x, key);
            //System.out.println("Default encrypted open text (y): " + y);
            //System.out.println();
        }
        defaultTime = (System.nanoTime() - beginTime) / 1000000000.0;

        beginTime = System.nanoTime();
        for (int i = 0; i < numberOfIterations; ++i) {
            x = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            //System.out.println("Random open text (x): " + x);
            key = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            //System.out.println("Random key (k): " + key);
            y_ = encrypt(x ^ highMaskHalf, key ^ lowMaskHalf);
            //System.out.println("Masked encrypted open text (y'): " + y_);
            //System.out.println();
        }
        System.out.println("Total time for " + numberOfIterations + " iterations with default encryption is (sec.): " + defaultTime);
        System.out.println("Total time for " + numberOfIterations + " iterations with masked encryption is (sec.): " + (System.nanoTime() - beginTime) / 1000000000.0);

    }

    public void run() {
        //test();
        Random random = new Random();
        long x = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Random open text (x): " + x);
        long key = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Random key (k): " + key);
        long y = encrypt(x, key);
        System.out.println("Default encrypted open text (y): " + y);
        System.out.println();

        long highMaskHalf = mask & 0xFFFF0000;
        long lowMaskHalf = mask & 0xFFFF;
        long y_ = encrypt(x ^ highMaskHalf, key ^ lowMaskHalf);
        System.out.println("Masked encrypted open text (y'): " + y_);

        //performanceTest(1000000);
    }
}
