package ist.five;

import ist.Pair;
import ist.one.IST_1;
import ist.three.IST_3;

import java.math.BigInteger;
import java.util.*;

public class IST_5 {
    private static int[] substitution = {6, 5, 3, 11, 14, 10, 8, 1, 12, 2, 15, 4, 0, 13, 9, 7};//{0, 13, 11, 8, 3, 6, 4, 1, 15, 2, 5, 14, 10, 12, 9, 7};
    private static int[] reverseSubstitution = new int[(int) Math.pow(2.0d, IST_1.BIT_LENGTH)];
    private int[] reversePermutation = new int[IST_3.BLOCK_LENGTH + 1];
    private static int[] permutation = new int[IST_3.BLOCK_LENGTH + 1];

    public IST_5() {
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
        return vector ^ key;
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
        System.out.println("After XOR key: " + result);

        //System.out.println("After XOR-key: " + result);
        long temp = 0;
        for (int j = 0; j < IST_3.BLOCK_LENGTH / IST_1.BIT_LENGTH; ++j) {
            temp |= ((long) substitution[(int) result & 0xF] << (j * IST_1.BIT_LENGTH));
            result >>= IST_1.BIT_LENGTH;
        }
        System.out.println("Before permutation: " + temp);
        temp = permutation(temp);
        System.out.println("After  permutation: " + temp);
        System.out.println("----");
        //System.out.println("Before permutation: " + temp);
        return temp;
    }

    public static long decrypt(long encryptedVector, long  key) {
        encryptedVector ^= key;
        for (int i = 0; i < IST_3.NUMBER_OF_ROUNDS; i++) {
            encryptedVector = decryptOneCycle(encryptedVector, key);
        }
        return encryptedVector;
    }

    private static long decryptOneCycle(long encryptedVector, long key) {
        long result = reversedPermutation(encryptedVector);
        //System.out.println("After reversed permutation: " + key);
        long temp = 0;
        for (int j = 0; j < IST_3.BLOCK_LENGTH / IST_1.BIT_LENGTH; ++j) {
            temp |= ((long) reverseSubstitution[(int) result & 0xF] << (j * IST_1.BIT_LENGTH));
            result >>= IST_1.BIT_LENGTH;
        }

        return temp ^ key;
    }

    private long decryptOneCycleKey(long encryptedVector, long openVector) {
        long key;
        key = reversedPermutation(encryptedVector);
        //System.out.println("After reversed permutation: " + key);
        long temp = 0;
        for (int j = 0; j < IST_3.BLOCK_LENGTH / IST_1.BIT_LENGTH; ++j) {
            temp |= ((long) reverseSubstitution[(int) key & 0xF] << (j * IST_1.BIT_LENGTH));
            key >>= IST_1.BIT_LENGTH;
        }
        key = temp;
        //System.out.println("Before XOR-key: " + key);
        return key ^ openVector;
    }

    private long revSP(long v) {
        long temp = 0;
        v = reversedPermutation(v);
        for (int j = 0; j < IST_3.BLOCK_LENGTH / IST_1.BIT_LENGTH; ++j) {
            temp |= ((long) reverseSubstitution[(int) v & 0xF] << (j * IST_1.BIT_LENGTH));
            v >>= IST_1.BIT_LENGTH;
        }
        return temp;
    }

    private boolean bruteSlide(long key) {
        Random random = new Random();
        long openText, cipherText, rev;
        //Set<Pair<Long, Long>> pairSet = new HashSet<Pair<Long, Long>>();
        // do not using Set & assuming that all generated pairs are unique
        List<Pair<Long, Long>> pairList = new ArrayList<Pair<Long, Long>>();
        while (true) {
            if (pairList.size() == Math.pow(2.0d, IST_3.BLOCK_LENGTH / 2)) {
                break;
            } else {
                openText = (long) (random.nextDouble() * (Integer.MAX_VALUE));
                cipherText = encryptWithoutKey(openText, key);
                pairList.add(new Pair<Long, Long>(openText, cipherText));
            }
        }
        System.out.println("Set created");
        long beginTime = System.nanoTime();
        long possibleKey;
        boolean firstMatchDone = false;
        List<Pair<Long, Pair<Pair<Long, Long>, Pair<Long, Long>>>> pairList2 = new LinkedList<Pair<Long, Pair<Pair<Long, Long>, Pair<Long, Long>>>>();
        for (int i = 0; i < pairList.size(); ++i) {
            for (int j = 0; j < pairList.size(); ++j) {
                if (i == j) {
                    continue;
                }
                Pair<Long, Long> pair1 = pairList.get(i);
                Pair<Long, Long> pair2 = pairList.get(j);
                possibleKey = decryptOneCycleKey(pair1.getFirst(), pair2.getFirst());
                if (possibleKey == decryptOneCycleKey(pair1.getSecond(), pair2.getSecond())) {
                    System.out.println("Key found: " + possibleKey);
                    System.out.println("Pair found:");
                    System.out.println("\tOpen text:" + pair1.getFirst());
                    System.out.println("\tCipher text:" + pair1.getSecond());
                    System.out.println("Slide-pair found:");
                    System.out.println("\tOpen text:" + pair2.getFirst());
                    System.out.println("\tCipher text:" + pair2.getSecond());
                    pairList2.add(
                            new Pair<Long, Pair<Pair<Long, Long>, Pair<Long, Long>>>(
                                    possibleKey,
                                    new Pair<Pair<Long, Long>, Pair<Long, Long>>(
                                            new Pair<Long, Long>(pair1.getFirst(), pair1.getSecond()),
                                            new Pair<Long, Long>(pair2.getFirst(), pair2.getSecond())
                                    )
                            )
                    );
                    if (!firstMatchDone) {
                        System.out.println("Time required to get first match (sec.): " + (System.nanoTime() - beginTime) / 1000000000.0);
                        firstMatchDone = true;
                    }
                    return true;
                }
            }
        }
        System.out.println("Total search time (sec.): " + (System.nanoTime() - beginTime) / 1000000000.0);

        if (pairList2.isEmpty()) {
            System.out.println("Method didn't find any slide-pairs. So sad =(");
            return false;
        }

        for (int i = 0; i < pairList2.size(); ++i) {
            System.out.println("Index: " + i);
            System.out.println("Key found: " + pairList2.get(i).getFirst());
            System.out.println("Pair found:");
            System.out.println("\tOpen text:" + pairList2.get(i).getSecond().getFirst().getFirst());
            System.out.println("\tCipher text:" + pairList2.get(i).getSecond().getFirst().getSecond());
            System.out.println("Slide-pair found:");
            System.out.println("\tOpen text:" + pairList2.get(i).getSecond().getSecond().getFirst());
            System.out.println("\tCipher text:" + pairList2.get(i).getSecond().getSecond().getSecond());
        }
        return true;
    }

    private void bruteForce(long openText, long cypherText) {
        long beginTime = System.nanoTime();
        for (long i = 0; i < Math.pow(2.0d, IST_3.BLOCK_LENGTH); i++) {
            /*if (encrypt(openText, i) == cypherText) {
                System.out.println("key is: " + i);
                System.out.println("Total search time (sec.): " + (System.nanoTime() - beginTime) / 1000000000.0);
                break;
            }*/
            if (encrypt(openText, i) == cypherText) {
                System.out.println("key is: " + i);
            }
            if (i != 0 && (i % Math.pow(10.0d, 6)) == 0) {
                System.out.println("Total search time (sec.): " + (System.nanoTime() - beginTime) / 1000000000.0);
                break;
            }
            /*if ((i % Math.pow(10.0d, 6)) == 0) {
                System.out.print(".");
            }*/
        }
    }

    public boolean advancedSlide(long key) {
        Map<Long, Pair<Long, Long>> map = new HashMap<Long, Pair<Long, Long>>();
        Random random = new Random();
        long openText, cipherText, revXORDifference, possibleKey;
        boolean keyFound = false;
        boolean firstMatchDone = false;
        long beginTime = System.nanoTime();
        List<Pair<Long, Pair<Pair<Long, Long>, Pair<Long, Long>>>> pairList = new LinkedList<Pair<Long, Pair<Pair<Long, Long>, Pair<Long, Long>>>>();
        for (long i = 0; i < Math.pow(2.0d, IST_3.BLOCK_LENGTH / 2); ++i) {
            openText = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            cipherText = encryptWithoutKey(openText, key);
            revXORDifference = revSP(openText) ^ revSP(cipherText);
            for (long j = 0; j < map.size(); ++j) {
                if (map.containsKey(revXORDifference)) {
                    possibleKey = decryptOneCycleKey(cipherText, map.get(revXORDifference).getSecond());
                    if (possibleKey == decryptOneCycleKey(openText, map.get(revXORDifference).getFirst())) {
                        /*System.out.println("Key found: " + possibleKey);
                        System.out.println("Pair found:");
                        System.out.println("\tOpen text:" + map.get(revXORDifference).getFirst());
                        System.out.println("\tCipher text:" + map.get(revXORDifference).getSecond());
                        System.out.println("Slide-pair found:");
                        System.out.println("\tOpen text:" + openText);
                        System.out.println("\tCipher text:" + cipherText);*/
                        keyFound = true;
                        pairList.add(
                                new Pair<Long, Pair<Pair<Long, Long>, Pair<Long, Long>>>(
                                        possibleKey,
                                        new Pair<Pair<Long, Long>, Pair<Long, Long>>(
                                                new Pair<Long, Long>(map.get(revXORDifference).getFirst(), map.get(revXORDifference).getSecond()),
                                                new Pair<Long, Long>(openText, cipherText)
                                        )
                                )
                        );
                        if (!firstMatchDone) {
                            System.out.println("Time required to get first match (sec.): " + (System.nanoTime() - beginTime) / 1000000000.0);
                            firstMatchDone = true;
                        }
                        break;
                        //return;
                    }
                }
            }
            if (!keyFound) {
                map.put(openText ^ cipherText, new Pair<Long, Long>(openText, cipherText));
                keyFound = false;
            }
        }
        System.out.println("Total search time (sec.): " + (System.nanoTime() - beginTime) / 1000000000.0);

        if (pairList.isEmpty()) {
            System.out.println("Method didn't find any slide-pairs. So sad =(");
            return false;
        }

        for (int i = 0; i < pairList.size(); ++i) {
            System.out.println("Index: " + i);
            System.out.println("Key found: " + pairList.get(i).getFirst());
            System.out.println("Pair found:");
            System.out.println("\tOpen text:" + pairList.get(i).getSecond().getFirst().getFirst());
            System.out.println("\tCipher text:" + pairList.get(i).getSecond().getFirst().getSecond());
            System.out.println("Slide-pair found:");
            System.out.println("\tOpen text:" + pairList.get(i).getSecond().getSecond().getFirst());
            System.out.println("\tCipher text:" + pairList.get(i).getSecond().getSecond().getSecond());
        }
        return true;
    }

    public void run() {
        Random random = new Random();
        long x = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Random open text (x): " + x);
        long key = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Random key (k): " + key);
        long x_ = encryptOneCycle(x, key);
        System.out.println("Encrypted one cycle open text (x'): " + x_);
        long decryptedKey = decryptOneCycleKey(x_, x);
        System.out.println("Decrypted key via x and x' (k'): " + decryptedKey);
        long y = encrypt(x, key);
        System.out.println("Encrypted all cycles open text (y): " + y);
        long yk = y ^ decryptedKey;
        System.out.println("(y^k): " + yk);
        long y_ = encrypt(x_, key);
        System.out.println("Encrypted all cycles encrypted one cycle open text (y'): " + y_);
        long yk_ = y_ ^ decryptedKey;
        System.out.println("(y' ^ k): " + yk_);
        long decryptedKey2 = decryptOneCycleKey(yk_, yk);
        System.out.println("Decrypted key via y and y' (k''): " + decryptedKey2);
        System.out.println("Are all keys equal? " + (decryptedKey2 == decryptedKey && decryptedKey == key));
        y = encrypt(x, key);
        if (advancedSlide(key)) {
        bruteSlide(key);
        bruteForce(x, y);
        }
    }
}
