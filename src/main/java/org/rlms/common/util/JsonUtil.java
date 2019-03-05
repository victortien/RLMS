package org.rlms.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.rlms.common.exception.BaseException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;

public class JsonUtil {

    private JsonUtil() {}

    private static final String ISO8601_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static String toJson(Object src) {
        return toJson(src, ISO8601_DATETIME_FORMAT);
    }

    public static String toJson(Object src, String datePattern) {
        try {
            ObjectMapper mapper = getJsonMapper();
            mapper.setDateFormat(new SimpleDateFormat(datePattern));

            ObjectWriter writter = mapper.writerWithDefaultPrettyPrinter();
            return  writter.writeValueAsString(src);
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public static <T> T fromJson(String jsonString, Class<T> classOfT) {
        try {
            ObjectMapper mapper = getJsonMapper();
            return mapper.readValue(jsonString, classOfT);
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public static <T> List<T> fromJsonToList(String jsonString, Class<?> classOfT) {
        try {
            ObjectMapper mapper = getJsonMapper();
            CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, classOfT);
            return mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public static <K, V> Map<K, V> fromJsonToMap(String jsonString) {
        try {
            ObjectMapper mapper = getJsonMapper();
            return mapper.readValue(jsonString, new TypeReference<Map<K, V>>() {});
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    private static ObjectMapper getJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.setDefaultPropertyInclusion(Include.NON_DEFAULT);
        return mapper;
    }

}
