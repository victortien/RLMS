package org.rlms.lucene.vo;

import org.rlms.lucene.core.FieldOption;

public class FieldVO {

    public static FieldVO create(FieldOption fieldType, String fieldValue) {
        FieldVO vo = new FieldVO();
        vo.setFieldType(fieldType);
        vo.setFieldValue(fieldValue);
        return vo;
    }

    private FieldOption fieldType;

    private String fieldValue;

    public FieldOption getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldOption fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

}
