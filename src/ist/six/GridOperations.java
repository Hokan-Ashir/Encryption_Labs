package ist.six;

public class GridOperations {
    public static double gridMIN(double[] inputVector) {
        double returnValue = inputVector[0];
        for (int i = 1; i < inputVector.length; ++i) {
            returnValue = Math.min(returnValue, inputVector[i]);
        }
        return Math.round(returnValue * 100) / 100.0d;
    }

    public static double gridMAX(double[] inputVector) {
        double returnValue = inputVector[0];
        for (int i = 1; i < inputVector.length; ++i) {
            returnValue = Math.max(returnValue, inputVector[i]);
        }
        return Math.round(returnValue * 100) / 100.0d;
    }

    public static double gridXOR(double a, double b) {
        if (a == 1 || a == 0 || b == 1 || b == 0) {
        if (a > b) {
            return Math.round((a - b) * 100) / 100.0d;
        } else {
            return Math.round((b - a) * 100) / 100.0d;
        }
        } else {
            if (a == 0.5 || b == 0.5) {
                return 0.5;
            } else if (Math.abs(a - 0.5) < Math.abs(b - 0.5)) {
                return a;
            } else {
                return b;
            }
        }
        /*if (a == 0.5 || b == 0.5) {
            return 0.5;
        } else if (Math.abs(a - 0.5) < Math.abs(b - 0.5)) {
            return a;
        } else {
            return b;
        }*/
    }
}
