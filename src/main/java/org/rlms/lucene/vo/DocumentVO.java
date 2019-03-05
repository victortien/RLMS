package org.rlms.lucene.vo;

public class DocumentVO {

    private FieldVO docId;
    private FieldVO docType;
    private FieldVO fileName;
    private FieldVO filePath;
    private FieldVO contents;

    public FieldVO getDocId() {
        return docId;
    }
    public void setDocId(FieldVO docId) {
        this.docId = docId;
    }
    public FieldVO getDocType() {
        return docType;
    }
    public void setDocType(FieldVO docType) {
        this.docType = docType;
    }
    public FieldVO getFileName() {
        return fileName;
    }
    public void setFileName(FieldVO fileName) {
        this.fileName = fileName;
    }
    public FieldVO getFilePath() {
        return filePath;
    }
    public void setFilePath(FieldVO filePath) {
        this.filePath = filePath;
    }
    public FieldVO getContents() {
        return contents;
    }
    public void setContents(FieldVO contents) {
        this.contents = contents;
    }

}
