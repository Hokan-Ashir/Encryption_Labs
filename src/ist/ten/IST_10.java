package ist.ten;

import ist.common.AbstractEncryptionCase;

import java.util.Random;

public class IST_10 extends AbstractEncryptionCase {
    private Random random;
    private long gamma;
    private long key;

    private void createNewKeyAndGamma() {
        key = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Random key (key): " + key);
        System.out.println("Binary key: " + toBinaryString(key));
        gamma = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Random s (gamma) - pure, not encrypted: " + gamma);
        System.out.println("Binary s: " + toBinaryString(gamma));
    }

    public IST_10() {
        // get this IST_5 instance only for permutation creation
        random = new Random();
        createNewKeyAndGamma();
    }

    private long decryptOFB(long encryptedVector) {
        gamma = encrypt(gamma, key);
        return gamma ^ encryptedVector;
    }

    private long decryptCFB(long encryptedVector) {
        long result = encrypt(gamma, key) ^ encryptedVector;
        gamma = encryptedVector;
        return result;
    }

    private long decryptCBC(long encryptedVector) {
        long result = decrypt(encryptedVector, key);
        result ^= gamma;
        gamma = encryptedVector;
        return result;
    }

    private long encryptOFB(long inputVector) {
        gamma = encrypt(gamma, key);
        return gamma ^ inputVector;
    }

    private long encryptCFB(long inputVector) {
        gamma = encrypt(gamma, key) ^ inputVector;
        return gamma;
    }

    private long encryptCBC(long inputVector) {
        gamma = encrypt(gamma ^ inputVector, key);
        return gamma;
    }

    private int numberOfMismatchedBits(String numberString1, String numberString2) {
        int numberOfMismatchedBits = 0;
        for (int i = 0; i < numberString1.length(); ++i) {
            if (numberString1.charAt(i) != numberString2.charAt(i)) {
                numberOfMismatchedBits++;
            }
        }
        return numberOfMismatchedBits;
    }

    private void point5() {
        System.out.println();
        createNewKeyAndGamma();
        long IV = gamma;
        long[] inputVectorsArray = new long[3];
        for (int i = 0; i < inputVectorsArray.length; ++i) {
            inputVectorsArray[i] = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            System.out.println("x" + i + ": " + inputVectorsArray[i]);
            System.out.println("x" + i + " (binary): " + toBinaryString(inputVectorsArray[i]));
        }
        System.out.println();

        // OFB
        System.out.println("OFB:");
        long[] encryptedVectorsArray = new long[3];
        for (int i = 0; i < encryptedVectorsArray.length; ++i) {
            encryptedVectorsArray[i] = encryptOFB(inputVectorsArray[i]);
            System.out.println("y" + i + ": " + encryptedVectorsArray[i]);
            System.out.println("y" + i + " (binary): " + toBinaryString(encryptedVectorsArray[i]));
        }
        System.out.println();
        encryptedVectorsArray[0] ^= 4;

        gamma = IV;
        long[] decryptedVectorsArray = new long[3];
        for (int i = 0; i < decryptedVectorsArray.length; ++i) {
            decryptedVectorsArray[i] = decryptOFB(encryptedVectorsArray[i]);
            System.out.println("x'" + i + ": " + decryptedVectorsArray[i]);
            System.out.println("x'" + i + " (binary): " + toBinaryString(decryptedVectorsArray[i]));
            System.out.println("Number of mismatched bits: " +
                    numberOfMismatchedBits(
                            toBinaryString(decryptedVectorsArray[i]),
                            toBinaryString(inputVectorsArray[i])
                    ));
        }

        System.out.println();
        System.out.println("CFB:");
        //createNewKeyAndGamma();
        //IV = gamma;
        gamma = IV;
        // CFB
        for (int i = 0; i < encryptedVectorsArray.length; ++i) {
            encryptedVectorsArray[i] = encryptCFB(inputVectorsArray[i]);
            System.out.println("y" + i + ": " + encryptedVectorsArray[i]);
            System.out.println("y" + i + " (binary): " + toBinaryString(encryptedVectorsArray[i]));
        }
        System.out.println();
        encryptedVectorsArray[0] ^= 4;

        gamma = IV;
        for (int i = 0; i < decryptedVectorsArray.length; ++i) {
            decryptedVectorsArray[i] = decryptCFB(encryptedVectorsArray[i]);
            System.out.println("x'" + i + ": " + decryptedVectorsArray[i]);
            System.out.println("x'" + i + " (binary): " + toBinaryString(decryptedVectorsArray[i]));
            System.out.println("Number of mismatched bits: " +
                    numberOfMismatchedBits(
                            toBinaryString(decryptedVectorsArray[i]),
                            toBinaryString(inputVectorsArray[i])
                    ));
        }

        System.out.println();
        System.out.println("CBC:");
        //createNewKeyAndGamma();
        //IV = gamma;
        gamma = IV;
        // CBC
        for (int i = 0; i < encryptedVectorsArray.length; ++i) {
            encryptedVectorsArray[i] = encryptCBC(inputVectorsArray[i]);
            System.out.println("y" + i + ": " + encryptedVectorsArray[i]);
            System.out.println("y" + i + " (binary): " + toBinaryString(encryptedVectorsArray[i]));
        }
        System.out.println();
        encryptedVectorsArray[0] ^= 4;

        gamma = IV;
        for (int i = 0; i < decryptedVectorsArray.length; ++i) {
            decryptedVectorsArray[i] = decryptCBC(encryptedVectorsArray[i]);
            System.out.println("x'" + i + ": " + decryptedVectorsArray[i]);
            System.out.println("x'" + i + " (binary): " + toBinaryString(decryptedVectorsArray[i]));
            System.out.println("Number of mismatched bits: " +
                    numberOfMismatchedBits(
                            toBinaryString(decryptedVectorsArray[i]),
                            toBinaryString(inputVectorsArray[i])
                    ));
        }
    }

    private void point31() {
        System.out.println();
        createNewKeyAndGamma();
        long IV = gamma;
        long[] inputVectorsArray = new long[2];
        for (int i = 0; i < inputVectorsArray.length; ++i) {
            inputVectorsArray[i] = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            System.out.println("x" + i + ": " + inputVectorsArray[i]);
            System.out.println("x" + i + " (binary): " + toBinaryString(inputVectorsArray[i]));
        }
        System.out.println();

        // CFB
        System.out.println("CFB:");
        long[] encryptedVectorsArray = new long[2];
        long[] decryptedVectorsArray = new long[2];
        //createNewKeyAndGamma();
        //IV = gamma;
        gamma = IV;
        for (int i = 0; i < encryptedVectorsArray.length; ++i) {
            encryptedVectorsArray[i] = encryptCFB(inputVectorsArray[i]);
            System.out.println("y" + i + ": " + encryptedVectorsArray[i]);
            System.out.println("y" + i + " (binary): " + toBinaryString(encryptedVectorsArray[i]));
        }
        System.out.println();
        encryptedVectorsArray[1] ^= 2;

        gamma = IV;
        for (int i = 0; i < decryptedVectorsArray.length; ++i) {
            decryptedVectorsArray[i] = decryptCFB(encryptedVectorsArray[i]);
            System.out.println("x'" + i + ": " + decryptedVectorsArray[i]);
            System.out.println("x'" + i + " (binary): " + toBinaryString(decryptedVectorsArray[i]));
        }
        System.out.println(decryptedVectorsArray[1] ^ inputVectorsArray[1]);
    }

    private void point32() {
        System.out.println();
        createNewKeyAndGamma();
        long IV = gamma;
        long[] inputVectorsArray = new long[3];
        for (int i = 0; i < inputVectorsArray.length; ++i) {
            inputVectorsArray[i] = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            System.out.println("x" + i + ": " + inputVectorsArray[i]);
            System.out.println("x" + i + " (binary): " + toBinaryString(inputVectorsArray[i]));
        }
        System.out.println();

        // CFB
        System.out.println("CFB:");
        long[] encryptedVectorsArray = new long[3];
        long[] decryptedVectorsArray = new long[3];
        //createNewKeyAndGamma();
        //IV = gamma;
        gamma = IV;
        for (int i = 0; i < encryptedVectorsArray.length; ++i) {
            encryptedVectorsArray[i] = encryptCFB(inputVectorsArray[i]);
            System.out.println("y" + i + ": " + encryptedVectorsArray[i]);
            System.out.println("y" + i + " (binary): " + toBinaryString(encryptedVectorsArray[i]));
        }
        System.out.println();

        gamma = IV;
        System.out.println("Old gamma: " + gamma);
        gamma = encryptedVectorsArray[0];
        System.out.println("New gamma: " + gamma);
        encryptedVectorsArray[0] = encryptedVectorsArray[1];
        encryptedVectorsArray[1] = encryptedVectorsArray[2];
        for (int i = 0; i < decryptedVectorsArray.length; ++i) {
            decryptedVectorsArray[i] = decryptCFB(encryptedVectorsArray[i]);
            System.out.println("x'" + i + ": " + decryptedVectorsArray[i]);
            System.out.println("x'" + i + " (binary): " + toBinaryString(decryptedVectorsArray[i]));
        }
    }

    private void point33() {
        System.out.println();
        createNewKeyAndGamma();
        long IV = gamma;
        long[] inputVectorsArray = new long[3];
        for (int i = 0; i < inputVectorsArray.length; ++i) {
            inputVectorsArray[i] = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            System.out.println("x" + i + ": " + inputVectorsArray[i]);
            System.out.println("x" + i + " (binary): " + toBinaryString(inputVectorsArray[i]));
        }
        System.out.println();

        // CFB
        System.out.println("CFB:");
        long[] encryptedVectorsArray = new long[3];
        long[] decryptedVectorsArray = new long[3];
        //createNewKeyAndGamma();
        //IV = gamma;
        gamma = IV;
        for (int i = 0; i < encryptedVectorsArray.length; ++i) {
            encryptedVectorsArray[i] = encryptCFB(inputVectorsArray[i]);
            System.out.println("y" + i + ": " + encryptedVectorsArray[i]);
            System.out.println("y" + i + " (binary): " + toBinaryString(encryptedVectorsArray[i]));
        }
        System.out.println();

        encryptedVectorsArray[2] = 0;

        gamma = IV;
        for (int i = 0; i < decryptedVectorsArray.length; ++i) {
            decryptedVectorsArray[i] = decryptCFB(encryptedVectorsArray[i]);
            System.out.println("x'" + i + ": " + decryptedVectorsArray[i]);
            System.out.println("x'" + i + " (binary): " + toBinaryString(decryptedVectorsArray[i]));
        }
    }

    private void point41() {
        System.out.println();
        createNewKeyAndGamma();
        long IV = gamma;
        long[] inputVectorsArray = new long[2];
        for (int i = 0; i < inputVectorsArray.length; ++i) {
            inputVectorsArray[i] = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            System.out.println("x" + i + ": " + inputVectorsArray[i]);
            System.out.println("x" + i + " (binary): " + toBinaryString(inputVectorsArray[i]));
        }
        System.out.println();

        // CBC
        System.out.println("CBC:");
        long[] encryptedVectorsArray = new long[2];
        long[] decryptedVectorsArray = new long[2];
        //createNewKeyAndGamma();
        //IV = gamma;
        gamma = IV;
        for (int i = 0; i < encryptedVectorsArray.length; ++i) {
            encryptedVectorsArray[i] = encryptCBC(inputVectorsArray[i]);
            System.out.println("y" + i + ": " + encryptedVectorsArray[i]);
            System.out.println("y" + i + " (binary): " + toBinaryString(encryptedVectorsArray[i]));
        }
        System.out.println();

        gamma = IV;
        gamma ^= 2;
        for (int i = 0; i < decryptedVectorsArray.length; ++i) {
            decryptedVectorsArray[i] = decryptCBC(encryptedVectorsArray[i]);
            System.out.println("x'" + i + ": " + decryptedVectorsArray[i]);
            System.out.println("x'" + i + " (binary): " + toBinaryString(decryptedVectorsArray[i]));
        }
        System.out.println(decryptedVectorsArray[0] ^ inputVectorsArray[0]);
        System.out.println(decryptedVectorsArray[1] ^ inputVectorsArray[1]);
    }

    private void point42() {
        System.out.println();
        createNewKeyAndGamma();
        long IV = gamma;
        long[] inputVectorsArray = new long[3];
        for (int i = 0; i < inputVectorsArray.length; ++i) {
            inputVectorsArray[i] = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            System.out.println("x" + i + ": " + inputVectorsArray[i]);
            System.out.println("x" + i + " (binary): " + toBinaryString(inputVectorsArray[i]));
        }
        System.out.println();

        // CBC
        System.out.println("CBC:");
        long[] encryptedVectorsArray = new long[3];
        long[] decryptedVectorsArray = new long[3];
        //createNewKeyAndGamma();
        //IV = gamma;
        gamma = IV;
        for (int i = 0; i < encryptedVectorsArray.length; ++i) {
            encryptedVectorsArray[i] = encryptCBC(inputVectorsArray[i]);
            System.out.println("y" + i + ": " + encryptedVectorsArray[i]);
            System.out.println("y" + i + " (binary): " + toBinaryString(encryptedVectorsArray[i]));
        }
        System.out.println();

        gamma = IV;
        System.out.println("Old gamma: " + gamma);
        gamma = encryptedVectorsArray[0];
        System.out.println("New gamma: " + gamma);
        encryptedVectorsArray[0] = encryptedVectorsArray[1];
        encryptedVectorsArray[1] = encryptedVectorsArray[2];
        for (int i = 0; i < decryptedVectorsArray.length; ++i) {
            decryptedVectorsArray[i] = decryptCBC(encryptedVectorsArray[i]);
            System.out.println("x'" + i + ": " + decryptedVectorsArray[i]);
            System.out.println("x'" + i + " (binary): " + toBinaryString(decryptedVectorsArray[i]));
        }
    }

    private void point43() {
        System.out.println();
        createNewKeyAndGamma();
        long IV = gamma;
        long[] inputVectorsArray = new long[3];
        for (int i = 0; i < inputVectorsArray.length; ++i) {
            inputVectorsArray[i] = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            System.out.println("x" + i + ": " + inputVectorsArray[i]);
            System.out.println("x" + i + " (binary): " + toBinaryString(inputVectorsArray[i]));
        }
        System.out.println();

        // CBC
        System.out.println("CBC:");
        long[] encryptedVectorsArray = new long[3];
        long[] decryptedVectorsArray = new long[3];
        //createNewKeyAndGamma();
        //IV = gamma;
        gamma = IV;
        for (int i = 0; i < encryptedVectorsArray.length; ++i) {
            encryptedVectorsArray[i] = encryptCBC(inputVectorsArray[i]);
            System.out.println("y" + i + ": " + encryptedVectorsArray[i]);
            System.out.println("y" + i + " (binary): " + toBinaryString(encryptedVectorsArray[i]));
        }
        System.out.println();

        encryptedVectorsArray[2] = 0;

        gamma = IV;
        for (int i = 0; i < decryptedVectorsArray.length; ++i) {
            decryptedVectorsArray[i] = decryptCBC(encryptedVectorsArray[i]);
            System.out.println("x'" + i + ": " + decryptedVectorsArray[i]);
            System.out.println("x'" + i + " (binary): " + toBinaryString(decryptedVectorsArray[i]));
        }
    }

    private void point21() {
        System.out.println();
        createNewKeyAndGamma();
        long IV = gamma;
        long[] inputVectorsArray = new long[2];
        for (int i = 0; i < inputVectorsArray.length; ++i) {
            inputVectorsArray[i] = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            System.out.println("x" + i + ": " + inputVectorsArray[i]);
            System.out.println("x" + i + " (binary): " + toBinaryString(inputVectorsArray[i]));
        }
        System.out.println();

        // OFB
        System.out.println("OFB:");
        long[] encryptedVectorsArray = new long[2];
        long[] decryptedVectorsArray = new long[2];
        //createNewKeyAndGamma();
        //IV = gamma;
        gamma = IV;
        for (int i = 0; i < encryptedVectorsArray.length; ++i) {
            encryptedVectorsArray[i] = encryptOFB(inputVectorsArray[i]);
            System.out.println("y" + i + ": " + encryptedVectorsArray[i]);
            System.out.println("y" + i + " (binary): " + toBinaryString(encryptedVectorsArray[i]));
        }
        System.out.println();

        encryptedVectorsArray[1] ^= 2;

        gamma = IV;
        for (int i = 0; i < decryptedVectorsArray.length; ++i) {
            decryptedVectorsArray[i] = decryptOFB(encryptedVectorsArray[i]);
            System.out.println("x'" + i + ": " + decryptedVectorsArray[i]);
            System.out.println("x'" + i + " (binary): " + toBinaryString(decryptedVectorsArray[i]));
        }
        System.out.println(decryptedVectorsArray[0] ^ inputVectorsArray[0]);
        System.out.println(decryptedVectorsArray[1] ^ inputVectorsArray[1]);
    }

    private void point22() {
        System.out.println();
        createNewKeyAndGamma();

        // ТОРТ
        long cake = Long.valueOf("00000001000000100000010000000001", 2);
        System.out.println("cake: " + cake);
        System.out.println("cake (binary): " + toBinaryString(cake));
        // ТРОС
        long rope = Long.valueOf("00000001000001000000001010000000", 2);
        System.out.println("rope: " + rope);
        System.out.println("rope (binary): " + toBinaryString(rope));

        long IV = gamma;
        long encryptedCake = encryptOFB(cake);
        System.out.println("encrypted cake: " + encryptedCake);
        System.out.println("encrypted cake (binary): " + toBinaryString(encryptedCake));

        gamma = IV;
        long encryptedRope = encryptOFB(rope);
        System.out.println("encrypted rope: " + encryptedRope);
        System.out.println("encrypted rope (binary): " + toBinaryString(encryptedRope));

        System.out.println("XOR difference: " + (encryptedCake ^ encryptedRope));
        System.out.println("XOR difference (binary): " + toBinaryString(encryptedCake ^ encryptedRope));
    }

    public void run() {
        point21();
        //point22();
        //point31();
        //point32();
        //point33();
        //point41();
        //point42();
        //point43();
        //point5();
    }
}
