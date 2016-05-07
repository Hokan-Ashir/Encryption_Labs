package ist.eleven;

import ist.five.IST_5;
import ist.one.IST_1;
import ist.three.IST_3;

import java.math.BigInteger;
import java.util.Random;

public class IST_11 {
    private static int[] substitution = {6, 5, 3, 11, 14, 10, 8, 1, 12, 2, 15, 4, 0, 13, 9, 7};//{0, 13, 11, 8, 3, 6, 4, 1, 15, 2, 5, 14, 10, 12, 9, 7};
    private static int[] defaultSubstitution = substitution.clone();
    private static int[] alreadySwapped = new int[substitution.length];
    private static int[] reverseSubstitution = new int[(int) Math.pow(2.0d, IST_1.BIT_LENGTH)];
    private int[] reversePermutation = new int[IST_3.BLOCK_LENGTH + 1];
    private static int[] permutation = new int[IST_3.BLOCK_LENGTH + 1];
    private static long mask;

    public IST_11() {
        // get this IST_5 instance only for permutation creation
        IST_5 ist_5 = new IST_5();

        for (int i = 1; i < IST_3.BLOCK_LENGTH + 1; ++i) {
            reversePermutation[(5 * i) % IST_3.BLOCK_LENGTH] = i;
            permutation[i] = (5 * i) % IST_3.BLOCK_LENGTH;
        }
        reversePermutation[IST_3.BLOCK_LENGTH] = IST_3.BLOCK_LENGTH;
        permutation[IST_3.BLOCK_LENGTH] = IST_3.BLOCK_LENGTH;

        System.out.println("Permutation");
        for (int i = 1; i < IST_3.BLOCK_LENGTH + 1; ++i) {
            System.out.print(permutation[i] + " ");
        }
        System.out.println();

        System.out.println("Reversed permutation");
        for (int i = 1; i < IST_3.BLOCK_LENGTH + 1; ++i) {
            System.out.print(reversePermutation[i] + " ");
        }
        System.out.println();

        for (int i = 0; i < reverseSubstitution.length; i++) {
            reverseSubstitution[substitution[i]] = i;
        }

        System.out.println("Reversed substitution");
        for (int i = 0; i < reverseSubstitution.length; ++i) {
            System.out.print(reverseSubstitution[i] + " ");
        }
        System.out.println();

        Random random = new Random();
        mask = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Generated mask: " + mask);
    }

    private static long permutation(long vector) {
        long result = 0;
        for (int i = 0; i < IST_3.BLOCK_LENGTH; ++i) {
            if (BigInteger.valueOf(vector).testBit(i)) {
                result |= (long) 1 << (permutation[i + 1] - 1);
            }
        }
        return result;
    }

    private static long reversedPermutation(long vector) {
        long result = 0;
        for (int i = 0; i < IST_3.BLOCK_LENGTH; ++i) {
            if (BigInteger.valueOf(vector).testBit(permutation[i + 1] - 1)) {
                result |= (long) 1 << i;
            }
        }
        return result;
    }

    public static long encrypt(long vector, long key) {
        vector = encryptWithoutKey(vector, key);
        return vector ^ key ^ mask;
    }

    private static long encryptWithoutKey(long vector, long key) {
        for (int i = 0; i < IST_3.NUMBER_OF_ROUNDS; i++) {
            vector = encryptOneCycle(vector, key);
        }
        return vector;
    }

    private static long encryptOneCycle(long vector, long key) {
        long result = vector;
        result ^= key;
        //System.out.println("After XOR-key: " + result);
        System.out.println("After XOR-key: " + result);
        System.out.println("After XOR-key, XOR mask: " + (result ^ mask));

        long temp = 0;
        long tempMask = mask;
        for (int j = 0; j < IST_3.BLOCK_LENGTH / IST_1.BIT_LENGTH; ++j) {

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

            temp |= ((long) substitution[(int) result & 0xF] << (j * IST_1.BIT_LENGTH));
            result >>= IST_1.BIT_LENGTH;
            tempMask >>= IST_1.BIT_LENGTH;
        }
        System.out.println("Before permutation: " + temp);
        System.out.println("Before permutation, XOR mask: " + (temp ^ mask));

        temp = permutation(temp);
        System.out.println("After permutation: " + temp);
        System.out.println("After permutation, XOR mask: " + (temp ^ permutation(mask)));
        System.out.println("----");

        return temp ^ permutation(mask) ^ (mask & 0xFFFF0000);
    }

    private static void swapArrayElements(int[] arrayToSwap, int i, int j) {
        int temp = arrayToSwap[i];
        arrayToSwap[i] = arrayToSwap[j];
        arrayToSwap[j] = temp;
    }

    private void test() {
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
            y = IST_5.encrypt(x, key);
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
        long y = IST_5.encrypt(x, key);
        System.out.println("Default encrypted open text (y): " + y);
        System.out.println();

        long highMaskHalf = mask & 0xFFFF0000;
        long lowMaskHalf = mask & 0xFFFF;
        long y_ = encrypt(x ^ highMaskHalf, key ^ lowMaskHalf);
        System.out.println("Masked encrypted open text (y'): " + y_);

        //performanceTest(1000000);
    }
}
