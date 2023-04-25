package com.sophony.flow.commons.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * CollectionUtils
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/27 9:56
 */
public abstract class CollectionUtils {
    public CollectionUtils() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Object... arrs) {
        return arrs == null || arrs.length == 0;
    }

    public static <T> List<T> newArrayList(T... ts) {
        List<T> list = new ArrayList();
        if (ts != null && ts.length > 0) {
            list.addAll(Arrays.asList(ts));
        }

        return list;
    }
}
