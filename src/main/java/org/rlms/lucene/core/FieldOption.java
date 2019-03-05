package org.rlms.lucene.core;

public enum FieldOption {

    DOC_ID("docId"),
    DOC_TYPE("docType"),
    CONTENTS("contents"),
    FILE_PATH("filePath"),
    FILE_NAME("fileName"),
    ;

    private String name;

    private FieldOption(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
