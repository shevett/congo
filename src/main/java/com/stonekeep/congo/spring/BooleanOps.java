package com.stonekeep.congo.spring;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A hack for doing boolean operations inside Spring config files. These
 * operations are relatively null-safe, following SQL 3-valued logic.
 */
public class BooleanOps {
    public static Boolean and(Boolean... operands) {
        Set<Boolean> values = new HashSet<Boolean>(Arrays.asList(operands));
        if (values.contains(null))
            return null;
        if (values.contains(false))
            return false;
        return true;
    }

    public static Boolean or(Boolean... operands) {
        Set<Boolean> values = new HashSet<Boolean>(Arrays.asList(operands));
        if (values.contains(true))
            return true;
        if (values.contains(null))
            return null;
        return false;
    }

    public static Boolean not(Boolean operand) {
        if (operand == null)
            return null;
        return !operand;
    }
}
