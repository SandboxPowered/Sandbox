package org.sandboxpowered.sandbox.fabric.util;

import org.apache.commons.lang3.ArrayUtils;

public class ArrayUtil {
    private ArrayUtil() {
    }

    @SafeVarargs
    public static <T> T[] removeAll(T[] arr, T... occ) {
        for (T oc : occ) {
            arr = ArrayUtils.removeAllOccurences(arr, oc);
        }
        return arr;
    }

    public static String join(String[] filenames, String s) {
        if (filenames.length == 1)
            return filenames[0];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < filenames.length; i++) {
            String filename = filenames[i];
            builder.append(filename);
            if (i != filenames.length - 1)
                builder.append(s);
        }
        return builder.toString();
    }
}