package org.rlms.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

    private LogUtil() {
    }

    private static final String LOG_PACKAGE = "com.fareast.hbo.common.util.LogUtil";

    private static String getCallingClass() {
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String callingClass = "";
        for (int i = 2; i < trace.length; i++) {
            String clazz = trace[i].getClassName();
            if (!LOG_PACKAGE.equals(clazz)) {
                callingClass = clazz;
                break;
            }
        }
        return callingClass;
    }

    public static void info(String msg) {
        info("{}", msg);
    }

    public static void info(String format, Object... arguments) {
        Logger logger = LoggerFactory.getLogger(getCallingClass());
        logger.info(format, arguments);
    }

    public static void warn(String msg) {
        warn("{}", msg);
    }

    public static void warn(String format, Object... arguments) {
        Logger logger = LoggerFactory.getLogger(getCallingClass());
        logger.warn(format, arguments);
    }

    public static void error(String msg) {
        error("{}", msg);
    }

    public static void error(String format, Object... arguments) {
        Logger logger = LoggerFactory.getLogger(getCallingClass());
        logger.error(format, arguments);
    }

    public static void debug(String msg) {
        debug("{}", msg);
    }

    public static void debug(String format, Object... arguments) {
        Logger logger = LoggerFactory.getLogger(getCallingClass());
        logger.debug(format, arguments);
    }

    public static void trace(String msg) {
        trace("{}", msg);
    }

    public static void trace(String format, Object... arguments) {
        Logger logger = LoggerFactory.getLogger(getCallingClass());
        logger.trace(format, arguments);
    }
}
