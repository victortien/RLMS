package org.rlms.common.util;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class MathUtil {
    private MathUtil() {
    }

    public static BigDecimal toBigDecimal(Object obj) {
        if(obj == null)
            return BigDecimal.ZERO;
        if(obj instanceof String)
            return StringUtils.isEmpty((String)obj) ? BigDecimal.ZERO : new BigDecimal((String)obj);
        if(obj instanceof Number)
            return new BigDecimal(obj.toString());
        if(obj instanceof BigDecimal)
            return (BigDecimal) obj;
        return BigDecimal.ZERO;
    }

    public static boolean isZero(Number num) {
        if(num == null)
            return false;
        if(num instanceof BigDecimal)
            return (BigDecimal.ZERO).compareTo((BigDecimal) num) == 0;
        if(num instanceof Integer)
            return Integer.valueOf(0) == num.intValue();
        if(num instanceof Double)
            return Double.valueOf(0) == num.doubleValue();
        if(num instanceof Float)
            return Float.valueOf(0) == num.floatValue();
        if(num instanceof Long)
            return Long.valueOf(0) == num.longValue();
        if(num instanceof Short)
            return Short.valueOf((short)0) == num.shortValue();
        return false;
    }
}
