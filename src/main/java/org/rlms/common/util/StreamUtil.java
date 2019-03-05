package org.rlms.common.util;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamUtil {

    private StreamUtil() {
    }

    public static <T> Stream<T> ofNullable(T t) {
        return t == null ? Stream.empty() : Stream.of(t);
    }

    public static <T> Stream<T> ofNullable(T[] t) {
        return t == null ? Stream.empty() : Stream.of(t);
    }

    public static <T> Stream<T> ofNullable(Collection<T> collection) {
        return collection == null ? Stream.empty() : collection.stream();
    }

    public static IntStream ofRangeStream(Number length) {
        return IntStream.range(0, length.intValue());
    }

}
