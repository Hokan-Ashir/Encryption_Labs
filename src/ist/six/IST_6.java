package ist.six;

import ist.common.AbstractEncryptionCase;

import java.util.Random;

public class IST_6  extends AbstractEncryptionCase {
    private double[] keyVector = new double[BLOCK_LENGTH];
    private long y;

    private void generateInputKeyVector() {
        Random random = new Random();
        System.out.println("Random keyVector: ");
        for (int i = 0; i < keyVector.length; ++i) {
            double newValue;
            boolean valid;
            do {
                valid = true;
                newValue = Math.round(random.nextDouble() * 100) / 100.0d;
                if (newValue == 0.5 || newValue == 1 || newValue == 0 || Math.abs(newValue - 0.5d) < 0.15d) {
                    valid = false;
                } else {
                    for (int j = 0; j < i; ++j) {
                        if (Math.round(Math.abs(newValue - 0.5) * 100) / 100.0d == Math.round(Math.abs(keyVector[j] - 0.5) * 100) / 100.0d) {
                            valid = false;
                            break;
                        }
                    }
                }
            } while (!valid);
            keyVector[i] = newValue;
            if (i!= 0 && i % 8 == 0) {
                System.out.println();
            }
            System.out.print(keyVector[i] + " ");

        }
        System.out.println();
    }

    public IST_6() {
        generateInputKeyVector();
    }

    private double v1(double u1, double u2, double u3, double u4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{u1, u3, 1 - u4}),
                GridOperations.gridMIN(new double[]{1 - u1, u2, 1 - u4}),
                GridOperations.gridMIN(new double[]{u2, 1 - u3, u4}),
                GridOperations.gridMIN(new double[]{1 - u1, 1 - u2, u3, u4}),
                GridOperations.gridMIN(new double[]{u1, 1 - u2, 1 - u4})
        });
    }

    private double v2(double u1, double u2, double u3, double u4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{1 - u1, 1 - u3, 1 - u4}),
                GridOperations.gridMIN(new double[]{u1, u2, u4}),
                GridOperations.gridMIN(new double[]{u1, 1 - u2, u3}),
                GridOperations.gridMIN(new double[]{u1, 1 - u2, 1 - u4}),
                GridOperations.gridMIN(new double[]{1 - u1, 1 - u2, 1 - u3})
        });
    }

    private double v3(double u1, double u2, double u3, double u4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{1 - u1, u2, 1 - u3}),
                GridOperations.gridMIN(new double[]{u1, u2, u3, u4}),
                GridOperations.gridMIN(new double[]{u1, 1 - u2, 1 - u3, u4}),
                GridOperations.gridMIN(new double[]{1 - u1, 1 - u2, u3}),
                GridOperations.gridMIN(new double[]{1 - u2, u3, 1 - u4}),
                GridOperations.gridMIN(new double[]{1 - u1, 1 - u2, 1 - u4})
        });
    }

    private double v4(double u1, double u2, double u3, double u4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{u1, u2, u3}),
                GridOperations.gridMIN(new double[]{u1, u2, u4}),
                GridOperations.gridMIN(new double[]{u2, u3, u4}),
                GridOperations.gridMIN(new double[]{1 - u1, 1 - u2, u4}),
                GridOperations.gridMIN(new double[]{1 - u2, u3, 1 - u4})
        });
    }

    private double[] innerEncryption(double[] inputVector) {
        // substitution
        double[] resultVector = new double[inputVector.length];
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

        int[] permutation = getPermutation();
        // permutation
        for (int i = 0; i < resultVector.length; ++i) {
            resultVector[i] = inputVector[permutation[i] - 1];
        }

        return resultVector;
    }

    private double[] oneCycleEncryption(double[] inputVector, double[] key) {
        // xor
        for (int i = 0; i < inputVector.length; ++i) {
            inputVector[i] = GridOperations.gridXOR(inputVector[i], key[i]);
        }

        return innerEncryption(inputVector);
    }

    private double[] oneCycleEncryption(double[] inputVector, long key) {
        // xor
        for (int i = 0; i < inputVector.length; ++i) {
            inputVector[i] = GridOperations.gridXOR(inputVector[i], (key & ((long) 1 << (inputVector.length - 1 - i))) >> (inputVector.length - 1 - i));
        }

        return innerEncryption(inputVector);
    }

    private double u1(double v1, double v2, double v3, double v4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{v1, 1 - v3, 1 - v4}),
                GridOperations.gridMIN(new double[]{v1, 1 - v3, v4}),
                GridOperations.gridMIN(new double[]{v2, v3, v4}),
                GridOperations.gridMIN(new double[]{1 - v1, 1 - v2, 1 - v4})
        });
    }

    private double u2(double v1, double v2, double v3, double v4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{v1, v3, 1 - v4}),
                GridOperations.gridMIN(new double[]{v1, 1 - v3, v4}),
                GridOperations.gridMIN(new double[]{1 - v1, v2, v3, v4}),
                GridOperations.gridMIN(new double[]{v1, 1 - v2, 1 - v3}),
                GridOperations.gridMIN(new double[]{1 - v1, 1 - v2, 1 - v3})
        });
    }

    private double u3(double v1, double v2, double v3, double v4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{1 - v1, v2, 1 - v3, 1 - v4}),
                GridOperations.gridMIN(new double[]{v3, v4}),
                GridOperations.gridMIN(new double[]{1 - v2, 1 - v3, v4}),
                GridOperations.gridMIN(new double[]{v1, 1 - v2, 1 - v3})
        });
    }

    private double u4(double v1, double v2, double v3, double v4) {
        return GridOperations.gridMAX(new double[]{
                GridOperations.gridMIN(new double[]{1 - v1, v2, 1 - v3}),
                GridOperations.gridMIN(new double[]{v2, 1 - v3, v4}),
                GridOperations.gridMIN(new double[]{1 - v1, v2, v4}),
                GridOperations.gridMIN(new double[]{1 - v1, 1 - v3, v4}),
                GridOperations.gridMIN(new double[]{v1, 1 - v2, v3}),
                GridOperations.gridMIN(new double[]{1 - v2, v3, 1 - v4})
        });
    }

    private double[] innerDecryption(double[] inputVector) {
        // reversed permutation
        int[] reversedPermutation = getReversePermutation();
        double[] resultVector = new double[inputVector.length];
        for (int i = 0; i < resultVector.length; ++i) {
            resultVector[i] = inputVector[reversedPermutation[i] - 1];
        }

        // reversed substitution
        for (int i = 0; i < resultVector.length; i += BIT_LENGTH) {
            double v1 = resultVector[i];
            double v2 = resultVector[i + 1];
            double v3 = resultVector[i + 2];
            double v4 = resultVector[i + 3];
            resultVector[i] = u1(v1, v2, v3, v4);
            resultVector[i + 1] = u2(v1, v2, v3, v4);
            resultVector[i + 2] = u3(v1, v2, v3, v4);
            resultVector[i + 3] = u4(v1, v2, v3, v4);
        }

        return resultVector;
    }

    private double[] oneCycleDecryption(double[] inputVector, double[] key) {
        double[] resultVector = innerDecryption(inputVector);

        // xor
        for (int i = 0; i < inputVector.length; ++i) {
            resultVector[i] = GridOperations.gridXOR(resultVector[i], key[i]);
        }

        return resultVector;
    }

    private double[] oneCycleDecryption(double[] inputVector, long key) {
        double[] resultVector = innerDecryption(inputVector);

        // xor
        for (int i = 0; i < inputVector.length; ++i) {
            resultVector[i] = GridOperations.gridXOR(resultVector[i], (key & ((long) 1 << (inputVector.length - 1 - i))) >> (inputVector.length - 1 - i));
        }

        return resultVector;
    }

    private void printDoubleVector(double[] inputVector) {
        for (int i = 0; i < inputVector.length; i += (BIT_LENGTH * 2)) {
            System.out.println(inputVector[i] + " " +
                    inputVector[i + 1] + " " +
                    inputVector[i + 2] + " " +
                    inputVector[i + 3] + " " +
                    inputVector[i + 4] + " " +
                    inputVector[i + 5] + " " +
                    inputVector[i + 6] + " " +
                    inputVector[i + 7]);
        }
    }

    private void doOneIteration(long x, long key) {
        double[] resultVectorTwoEncryptionCycles = oneCycleEncryption(oneCycleEncryption(keyVector, x), keyVector);
        System.out.println("Result F2(x) vector:");
        printDoubleVector(resultVectorTwoEncryptionCycles);
        System.out.println();

        // y to double[]
        long y = this.y;
        double[] resultVector = new double[keyVector.length];
        for (int i = resultVector.length - 1; i > -1; --i) {
            resultVector[i] = y & 0x1;
            y >>= 1;
        }

        // xor
        for (int i = 0; i < resultVector.length; ++i) {
            resultVector[i] = GridOperations.gridXOR(resultVector[i], keyVector[i]);
        }
        double[] resultTwoDecryptionCycles = oneCycleDecryption(oneCycleDecryption(resultVector, keyVector), keyVector);
        System.out.println("Result F-2(y ^ k) vector:");
        printDoubleVector(resultTwoDecryptionCycles);
        System.out.println();

        double minimumValue = 1;
        double[] aimFunctionVector = new double[resultTwoDecryptionCycles.length];
        for (int i = 0; i < aimFunctionVector.length; ++i) {
            aimFunctionVector[i] = Math.round((1 - GridOperations.gridXOR(resultVectorTwoEncryptionCycles[i], resultTwoDecryptionCycles[i])) * 100) / 100.0d;
            minimumValue = Math.min(minimumValue, aimFunctionVector[i]);
        }
        System.out.println("Result 1 - (F2(x) ^ F-2(y ^ k)) vector:");
        printDoubleVector(aimFunctionVector);
        System.out.println();

        System.out.println("Minimum value: " + minimumValue);
        System.out.println("Removal: " + Math.round((BLOCK_LENGTH - 100 * minimumValue) * 100) / 100.0d);
        System.out.println();
    }

    public void run() {
        Random random = new Random();
        long x = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Random open text (x): " + x);
        System.out.println("Binary open text: " + toBinaryString(x));
        long key = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Random key (k): " + key);
        System.out.println("Binary key: " + toBinaryString(key));
        y = encrypt(x, key);
        System.out.println("Cipher text (y): " + y);
        System.out.println("Binary cipher text: " + toBinaryString(y));

        doOneIteration(x, key);
        for (int i = 0; i < 2; ++i) {
            generateInputKeyVector();
            doOneIteration(x, key);
        }
    }
}
