package ist.six;

import ist.common.AbstractEncryptionCase;

public class Example extends AbstractEncryptionCase {
    private double[] testVector = {
            0.68d, 0.05d, 0.09d, 0.13d, 0.98d, 0.94d, 0.9d, 0.86d,
            0.97d, 0.93d, 0.89d, 0.82d, 0.96d, 0.92d, 0.88d, 0.84,
            0.17d, 0.21d, 0.25d, 0.29d, 0.85d, 0.78d, 0.74d, 0.7d,
            0.81d, 0.77d, 0.73d, 0.69d, 0.8d, 0.76d, 0.72d, 0.01d
    };

    private double[] testCipherText = {
            0, 0, 1, 1,
            0, 1, 0, 0,
            0, 1, 1, 1,
            1, 0, 1, 0,
            1, 1, 0, 0,
            0, 0, 0, 0,
            1, 0, 1, 1,
            1, 0, 0, 1
    };

    private double[] testKey = {
            1, 0, 0, 0,
            0, 1, 1, 1,
            0, 1, 1, 0,
            0, 1, 0, 1,
            0, 1, 0, 0,
            0, 0, 1, 1,
            0, 0, 1, 0,
            0, 0, 0, 1
    };

    private long key = Long.valueOf("10000111011001010100001100100001", 2);

    private int[] permutation = {
            32, 5, 9, 13, 2, 6, 10, 14,
            3, 7, 11, 18, 4, 8, 12, 16,
            17, 21, 25, 29, 15, 22, 26, 30,
            19, 23, 27, 31, 20, 24, 28, 1
    };

    private double v1(double u1, double u2, double u3, double u4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{u1, u2, 1 - u4}),
                GridOperations.gridMIN(new double[]{u1, u2, 1 - u3}),
                GridOperations.gridMIN(new double[]{u1, 1 - u3, 1 - u4}),
                GridOperations.gridMIN(new double[]{1 - u2, u3, u4}),
                GridOperations.gridMIN(new double[]{1 - u1, 1 - u2, u4}),
                GridOperations.gridMIN(new double[]{1 - u1, 1 - u2, u3})
        });
    }

    private double v2(double u1, double u2, double u3, double u4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{1 - u1, u2, u3, 1 - u4}),
                GridOperations.gridMIN(new double[]{1 - u1, 1 - u3, u4}),
                GridOperations.gridMIN(new double[]{u1, u2, u4}),
                GridOperations.gridMIN(new double[]{u1, u3, u4}),
                GridOperations.gridMIN(new double[]{u1, 1 - u2, 1 - u4})
        });
    }

    private double v3(double u1, double u2, double u3, double u4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{u2, 1 - u3, 1 - u4}),
                GridOperations.gridMIN(new double[]{1 - u1, u2, 1 - u3}),
                GridOperations.gridMIN(new double[]{u1, u3, u4}),
                GridOperations.gridMIN(new double[]{u1, 1 - u2, 1 - u3}),
                GridOperations.gridMIN(new double[]{1 - u1, 1 - u2, u3, 1 - u4})
        });
    }

    private double v4(double u1, double u2, double u3, double u4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{u1, u2, u3}),
                GridOperations.gridMIN(new double[]{u2, u3, u4}),
                GridOperations.gridMIN(new double[]{u1, 1 - u2, 1 - u4}),
                GridOperations.gridMIN(new double[]{1 - u2, u3, 1 - u4}),
                GridOperations.gridMIN(new double[]{1 - u1, u2, 1 - u3, 1 - u4}),
                GridOperations.gridMIN(new double[]{1 - u1, 1 - u2, 1 - u3, u4})
        });
    }

    private double[] testOneCycleEncryption(double[] inputVector) {
        long key = this.key;
        for (int i = 0; i < inputVector.length; ++i) {
            inputVector[i] = GridOperations.gridXOR(inputVector[i], /*testKey[i]*/(key & ((long)1 << (inputVector.length - 1 - i))) >> (inputVector.length - 1 - i));
            //key >>= 1;
        }

        double[] resultVector = new double[inputVector.length];
        for (int i = 0; i < resultVector.length; ++i) {
            resultVector[i] = inputVector[permutation[i] - 1];
        }

        for (int i = 0; i < resultVector.length; i += BIT_LENGTH) {
            double u1 = resultVector[i];
            double u2 = resultVector[i + 1];
            double u3 = resultVector[i + 2];
            double u4 = resultVector[i + 3];
            resultVector[i] = v1(u1, u2, u3, u4);
            resultVector[i + 1] = v2(u1, u2, u3, u4);
            resultVector[i + 2] = v3(u1, u2, u3, u4);
            resultVector[i + 3] = v4(u1, u2, u3, u4);
        }
        /*double[] resultVector = new double[inputVector.length];
        for (int i = 0; i < resultVector.length; ++i) {
            resultVector[i] = inputVector[i];
        }*/

        double[] slideBits = new double[15];
        for (int i = 0; i < 15; ++i) {
            slideBits[i] = resultVector[i];
        }

        double[] leftBits = new double[resultVector.length - 15];
        for (int i = 0; i < resultVector.length - 15; ++i) {
            leftBits[i] = resultVector[15 + i];
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
        double[] resultVector = new double[testCipherText.length];
        /*for (int i = 0; i < resultVector.length; ++i) {
            resultVector[i] = i;
        }

        double[] slideBits = new double[15];
        for (int i = 0; i < 15; ++i) {
            slideBits[i] = resultVector[resultVector.length - i - 1];
        }

        double[] leftBits = new double[resultVector.length - 15];
        for (int i = 0; i < resultVector.length - 15; ++i) {
            leftBits[i] = resultVector[i];
        }

        for (int i = 0; i < leftBits.length; ++i) {
            resultVector[resultVector.length - 1 - i] = leftBits[i];
        }

        for (int i = 0; i < slideBits.length; ++i) {
            resultVector[resultVector.length - 1 - leftBits.length - i] = slideBits[i];
        }*/

        /*for (int i = 15, j = 0; i > 0; --i, ++j) {
            resultVector[i] = slideBits[j];
        }*/
        resultVector = testOneCycleEncryption(testOneCycleEncryption(testVector));
        /*for (int i = 0; i < testVector.length; ++i) {
            result[i] = gridXOR(testVector[i], testCipherText[i]);
        }*/

        System.out.println("Result F2(x) vector:");
        for (int i = 0; i < resultVector.length; i += (BIT_LENGTH * 2)) {
            System.out.println(resultVector[i] + " " +
                    resultVector[i + 1] + " " +
                    resultVector[i + 2] + " " +
                    resultVector[i + 3] + " " +
                    resultVector[i + 4] + " " +
                    resultVector[i + 5] + " " +
                    resultVector[i + 6] + " " +
                    resultVector[i + 7]/* + " " +
                    result[i + 8]*/);
        }
        System.out.println();

        double[] fullEncryption = testOneCycleEncryption(testOneCycleEncryption(resultVector));
        System.out.println("Result F4(x) vector:");
        for (int i = 0; i < fullEncryption.length; i += (BIT_LENGTH * 2)) {
            System.out.println(fullEncryption[i] + " " +
                    fullEncryption[i + 1] + " " +
                    fullEncryption[i + 2] + " " +
                    fullEncryption[i + 3] + " " +
                    fullEncryption[i + 4] + " " +
                    fullEncryption[i + 5] + " " +
                    fullEncryption[i + 6] + " " +
                    fullEncryption[i + 7]/* + " " +
                    result[i + 8]*/);
        }

        /*for (int i = resultVector.length - 1; i > -1; i -= (IST_1.BIT_LENGTH * 2)) {
            System.out.println(resultVector[i] + " " +
                    resultVector[i - 1] + " " +
                    resultVector[i - 2] + " " +
                    resultVector[i - 3] + " " +
                    resultVector[i - 4] + " " +
                    resultVector[i - 5] + " " +
                    resultVector[i - 6] + " " +
                    resultVector[i - 7]/* + " " +
                    result[i + 8]*//*);
        }
        System.out.println();
        for (double slideBit : slideBits) {
            System.out.print(slideBit + " ");
        }

        System.out.println();
        for (double slideBit : leftBits) {
            System.out.print(slideBit + " ");
        }*/
    }
}
