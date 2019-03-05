package org.rlms.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.rlms.common.exception.BaseException;

public class PropertiesUtil {

    private PropertiesUtil() {
    }

    public static String getResourceBundle(String propKey, String fileName) {
        ResourceBundle res = ResourceBundle.getBundle(fileName);

        return res.getString(propKey);
    }

    public static String getFileProperty(String propKey, String fileName) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(fileName));
            return properties.getProperty(propKey);
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

}
