package org.sandboxpowered.sandbox.fabric.util;

import org.apache.commons.lang3.ArrayUtils;

public class ArrayUtil {
    @SafeVarargs
    public static <T> T[] removeAll(T[] arr, T... occ) {
        for (T oc : occ) {
            arr = ArrayUtils.removeAllOccurences(arr, oc);
        }
        return arr;
    }
}
