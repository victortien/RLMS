package org.rlms.common.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.rlms.common.exception.BaseException;

public class FileUtil {

    private FileUtil(){}

    public static final String ENCODING_UTF8 = "UTF-8";

    public static String readTextFile(InputStream inStream) {
        try {
            int ca = inStream.available();
            byte[] by = new byte[ca];

            int isSuccess = inStream.read(by);
            String content = (isSuccess != -1) ? new String(by, ENCODING_UTF8) : StringUtils.EMPTY;

            inStream.close();
            return content;
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public static boolean isEmptyFile(File file) {

        if(ObjectUtil.isEmpty(file)) {
            return true;
        }

        Set<Boolean> validateSet = new HashSet<>();
        validateSet.add(!file.isDirectory());
        validateSet.add(!file.isHidden());
        validateSet.add(file.exists());
        validateSet.add(file.canRead());

        return validateSet.contains(false);
    }

    public static void writeTextFile(String content, String fileName) {
        try {
            File file = new File(fileName);
            FileUtils.touch(file);
            FileUtils.write(file, content, ENCODING_UTF8);
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public static File[] listFiles(String filePath, FilenameFilter filter) {
        File file = new File(filePath);
        return file.listFiles(filter);
    }

    public static String getBaseFileName(String filePath) {
        File file = new File(filePath);
        return FilenameUtils.getBaseName(file.getName());
    }

    public static void writeRecordFile(String absoluteFileName, String record) {
        try {
            verifyFile(new File(absoluteFileName));

            String dateTime = DateUtil.getDateString(LocalDateTime.now(), DateTimeFormatSet.DATETIME_FORMATE1.getFormat());
            String recordStr = dateTime + " " + record;

            writeTextFile(recordStr, absoluteFileName);
        } catch (Exception e) {
            LogUtil.error("Error occurred while write record file: {}, {}", record, e.getMessage());
            throw new BaseException(e);
        }
    }

    public static File verifyFile(File in) {
        File parent = in.getParentFile();

        if(!parent.exists()) {
            parent.mkdirs();
        }
        return in;
    }

    public static String combineFilePath(String... separators) {
        return StreamUtil.ofNullable(separators)
                .filter(StringUtils::isNotEmpty)
                .reduce((a, b) -> a + File.separator + b).orElse(StringUtils.EMPTY);
    }

}
