package org.rlms.common.util;

import java.util.EnumSet;
import java.util.Set;

public enum DateTimeFormatSet {
    //Date Format
    DATE_FORMATE1("yyyy/MM/dd"),
    DATE_FORMATE2("MM/dd/yyyy"),
    DATE_FORMATE3("yyyyMMdd"),
    //DateTime Format
    DATETIME_FORMATE1("yyyy-MM-dd HH:mm:ss");

    private String format;

    private DateTimeFormatSet(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }

    private static final EnumSet<DateTimeFormatSet> DATE_TIME_FORMAT_SET =
            EnumSet.of(DATETIME_FORMATE1);

    public static Set<DateTimeFormatSet> getDateTimeFormat() {
        return DATE_TIME_FORMAT_SET;
    }
}
