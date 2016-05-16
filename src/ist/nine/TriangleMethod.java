package ist.nine;

import ist.common.AbstractEncryptionCase;

public class TriangleMethod extends AbstractEncryptionCase {
    private static final int NUMBER_OF_ELEMENTS = (int) Math.pow(2.0d, BIT_LENGTH);
    private boolean[][] methodMatrix = new boolean[NUMBER_OF_ELEMENTS][NUMBER_OF_ELEMENTS];
    ISubstitutionFunctions substitutionFunctions;

    private void printSubstitutionResults(boolean reversedSubstitution) {
        boolean printedOnce = false;
        for (int i = 0; i < NUMBER_OF_ELEMENTS; ++i) {
            if (methodMatrix[0][i] == true) {
                String parametersString;
                if (!printedOnce) {
                    parametersString = "";
                } else {
                    parametersString = " ^ ";
                }
                if (((i & 0x8) >> 3) == 1) {
                    if (reversedSubstitution) {
                        parametersString += "v1";
                    } else {
                        parametersString += "u1";
                    }
                }
                if (((i & 0x4) >> 2) == 1) {
                    if (reversedSubstitution) {
                        parametersString += "v2";
                    } else {
                        parametersString += "u2";
                    }
                }
                if (((i & 0x2) >> 1) == 1) {
                    if (reversedSubstitution) {
                        parametersString += "v3";
                    } else {
                        parametersString += "u3";
                    }
                }
                if ((i & 0x1) == 1) {
                    if (reversedSubstitution) {
                        parametersString += "v4";
                    } else {
                        parametersString += "u4";
                    }
                }
                if (i == 0) {
                    parametersString += "1";
                }

                System.out.print(parametersString);
                if (!printedOnce) {
                    printedOnce = true;
                }
            }
        }
    }

    private void triangleMethod() {
        for (int i = 1; i < NUMBER_OF_ELEMENTS; ++i) {
            for (int j = 0; j < NUMBER_OF_ELEMENTS - i; ++j) {
                methodMatrix[j][i] = methodMatrix[j][i - 1] ^ methodMatrix[j + 1][i - 1];
            }
        }
    }

    private void runV1() {
        System.out.println("Running V1 function:");
        for (int i = 0; i < NUMBER_OF_ELEMENTS; ++i) {
            methodMatrix[i][0] = substitutionFunctions.v1(
                    ((i & 0x8) >> 3) == 1,
                    ((i & 0x4) >> 2) == 1,
                    ((i & 0x2) >> 1) == 1,
                    (i & 0x1) == 1);
        }
        triangleMethod();
        printSubstitutionResults(false);
        System.out.println();
    }

    private void runV2() {
        System.out.println("Running V2 function:");
        for (int i = 0; i < NUMBER_OF_ELEMENTS; ++i) {
            methodMatrix[i][0] = substitutionFunctions.v2(
                    ((i & 0x8) >> 3) == 1,
                    ((i & 0x4) >> 2) == 1,
                    ((i & 0x2) >> 1) == 1,
                    (i & 0x1) == 1);
        }
        triangleMethod();
        printSubstitutionResults(false);
        System.out.println();
    }

    private void runV3() {
        System.out.println("Running V3 function:");
        for (int i = 0; i < NUMBER_OF_ELEMENTS; ++i) {
            methodMatrix[i][0] = substitutionFunctions.v3(
                    ((i & 0x8) >> 3) == 1,
                    ((i & 0x4) >> 2) == 1,
                    ((i & 0x2) >> 1) == 1,
                    (i & 0x1) == 1);
        }
        triangleMethod();
        printSubstitutionResults(false);
        System.out.println();
    }

    private void runV4() {
        System.out.println("Running V4 function:");
        for (int i = 0; i < NUMBER_OF_ELEMENTS; ++i) {
            methodMatrix[i][0] = substitutionFunctions.v4(
                    ((i & 0x8) >> 3) == 1,
                    ((i & 0x4) >> 2) == 1,
                    ((i & 0x2) >> 1) == 1,
                    (i & 0x1) == 1);
        }
        triangleMethod();
        printSubstitutionResults(false);
        System.out.println();
    }

    private void runU1() {
        System.out.println("Running U1 function:");
        for (int i = 0; i < NUMBER_OF_ELEMENTS; ++i) {
            methodMatrix[i][0] = substitutionFunctions.u1(
                    ((i & 0x8) >> 3) == 1,
                    ((i & 0x4) >> 2) == 1,
                    ((i & 0x2) >> 1) == 1,
                    (i & 0x1) == 1);
        }
        triangleMethod();
        printSubstitutionResults(true);
        System.out.println();
    }

    private void runU2() {
        System.out.println("Running U2 function:");
        for (int i = 0; i < NUMBER_OF_ELEMENTS; ++i) {
            methodMatrix[i][0] = substitutionFunctions.u2(
                    ((i & 0x8) >> 3) == 1,
                    ((i & 0x4) >> 2) == 1,
                    ((i & 0x2) >> 1) == 1,
                    (i & 0x1) == 1);
        }
        triangleMethod();
        printSubstitutionResults(true);
        System.out.println();
    }

    private void runU3() {
        System.out.println("Running U3 function:");
        for (int i = 0; i < NUMBER_OF_ELEMENTS; ++i) {
            methodMatrix[i][0] = substitutionFunctions.u3(
                    ((i & 0x8) >> 3) == 1,
                    ((i & 0x4) >> 2) == 1,
                    ((i & 0x2) >> 1) == 1,
                    (i & 0x1) == 1);
        }
        triangleMethod();
        printSubstitutionResults(true);
        System.out.println();
    }

    private void runU4() {
        System.out.println("Running U4 function:");
        for (int i = 0; i < NUMBER_OF_ELEMENTS; ++i) {
            methodMatrix[i][0] = substitutionFunctions.u4(
                    ((i & 0x8) >> 3) == 1,
                    ((i & 0x4) >> 2) == 1,
                    ((i & 0x2) >> 1) == 1,
                    (i & 0x1) == 1);
        }
        triangleMethod();
        printSubstitutionResults(true);
        System.out.println();
    }

    public void run(ISubstitutionFunctions substitutionFunctions) {
        this.substitutionFunctions = substitutionFunctions;
        // run substitution
        runV1();
        runV2();
        runV3();
        runV4();

        // run reversed substitution
        runU1();
        runU2();
        runU3();
        runU4();
    }
}
