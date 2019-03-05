package org.rlms.common.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import org.rlms.common.exception.BaseException;

public class ObjectUtil {

    private ObjectUtil() {}

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static <T> T castTo(Object obj, Class<T> classOfT) {
        if(!classOfT.isInstance(obj))
            throw new BaseException("Class Cast Error with: " + obj.getClass().getName() + " cannot to be " + classOfT.getName());
        return classOfT.cast(obj);
    }

    public static void releaseObjMemory(Object... objs) {
        releaseArrayMemory(objs);
    }

    @SafeVarargs
    public static <T> void releaseArrayMemory(T... objs) {
        StreamUtil.ofNullable(objs).forEach(obj -> obj = null);
    }

}
