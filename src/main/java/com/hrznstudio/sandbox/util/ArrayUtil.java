package com.hrznstudio.sandbox.util;

import org.apache.commons.lang3.ArrayUtils;

public class ArrayUtil {
    public static <T> T[] removeAll(T[] arr, T... occ) {
        for (T oc : occ) {
            arr = ArrayUtils.removeAllOccurences(arr, oc);
        }
        return arr;
    }
}
