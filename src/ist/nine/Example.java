package ist.nine;

import ist.one.IST_1;

public class Example {
    private static final int BLOCK_LENGTH = 64;

    private final int[] testKey = {
            // key for 9 lab (@workbook)
            1, 0, 1, 1,
            0, 1, 0, 0,
            1, 0, 1, 0,
            1, 1, 0, 1,
            1, 1, 0, 0,
            0, 0, 1, 1,
            0, 1, 1, 0,
            1, 0, 0, 0,
            1, 0, 1, 0,
            0, 1, 1, 1,
            0, 1, 0, 0,
            1, 0, 1, 0,
            0, 0, 0, 1,
            0, 1, 0, 1,
            1, 0, 1, 1,
            0, 1, 0, 1
            // key for 9 lab (book)
            /*1, 0, 1, 1,
            0, 1, 1, 0,
            1, 1, 1, 0,
            1, 1, 0, 1,
            1, 1, 0, 0,
            0, 0, 1, 1,
            0, 1, 1, 0,
            1, 0, 0, 0,
            1, 0, 1, 0,
            1, 1, 1, 1,
            0, 1, 0, 0,
            1, 0, 1, 0,
            0, 0, 0, 1,
            0, 1, 0, 1,
            1, 0, 1, 0,
            0, 1, 0, 1*/
            // key for 8/7 lab
            /*1, 0, 0, 0,
            0, 1, 1, 0,
            1, 1, 1, 0,
            1, 1, 0, 1,
            1, 1, 0, 0,
            0, 0, 1, 1,
            0, 0, 1, 0,
            1, 0, 0, 0,
            0, 0, 1, 0,
            0, 1, 1, 1,
            0, 0, 0, 0,
            1, 0, 1, 0,
            0, 0, 0, 1,
            0, 1, 0, 1,
            1, 0, 1, 0,
            0, 1, 0, 1*/

    };

    private final int[] testXVector = {
            0, 0, 1, 0,
            0, 0, 0, 1,
            1, 1, 0, 1,
            0, 1, 1, 0,
            1, 1, 0, 0,
            0, 0, 0, 1,
            1, 1, 0, 0,
            0, 1, 1, 0,
            1, 0, 1, 1,
            0, 1, 0, 1,
            0, 1, 1, 1,
            1, 0, 0, 1,
            0, 0, 0, 0,
            1, 0, 1, 0,
            1, 0, 1, 1,
            0, 1, 1, 1
    };

    private int[] permutation = {
            0, 23, 46, 5, 28, 51, 10, 33,
            56, 15, 38, 61, 20, 43, 2, 25,
            48, 7, 30, 53, 12, 35, 58, 17,
            40, 63, 22, 45, 4, 27, 50, 9,
            32, 55, 14, 37, 60, 19, 42, 1,
            24, 47, 6, 29, 52, 11, 34, 57,
            16, 39, 62, 21, 44, 3, 26, 49,
            8, 31, 54, 13, 36, 59, 18, 41
    };

    private int[] substitution = {
            0, 13, 11, 8, 3, 6, 4, 1, 15, 2, 5, 14, 10, 12, 9, 7
    };

    // TODO make twoAdic-methods
    // ^ -> twoAdicXOR(int x , int y, int m) {return x + y (mod 2^m)}
    // & -> twoAdicAND(int x , int y, int m) {return x * y (mod 2^m)}
    private int v1(int u1, int u2, int u3, int u4) {
        return u4 ^ u3 ^ (u3 & u4) ^ (u2 & u4) ^ (u2 & u3) ^ (u2 & u3 & u4) ^ u1 ^ (u1 & u3 & u4);
    }

    private int v2(int u1, int u2, int u3, int u4) {
        return u4 ^ (u3 & u4) ^ (u2 & u3) ^ (u2 & u3 & u4) ^ u1 ^ (u1 & u2) ^ (u1 & u2 & u3);
    }

    private int v3(int u1, int u2, int u3, int u4) {
        return u3 ^ (u3 & u4) ^ u2 ^ (u2 & u3 & u4) ^ u1 ^ (u1 & u2) ^ (u1 & u2 & u4);
    }

    private int v4(int u1, int u2, int u3, int u4) {
        return u4 ^ u3 ^ u2 ^ u1 ^ (u1 & u3) ^ (u1 & u2 & u4) ^ (u1 & u2 & u3);
    }

    private int[] oneCycleEncryption(int[] xVector, int[] key) {
        // xor
        for (int i = 0; i < xVector.length; ++i) {
            xVector[i] ^= key[i];
        }

        // permutation
        int[] resultVector = new int[xVector.length];
        for (int i = 0; i < xVector.length; ++i) {
            resultVector[i] = xVector[permutation[i]];
        }

        // substitution
        for (int i = 0; i < resultVector.length; i += IST_1.BIT_LENGTH) {
            int u1 = resultVector[i];
            int u2 = resultVector[i + 1];
            int u3 = resultVector[i + 2];
            int u4 = resultVector[i + 3];
            resultVector[i] = v1(u1, u2, u3, u4);
            resultVector[i + 1] = v2(u1, u2, u3, u4);
            resultVector[i + 2] = v3(u1, u2, u3, u4);
            resultVector[i + 3] = v4(u1, u2, u3, u4);

            // using
            /*int vector = (resultVector[i] << 3) + (resultVector[i + 1] << 2) + (resultVector[i + 2] << 1) + resultVector[i + 3];
            vector = substitution[vector];
            resultVector[i] = (vector & 0x8) >> 3;
            resultVector[i + 1] = (vector & 0x4) >> 2;
            resultVector[i + 2] = (vector & 0x2) >> 1;
            resultVector[i + 3] = vector & 0x1;*/
        }
        /*double[] resultVector = new double[inputVector.length];
        for (int i = 0; i < resultVector.length; ++i) {
            resultVector[i] = inputVector[i];
        }*/

        int[] slideBits = new int[25];
        for (int i = 0; i < 25; ++i) {
            slideBits[i] = resultVector[i];
        }

        int[] leftBits = new int[resultVector.length - 25];
        for (int i = 0; i < resultVector.length - 25; ++i) {
            leftBits[i] = resultVector[25 + i];
        }

        /*System.out.println();
        for (double leftBit : leftBits) {
            System.out.print(leftBit + " ");
        }
        System.out.println();
        for (double leftBit : slideBits) {
            System.out.print(leftBit + " ");
        } */

        for (int i = 0; i < leftBits.length; ++i) {
            resultVector[i] = leftBits[i];
        }

        for (int i = 0; i < slideBits.length; ++i) {
            resultVector[leftBits.length + i] = slideBits[i];
        }
        return resultVector;
    }

    public void run() {
        int[] resultVector = oneCycleEncryption(testXVector, testKey);
        for (int i = 0; i < 15; ++i) {
            resultVector = oneCycleEncryption(resultVector, testKey);
        }

        // xor
        /*for (int i = 0; i < resultVector.length; ++i) {
            resultVector[i] ^= testXVector[i];
        }*/

        for (int j = 0; j < resultVector.length; ++j) {
            if (j != 0 && j % IST_1.BIT_LENGTH == 0) {
                System.out.print(" ");
            }
            System.out.print(resultVector[j]);
        }
        System.out.println();
    }
}
