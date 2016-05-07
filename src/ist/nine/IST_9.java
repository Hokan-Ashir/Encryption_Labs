package ist.nine;

import ist.Pair;
import ist.five.IST_5;
import ist.one.IST_1;
import ist.three.IST_3;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class IST_9 {
    private long[] tempVector = new long[IST_3.BLOCK_LENGTH];

    private long[] permutation = {
            5, 10, 15, 20, 25, 30, 3, 8,
            13, 18, 23, 28, 1, 6, 11, 16,
            21, 26, 31, 4, 9, 14, 19, 24,
            29, 2, 7, 12, 17, 22, 27, 32
    };

    private long[] reversedPermutation = {
            13, 26, 7, 20, 1, 14, 27, 8,
            21, 2, 15, 28, 9, 22, 3, 16,
            29, 10, 23, 4, 17, 30, 11, 24,
            5, 18, 31, 12, 25, 6, 19, 32
    };

    private long v1(long u1, long u2, long u3, long u4, int m) {
        return TwoAdicOperations.twoAdicXOR(new long[]{
                TwoAdicOperations.twoAdicAND(u3, u4, m),
                u2,
                u1,
                TwoAdicOperations.twoAdicAND(u1, u4, m),
                TwoAdicOperations.twoAdicAND(new long[]{u1, u3, u4}, m),
                TwoAdicOperations.twoAdicAND(new long[]{u1, u2, u3}, m)
        }, m);
    }

    private long v2(long u1, long u2, long u3, long u4, int m) {
        return TwoAdicOperations.twoAdicXOR(new long[]{
                1,
                u3,
                TwoAdicOperations.twoAdicAND(u2, u4, m),
                TwoAdicOperations.twoAdicAND(new long[]{u2, u3, u4}, m),
                TwoAdicOperations.twoAdicAND(u1, u4, m),
                TwoAdicOperations.twoAdicAND(u1, u3, m),
                TwoAdicOperations.twoAdicAND(new long[]{u1, u3, u4}, m),
                TwoAdicOperations.twoAdicAND(u1, u2, m),
                TwoAdicOperations.twoAdicAND(new long[]{u1, u2, u4}, m)
        }, m);
    }

    private long v3(long u1, long u2, long u3, long u4, int m) {
        return TwoAdicOperations.twoAdicXOR(new long[]{
                1,
                u4,
                TwoAdicOperations.twoAdicAND(u3, u4, m),
                TwoAdicOperations.twoAdicAND(u2, u4, m),
                TwoAdicOperations.twoAdicAND(u2, u3, m),
                TwoAdicOperations.twoAdicAND(new long[]{u2, u3, u4}, m),
                u1,
                TwoAdicOperations.twoAdicAND(u1, u3, m),
                TwoAdicOperations.twoAdicAND(new long[]{u1, u3, u4}, m)
        }, m);
    }

    private long v4(long u1, long u2, long u3, long u4, int m) {
        return TwoAdicOperations.twoAdicXOR(new long[]{
                u4,
                TwoAdicOperations.twoAdicAND(u2, u4, m),
                TwoAdicOperations.twoAdicAND(new long[]{u2, u3, u4}, m),
                TwoAdicOperations.twoAdicAND(u1, u4, m),
                TwoAdicOperations.twoAdicAND(new long[]{u1, u2, u3}, m)
        }, m);
    }

    private long u1(long v1, long v2, long v3, long v4, int m) {
        return TwoAdicOperations.twoAdicXOR(new long[]{
                1,
                v4,
                v2,
                TwoAdicOperations.twoAdicAND(v2, v4, m),
                TwoAdicOperations.twoAdicAND(new long[]{v2, v3, v4}, m),
                TwoAdicOperations.twoAdicAND(v1, v4, m),
                TwoAdicOperations.twoAdicAND(v1, v3, m),
                TwoAdicOperations.twoAdicAND(v1, v2, m),
                TwoAdicOperations.twoAdicAND(new long[]{v1, v2, v4}, m)
        }, m);
    }

    private long u2(long v1, long v2, long v3, long v4, int m) {
        return TwoAdicOperations.twoAdicXOR(new long[]{
                1,
                v3,
                v2,
                TwoAdicOperations.twoAdicAND(v2, v3, m),
                TwoAdicOperations.twoAdicAND(new long[]{v2, v3, v4}, m),
                TwoAdicOperations.twoAdicAND(v1, v3, m),
                TwoAdicOperations.twoAdicAND(new long[]{v1, v3, v4}, m),
                TwoAdicOperations.twoAdicAND(new long[]{v1, v2, v4}, m)

        }, m);
    }

    private long u3(long v1, long v2, long v3, long v4, int m) {
        return TwoAdicOperations.twoAdicXOR(new long[]{
                v4,
                v2,
                TwoAdicOperations.twoAdicAND(v2, v3, m),
                v1,
                TwoAdicOperations.twoAdicAND(v1, v4, m),
                TwoAdicOperations.twoAdicAND(v1, v3, m),
                TwoAdicOperations.twoAdicAND(new long[]{v1, v3, v4}, m)
        }, m);
    }

    private long u4(long v1, long v2, long v3, long v4, int m) {
        return TwoAdicOperations.twoAdicXOR(new long[]{
                v4,
                v3,
                v2,
                TwoAdicOperations.twoAdicAND(v2, v4, m),
                TwoAdicOperations.twoAdicAND(new long[]{v2, v3, v4}, m),
                TwoAdicOperations.twoAdicAND(v1, v4, m),
                TwoAdicOperations.twoAdicAND(v1, v2, m),
                TwoAdicOperations.twoAdicAND(new long[]{v1, v2, v3}, m)
        }, m);
    }

    private long[] oneCycleDecryption(long[] xVector, long[] key, int m) {
        // reversed permutation
        long[] resultVector = new long[xVector.length];
        for (int i = 0; i < xVector.length; ++i) {
            resultVector[i] = xVector[(int)reversedPermutation[i] - 1];
        }

        // reversed substitution
        for (int i = 0; i < resultVector.length; i += IST_1.BIT_LENGTH) {
            long v1 = resultVector[i];
            long v2 = resultVector[i + 1];
            long v3 = resultVector[i + 2];
            long v4 = resultVector[i + 3];
            resultVector[i] = u1(v1, v2, v3, v4, m);
            resultVector[i + 1] = u2(v1, v2, v3, v4, m);
            resultVector[i + 2] = u3(v1, v2, v3, v4, m);
            resultVector[i + 3] = u4(v1, v2, v3, v4, m);

            // using
            /*int vector = (xVector[i] << 3) + (xVector[i + 1] << 2) + (xVector[i + 2] << 1) + xVector[i + 3];
            vector = reversedSubstitution[vector];
            xVector[i] = (vector & 0x8) >> 3;
            xVector[i + 1] = (vector & 0x4) >> 2;
            xVector[i + 2] = (vector & 0x2) >> 1;
            xVector[i + 3] = vector & 0x1;*/
        }

        // xor
        for (int i = 0; i < resultVector.length; ++i) {
            resultVector[i] = TwoAdicOperations.twoAdicXOR(resultVector[i], key[i], m);
        }

        return resultVector;
    }

    private long[] oneCycleEncryption(long[] xVector, long[] key, int m) {
        for (int i = 0; i < xVector.length; ++i) {
            tempVector[i] = xVector[i];
        }
        long[] inputVector = tempVector;//xVector.clone();
        // xor
        for (int i = 0; i < xVector.length; ++i) {
            inputVector[i] = TwoAdicOperations.twoAdicXOR(inputVector[i], key[i], m);
        }

        // substitution
        for (int i = 0; i < inputVector.length; i += IST_1.BIT_LENGTH) {
            long u1 = inputVector[i];
            long u2 = inputVector[i + 1];
            long u3 = inputVector[i + 2];
            long u4 = inputVector[i + 3];
            inputVector[i] = v1(u1, u2, u3, u4, m);
            inputVector[i + 1] = v2(u1, u2, u3, u4, m);
            inputVector[i + 2] = v3(u1, u2, u3, u4, m);
            inputVector[i + 3] = v4(u1, u2, u3, u4, m);

            // using
            /*int vector = (xVector[i] << 3) + (xVector[i + 1] << 2) + (xVector[i + 2] << 1) + xVector[i + 3];
            vector = substitution[vector];
            xVector[i] = (vector & 0x8) >> 3;
            xVector[i + 1] = (vector & 0x4) >> 2;
            xVector[i + 2] = (vector & 0x2) >> 1;
            xVector[i + 3] = vector & 0x1;*/
        }

        // permutation
        long[] resultVector = new long[inputVector.length];
        for (int i = 0; i < inputVector.length; ++i) {
            resultVector[i] = inputVector[(int)permutation[i] - 1];
        }

        return resultVector;
    }

    private long[] fullEncryption(long[] xVector, long[] key, int m) {
        long[] resultVector = oneCycleEncryption(xVector, key, m);
        for (int i = 1; i < IST_3.NUMBER_OF_ROUNDS; ++i) {
            resultVector = oneCycleEncryption(resultVector, key, m);
        }

        // xor
        for (int i = 0; i < resultVector.length; ++i) {
            resultVector[i] = TwoAdicOperations.twoAdicXOR(resultVector[i], key[i], m);
        }

        return resultVector;
    }

    private int H(long[] inputVector, long[] outputVector, long[] key, int m) {
        long[] twoCyclesEncryption = oneCycleEncryption(oneCycleEncryption(inputVector, key, m), key, m);

        for (int i = 0; i < outputVector.length; ++i) {
            tempVector[i] = outputVector[i];
        }
        long[] outVector = tempVector;//outputVector.clone();
        // xor
        for (int i = 0; i < outputVector.length; ++i) {
            outVector[i] = TwoAdicOperations.twoAdicXOR(outVector[i], key[i], m);
        }

        long[] twoCyclesDecryption = oneCycleDecryption(oneCycleDecryption(outVector, key, m), key, m);
        long aimFunctionValuePreVal = 1;
        for (int i = 0; i < twoCyclesDecryption.length; ++i) {
            aimFunctionValuePreVal = TwoAdicOperations.twoAdicAND(
                    aimFunctionValuePreVal,
                    TwoAdicOperations.twoAdicXOR(new long[]{
                            1,
                            twoCyclesEncryption[i],
                            twoCyclesDecryption[i]
                    }, m),
                    m);
        }

        // val
        long[] aimFunctionArray = LongUtils.longToLongArray(aimFunctionValuePreVal, IST_3.BLOCK_LENGTH * 2);
        int valIndex = 0;
        for (int i = aimFunctionArray.length - 1; i > -1; --i) {
            if (aimFunctionArray[i] == 1) {
                valIndex = i;
                break;
            }
        }

        return -(aimFunctionArray.length - 1 - valIndex);
    }

    private long[] createRandomVector() {
        long[] resultVector = new long[IST_3.BLOCK_LENGTH];
        Random random = new Random();
        for (int i = 0; i < resultVector.length; ++i) {
            resultVector[i] = Math.abs(random.nextInt()) % 3;
        }
        return resultVector;
    }

    private Pair<IntegerPairWrapper[], Integer> digitRating(long[] inVector, int m) {
        for (int i = 0; i < inVector.length; ++i) {
            tempVector[i] = inVector[i];
        }
        long[] inputVector = tempVector;//inVector.clone();
        Pair<IntegerPairWrapper[], Integer> resultVector = new Pair(new IntegerPairWrapper[IST_3.BLOCK_LENGTH], new Integer(0));
        for (int i = 0; i < IST_3.BLOCK_LENGTH; ++i) {
            resultVector.getFirst()[i] = new IntegerPairWrapper(0, 0);
        }
        // step 1
        //int j = 0;
        long[] key = createRandomVector();
        long[] outputVector = fullEncryption(inputVector, key, m);
        int h = H(inputVector, outputVector, key, m);
        //int iteration = 1;
        while (true) {
            // step 2
            for (int i = 0; i < IST_3.BLOCK_LENGTH; ++i) {
                if (key[i] == 2) {
                    // 2.1
                    key[i] = 0;
                    outputVector = fullEncryption(inputVector, key, m);
                    int h0 = H(inputVector, outputVector, key, m);
                    key[i] = 1;
                    outputVector = fullEncryption(inputVector, key, m);
                    int h1 = H(inputVector, outputVector, key, m);
                    key[i] = 2;
                    if (h0 < h && h < h1) {
                        resultVector.getFirst()[i].setFirst(h1);
                        resultVector.getFirst()[i].setSecond(1);
                    } else if (h1 < h && h < h0) {
                        resultVector.getFirst()[i].setFirst(h0);
                        resultVector.getFirst()[i].setSecond(0);
                    } else {
                        resultVector.getFirst()[i].setFirst(h);
                        resultVector.getFirst()[i].setSecond(2);
                    }
                } else if (key[i] == 0) {
                    // 2.2
                    key[i] = 2;
                    outputVector = fullEncryption(inputVector, key, m);
                    int h2 = H(inputVector, outputVector, key, m);
                    key[i] = 1;
                    outputVector = fullEncryption(inputVector, key, m);
                    int h1 = H(inputVector, outputVector, key, m);
                    key[i] = 0;
                    if (h2 < h && h < h1) {
                        resultVector.getFirst()[i].setFirst(h1);
                        resultVector.getFirst()[i].setSecond(1);
                    } else if (h1 < h && h < h2) {
                        resultVector.getFirst()[i].setFirst(h2);
                        resultVector.getFirst()[i].setSecond(2);
                    } else {
                        resultVector.getFirst()[i].setFirst(h);
                        resultVector.getFirst()[i].setSecond(0);
                    }
                } else if (key[i] == 1) {
                    // 2.3
                    key[i] = 0;
                    outputVector = fullEncryption(inputVector, key, m);
                    int h0 = H(inputVector, outputVector, key, m);
                    key[i] = 2;
                    outputVector = fullEncryption(inputVector, key, m);
                    int h2 = H(inputVector, outputVector, key, m);
                    key[i] = 1;
                    if (h0 < h && h < h2) {
                        resultVector.getFirst()[i].setFirst(h2);
                        resultVector.getFirst()[i].setSecond(2);
                    } else if (h2 < h && h < h0) {
                        resultVector.getFirst()[i].setFirst(h0);
                        resultVector.getFirst()[i].setSecond(0);
                    } else {
                        resultVector.getFirst()[i].setFirst(h);
                        resultVector.getFirst()[i].setSecond(1);
                    }
                }
            }

            /*System.out.println("Iteration: " + iteration);
            for (IntegerPairWrapper integerPairWrapper : resultVector.getFirst()) {
                System.out.println("H: " + integerPairWrapper.getFirst() + " digit: " + integerPairWrapper.getSecond());
            }*/

            // step 3
            int hMax = resultVector.getFirst()[0].getFirst();
            int index = 0;
            int currentHMax;
            for (int i = 1; i < IST_3.BLOCK_LENGTH; ++i) {
                currentHMax = hMax;
                hMax = Math.max(hMax, resultVector.getFirst()[i].getFirst());
                if (currentHMax != hMax) {
                    index = i;
                }
            }

            if (hMax > h) {
                key[index] = resultVector.getFirst()[index].getSecond();
                h = hMax;
                //j = resultVector.getFirst()[index].getSecond();
            } else if (hMax == h) {
                break;
            }
            //iteration++;
        }
        resultVector.setSecond(h);

        return resultVector;
    }

    private Statistics statistics(long[] inputVector, long[] realKey, int numberOfTestKeys, int m) {
        // step 1
        // setting up N-Matrix
        Statistics statistics = new Statistics(IST_3.BLOCK_LENGTH);
        for (int i = 0; i < numberOfTestKeys; ++i) {
            Pair<IntegerPairWrapper[], Integer> digitRating = digitRating(inputVector, m);
            for (int j = 0; j < IST_3.BLOCK_LENGTH; ++j) {
                if (digitRating.getFirst()[j].getSecond() == 0 && realKey[j] == 0) {
                    statistics.nMatrixStatistics[j].n00++;
                } else if (digitRating.getFirst()[j].getSecond() == 0 && realKey[j] == 1) {
                    statistics.nMatrixStatistics[j].n01++;
                } else if (digitRating.getFirst()[j].getSecond() == 1 && realKey[j] == 0) {
                    statistics.nMatrixStatistics[j].n10++;
                } else if (digitRating.getFirst()[j].getSecond() == 1 && realKey[j] == 1) {
                    statistics.nMatrixStatistics[j].n11++;
                }
            }
        }

        // setting up P-Matrix & Q-Matrix
        for (int i = 0; i < IST_3.BLOCK_LENGTH; ++i) {
            // 00
            if ((statistics.nMatrixStatistics[i].n00 + statistics.nMatrixStatistics[i].n10) != 0) {
                statistics.pMatrixStatistics[i].p00 =
                        (double)statistics.nMatrixStatistics[i].n00
                                / (statistics.nMatrixStatistics[i].n00 + statistics.nMatrixStatistics[i].n10);
            }
            if ((statistics.nMatrixStatistics[i].n00 + statistics.nMatrixStatistics[i].n01) != 0) {
                statistics.qMatrixStatistics[i].p00 =
                        (double)statistics.nMatrixStatistics[i].n00
                                / (statistics.nMatrixStatistics[i].n00 + statistics.nMatrixStatistics[i].n01);
            }

            // 01
            if ((statistics.nMatrixStatistics[i].n01 + statistics.nMatrixStatistics[i].n11) != 0) {
                statistics.pMatrixStatistics[i].p01 =
                        (double)statistics.nMatrixStatistics[i].n01
                                / (statistics.nMatrixStatistics[i].n01 + statistics.nMatrixStatistics[i].n11);
            }
            if ((statistics.nMatrixStatistics[i].n10 + statistics.nMatrixStatistics[i].n11) != 0) {
                statistics.qMatrixStatistics[i].p01 =
                        (double)statistics.nMatrixStatistics[i].n10
                                / (statistics.nMatrixStatistics[i].n10 + statistics.nMatrixStatistics[i].n11);
            }

            // 10
            if ((statistics.nMatrixStatistics[i].n00 + statistics.nMatrixStatistics[i].n10) != 0) {
                statistics.pMatrixStatistics[i].p10 =
                        (double)statistics.nMatrixStatistics[i].n10
                                / (statistics.nMatrixStatistics[i].n00 + statistics.nMatrixStatistics[i].n10);
            }
            if ((statistics.nMatrixStatistics[i].n00 + statistics.nMatrixStatistics[i].n01) != 0) {
                statistics.qMatrixStatistics[i].p10 =
                        (double)statistics.nMatrixStatistics[i].n01
                                / (statistics.nMatrixStatistics[i].n00 + statistics.nMatrixStatistics[i].n01);
            }

            // 11
            if ((statistics.nMatrixStatistics[i].n11 + statistics.nMatrixStatistics[i].n01) != 0) {
                statistics.pMatrixStatistics[i].p11 =
                        (double)statistics.nMatrixStatistics[i].n11
                                / (statistics.nMatrixStatistics[i].n11 + statistics.nMatrixStatistics[i].n01);
            }
            if ((statistics.nMatrixStatistics[i].n10 + statistics.nMatrixStatistics[i].n11) != 0) {
                statistics.qMatrixStatistics[i].p11 =
                        (double)statistics.nMatrixStatistics[i].n11
                                / (statistics.nMatrixStatistics[i].n10 + statistics.nMatrixStatistics[i].n11);
            }
        }
        return statistics;
    }

    private DoublePairWrapper[] testKeysStatistics(long[] inputVector, Statistics statistics, int numberOfTestKeys, int m) {
        double p0 = 0.0d;
        double p1 = 0.0d;
        for (int i = 0; i < numberOfTestKeys; ++i) {
            Pair<IntegerPairWrapper[], Integer> digitRating = digitRating(inputVector, m);
            for (int j = 0; j < IST_3.BLOCK_LENGTH; ++j) {
                if (digitRating.getFirst()[j].getSecond() == 0) {
                    p0++;
                } else if (digitRating.getFirst()[j].getSecond() == 1) {
                    p1++;
                }
            }
        }
        p0 /= numberOfTestKeys;
        p1 /= numberOfTestKeys;

        DoublePairWrapper[] piStatistics = new DoublePairWrapper[IST_3.BLOCK_LENGTH];
        for (int i = 0; i < piStatistics.length; ++i) {
            piStatistics[i] = new DoublePairWrapper(0.0d, 0.0d);
            piStatistics[i].setFirst(
                    (statistics.pMatrixStatistics[i].p11 * p0 - statistics.pMatrixStatistics[i].p01 * p1)
                    / (statistics.pMatrixStatistics[i].p11 * statistics.pMatrixStatistics[i].p00 - statistics.pMatrixStatistics[i].p01 * statistics.pMatrixStatistics[i].p10)
            );
            piStatistics[i].setSecond(
                    (p1 - statistics.pMatrixStatistics[i].p10 * piStatistics[i].getFirst()) / statistics.pMatrixStatistics[i].p11
            );
        }

        return piStatistics;
    }

    private Statistics mergeStatistics(Collection<Statistics> statisticsCollection) {
        Statistics resultStatistics = new Statistics(statisticsCollection.iterator().next().nMatrixStatistics.length);
        for (Statistics statistics : statisticsCollection) {
            for (int i = 0; i < resultStatistics.nMatrixStatistics.length; ++i) {
                // NMatrix merging
                resultStatistics.nMatrixStatistics[i].n00 += statistics.nMatrixStatistics[i].n00;
                resultStatistics.nMatrixStatistics[i].n01 += statistics.nMatrixStatistics[i].n01;
                resultStatistics.nMatrixStatistics[i].n10 += statistics.nMatrixStatistics[i].n10;
                resultStatistics.nMatrixStatistics[i].n11 += statistics.nMatrixStatistics[i].n11;
            }
        }

        // PMatrix/QMatrix merging; need recalculation
        for (int i = 0; i < resultStatistics.nMatrixStatistics.length; ++i) {
            // 00
            if ((resultStatistics.nMatrixStatistics[i].n00 + resultStatistics.nMatrixStatistics[i].n10) != 0) {
                resultStatistics.pMatrixStatistics[i].p00 =
                        (double)resultStatistics.nMatrixStatistics[i].n00
                                / (resultStatistics.nMatrixStatistics[i].n00 + resultStatistics.nMatrixStatistics[i].n10);
            }
            if ((resultStatistics.nMatrixStatistics[i].n00 + resultStatistics.nMatrixStatistics[i].n01) != 0) {
                resultStatistics.qMatrixStatistics[i].p00 =
                        (double)resultStatistics.nMatrixStatistics[i].n00
                                / (resultStatistics.nMatrixStatistics[i].n00 + resultStatistics.nMatrixStatistics[i].n01);
            }

            // 01
            if ((resultStatistics.nMatrixStatistics[i].n01 + resultStatistics.nMatrixStatistics[i].n11) != 0) {
                resultStatistics.pMatrixStatistics[i].p01 =
                        (double)resultStatistics.nMatrixStatistics[i].n01
                                / (resultStatistics.nMatrixStatistics[i].n01 + resultStatistics.nMatrixStatistics[i].n11);
            }
            if ((resultStatistics.nMatrixStatistics[i].n10 + resultStatistics.nMatrixStatistics[i].n11) != 0) {
                resultStatistics.qMatrixStatistics[i].p01 =
                        (double)resultStatistics.nMatrixStatistics[i].n10
                                / (resultStatistics.nMatrixStatistics[i].n10 + resultStatistics.nMatrixStatistics[i].n11);
            }

            // 10
            if ((resultStatistics.nMatrixStatistics[i].n00 + resultStatistics.nMatrixStatistics[i].n10) != 0) {
                resultStatistics.pMatrixStatistics[i].p10 =
                        (double)resultStatistics.nMatrixStatistics[i].n10
                                / (resultStatistics.nMatrixStatistics[i].n00 + resultStatistics.nMatrixStatistics[i].n10);
            }
            if ((resultStatistics.nMatrixStatistics[i].n00 + resultStatistics.nMatrixStatistics[i].n01) != 0) {
                resultStatistics.qMatrixStatistics[i].p10 =
                        (double)resultStatistics.nMatrixStatistics[i].n01
                                / (resultStatistics.nMatrixStatistics[i].n00 + resultStatistics.nMatrixStatistics[i].n01);
            }

            // 11
            if ((resultStatistics.nMatrixStatistics[i].n11 + resultStatistics.nMatrixStatistics[i].n01) != 0) {
                resultStatistics.pMatrixStatistics[i].p11 =
                        (double)resultStatistics.nMatrixStatistics[i].n11
                                / (resultStatistics.nMatrixStatistics[i].n11 + resultStatistics.nMatrixStatistics[i].n01);
            }
            if ((resultStatistics.nMatrixStatistics[i].n10 + resultStatistics.nMatrixStatistics[i].n11) != 0) {
                resultStatistics.qMatrixStatistics[i].p11 =
                        (double)resultStatistics.nMatrixStatistics[i].n11
                                / (resultStatistics.nMatrixStatistics[i].n10 + resultStatistics.nMatrixStatistics[i].n11);
            }
        }
        return resultStatistics;
    }

    private Statistics getCommonStatistics(int numberOfTestKeys, int numberOfApproximations) {
        Random random = new Random();
        long x, key/*, y*/;
        long[] inputVector, realKey;
        int m = 56;
        List<Statistics> statisticsList = new LinkedList<Statistics>();
        for (int i = 0; i < numberOfTestKeys; ++i) {
            x = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            //System.out.println("Random open text (x): " + x);
            //System.out.println("Binary open text: " + IST_6.toBinaryString(x));
            key = (long) (random.nextDouble() * (Integer.MAX_VALUE));
            //System.out.println("Random key (k): " + key);
            //System.out.println("Binary key: " + IST_6.toBinaryString(key));
            //y = IST_5.encrypt(x, key);
            //System.out.println("Cipher text (y): " + y);
            //System.out.println("Binary cipher text: " + IST_6.toBinaryString(y));

            inputVector = LongUtils.longToLongArray(x, IST_3.BLOCK_LENGTH);
            realKey = LongUtils.longToLongArray(key, IST_3.BLOCK_LENGTH);
            //long[] outputVector = LongUtils.longToLongArray(y, IST_3.BLOCK_LENGTH);

            statisticsList.add(statistics(inputVector, realKey, numberOfApproximations, m));
            if (i != 0 & (i % 300 == 0)) {
                System.out.print(".");
            }
        }
        System.out.println();

        Statistics commonStatistics = mergeStatistics(statisticsList);

        commonStatistics.printNMatrix();
        commonStatistics.printPMatrix();
        commonStatistics.printQMatrix();
        commonStatistics.calculateAverageQMatrix();
        commonStatistics.printAverageQMatrix();
        double dominance = commonStatistics.getAverageDominance();
        System.out.println("Dominance: " + dominance);
        commonStatistics.getDominance();
        System.out.println();
        System.out.println();

        return commonStatistics;
    }

    private Statistics getKeysStatistics(int numberOfTestKeys) {
        System.out.println("Keys statistics:");
        return getCommonStatistics(numberOfTestKeys, 1);
    }

    private Statistics getApproximationsStatistics(int numberOfApproximations) {
        System.out.println("Approximations statistics:");
        return getCommonStatistics(2, numberOfApproximations);
    }

    private void printErgodicDigitsList(Statistics keysStatistics, Statistics approximationsStatistics) {
        System.out.println("Ergodic digits statistics:");
        System.out.println("i:\t\tdominance(key)\t\tdominance(approximation)");
        for (int i = 0; i < keysStatistics.nMatrixStatistics.length; ++i) {
            if (Math.signum(keysStatistics.getDominance(i)) == Math.signum(approximationsStatistics.getDominance(i))) {
                System.out.println(i + "\t\t" + keysStatistics.getDominance(i) + "\t\t" + approximationsStatistics.getDominance(i));
            }
        }
    }

    public void run() {
        // get this IST_5 instance only for permutation creation
        IST_5 ist_5 = new IST_5();
        Statistics keysStatistics = getKeysStatistics(3000);
        Statistics approximationsStatistics = getApproximationsStatistics(3000);
        printErgodicDigitsList(keysStatistics, approximationsStatistics);
        /*Random random = new Random();
        long x = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Random open text (x): " + x);
        System.out.println("Binary open text: " + IST_6.toBinaryString(x));
        long key = (long) (random.nextDouble() * (Integer.MAX_VALUE));
        System.out.println("Random key (k): " + key);
        System.out.println("Binary key: " + IST_6.toBinaryString(key));
        long y = IST_5.encrypt(x, key);
        System.out.println("Cipher text (y): " + y);
        System.out.println("Binary cipher text: " + IST_6.toBinaryString(y));

        long[] inputVector = LongUtils.longToLongArray(x, IST_3.BLOCK_LENGTH);
        long[] realKey = LongUtils.longToLongArray(key, IST_3.BLOCK_LENGTH);
        //long[] outputVector = LongUtils.longToLongArray(y, IST_3.BLOCK_LENGTH);

        int m = 56;
        Statistics statistics = statistics(inputVector, realKey, 5, m);
        statistics.printNMatrix();
        statistics.printPMatrix();
        statistics.printQMatrix();
        statistics.calculateAverageQMatrix();
        statistics.printAverageQMatrix();
        double dominance = statistics.getAverageDominance();
        System.out.println("Dominance: " + dominance);
        statistics.getDominance();*/
        /*DoublePairWrapper[] piStatistics = testKeysStatistics(inputVector, statistics, (int)Math.round(Math.pow(dominance, -2)), m);
        System.out.println("pi-Statistics:");
        System.out.println("i:\t\tpi0 pi1:");
        double epsilon = 0.05;
        for (int i = 0; i < piStatistics.length; ++i) {
            System.out.println(i + "\t\t" + piStatistics[i].getFirst() + " " + piStatistics[i].getSecond());
            if (piStatistics[i].getFirst() <= epsilon) {
                System.out.println("Ergodic digit by pi-0");
            } else if (piStatistics[i].getSecond() <= epsilon) {
                System.out.println("Ergodic digit by pi-1");
            }
        }*/
    }
}

class Statistics {
    public NMatrix[] nMatrixStatistics;
    public PMatrix[] pMatrixStatistics;
    public PMatrix[] qMatrixStatistics;
    public PMatrix averageQMatrix;

    public Statistics(int arraysSize) {
        nMatrixStatistics = new NMatrix[arraysSize];
        pMatrixStatistics = new PMatrix[arraysSize];
        qMatrixStatistics = new PMatrix[arraysSize];
        for (int i = 0; i < arraysSize; ++i) {
            nMatrixStatistics[i] = new NMatrix();
            pMatrixStatistics[i] = new PMatrix();
            qMatrixStatistics[i] = new PMatrix();
        }

        averageQMatrix = new PMatrix();
    }

    public void printNMatrix() {
        System.out.println("NMatrix:");
        System.out.println("i:\t\tN:");
        for (int i = 0; i < nMatrixStatistics.length; ++i) {
            System.out.println(i + "\t\t" + nMatrixStatistics[i].n00 + " " + nMatrixStatistics[i].n01);
            System.out.println(" \t\t" + nMatrixStatistics[i].n10 + " " + nMatrixStatistics[i].n11);
        }
    }

    public void printPMatrix() {
        System.out.println("PMatrix:");
        System.out.println("i:\t\tP:");
        for (int i = 0; i < pMatrixStatistics.length; ++i) {
            System.out.println(i + "\t\t" + pMatrixStatistics[i].p00 + " " + pMatrixStatistics[i].p01);
            System.out.println(" \t\t" + pMatrixStatistics[i].p10 + " " + pMatrixStatistics[i].p11);
        }
    }

    public void printQMatrix() {
        System.out.println("QMatrix:");
        System.out.println("i:\t\tQ:");
        for (int i = 0; i < qMatrixStatistics.length; ++i) {
            System.out.println(i + "\t\t" + qMatrixStatistics[i].p00 + " " + qMatrixStatistics[i].p01);
            System.out.println(" \t\t" + qMatrixStatistics[i].p10 + " " + qMatrixStatistics[i].p11);
        }
    }

    public void calculateAverageQMatrix() {
        for (int i = 0; i < qMatrixStatistics.length; ++i) {
            averageQMatrix.p00 += qMatrixStatistics[i].p00;
            averageQMatrix.p01 += qMatrixStatistics[i].p01;
            averageQMatrix.p10 += qMatrixStatistics[i].p10;
            averageQMatrix.p11 += qMatrixStatistics[i].p11;
        }
        averageQMatrix.p00 /= qMatrixStatistics.length;
        averageQMatrix.p01 /= qMatrixStatistics.length;
        averageQMatrix.p10 /= qMatrixStatistics.length;
        averageQMatrix.p11 /= qMatrixStatistics.length;
    }

    public void printAverageQMatrix() {
        System.out.println("Average QMatrix:");
        System.out.println("\t\t" + averageQMatrix.p00 + " " + averageQMatrix.p01);
        System.out.println("\t\t" + averageQMatrix.p10 + " " + averageQMatrix.p11);
    }

    public double getDominance(int i) {
        return Math.max(pMatrixStatistics[i].p00 - 0.5, pMatrixStatistics[i].p11 - 0.5);
    }

    public void getDominance() {
        System.out.println("PMatrix dominance: ");
        System.out.println("i: \t\tdominance:");
        for (int i = 0; i < pMatrixStatistics.length; ++i) {
            System.out.println(i + "\t\t" + getDominance(i));
        }
    }

    public double getAverageDominance() {
        double p00AverageValue = 0.0d;
        double p11AverageValue = 0.0d;
        for (int i = 0; i < pMatrixStatistics.length; ++i) {
            p00AverageValue += pMatrixStatistics[i].p00 - 0.5;
            p11AverageValue += pMatrixStatistics[i].p11 - 0.5;
        }
        return Math.max(
                p00AverageValue / pMatrixStatistics.length,
                p11AverageValue / pMatrixStatistics.length
        );
    }
}

class NMatrix {
    public int n00;
    public int n01;
    public int n10;
    public int n11;
}

class PMatrix {
    public double p00;
    public double p01;
    public double p10;
    public double p11;
}

class LongUtils {
    public static long[] longToLongArray(long x, int arrayLength) {
        long[] result = new long[arrayLength];
        for (int i = arrayLength - 1; i > -1; --i) {
            result[i] = x & 0x1;
            x >>= 1;
        }
        return result;
    }

    public static long longArrayToLong(long[] array) {
        int result = 0;
        for (int i = 0; i < array.length; ++i) {
            result |= array[i] << i;
        }
        return result;
    }
}

class IntegerPairWrapper {
    private int first;
    private int second;
    public IntegerPairWrapper(int first, int second){
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}

class DoublePairWrapper {
    private double first;
    private double second;
    public DoublePairWrapper(double first, double second){
        this.first = first;
        this.second = second;
    }

    public double getFirst() {
        return first;
    }

    public double getSecond() {
        return second;
    }

    public void setFirst(double first) {
        this.first = first;
    }

    public void setSecond(double second) {
        this.second = second;
    }
}