package ist.nine;

import java.math.BigDecimal;

public class TwoAdicOperations {
    public static long twoAdicXOR(long a, long b, int m) {
        return BigDecimal.valueOf(a).add(BigDecimal.valueOf(b)).remainder(BigDecimal.valueOf(Math.pow(2.0d, m))).longValue();
    }

    public static long twoAdicXOR(long[] a, int m) {
        long result = twoAdicXOR(a[0], a[1], m);
        for (int i = 2; i < a.length; ++i) {
            result = twoAdicXOR(result, a[i], m);
        }
        return result;
    }

    public static long twoAdicAND(long a, long b, int m) {
        return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b)).remainder(BigDecimal.valueOf(Math.pow(2.0d, m))).longValue();
    }

    public static long twoAdicAND(long[] a, int m) {
        long result = twoAdicAND(a[0], a[1], m);
        for (int i = 2; i < a.length; ++i) {
            result = twoAdicAND(result, a[i], m);
        }
        return result;
    }
}
