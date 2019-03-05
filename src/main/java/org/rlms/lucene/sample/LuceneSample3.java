package org.rlms.lucene.sample;

import java.math.BigDecimal;

import org.rlms.common.services.ServiceFacade;
import org.rlms.common.util.JsonUtil;
import org.rlms.common.util.LogUtil;
import org.rlms.lucene.core.FieldOption;
import org.rlms.lucene.core.LuceneSession;
import org.rlms.lucene.vo.DocumentVO;
import org.rlms.lucene.vo.FieldVO;

public class LuceneSample3 {

    private static final String STUDENT_ID = "aa12345";

    private static final String DOC_TYPE_INFO = "INFO";

    private static final String DOC_TYPE_GRADE = "GRADE";

    public static void main(String[] args) {

        prepareTestData();

        LuceneSession session = LuceneSession.link("D:\\Lucene").createSession();
        String jsonString = ServiceFacade.getLuceneService(session).getMergedTypeBySingleId(STUDENT_ID, DOC_TYPE_INFO, DOC_TYPE_GRADE);
        LogUtil.info("The result json is: " + jsonString);
    }

    private static void prepareTestData() {
        StudentInfoVO ivo = new StudentInfoVO();
        ivo.setStudentId(STUDENT_ID);
        ivo.setSex("male");
        ivo.setPhoneNumber("0123456789");
        DocumentVO d1 = createDoc(STUDENT_ID, DOC_TYPE_INFO, JsonUtil.toJson(ivo));

        StudentGradeVO gvo = new StudentGradeVO();
        gvo.setStudentId(STUDENT_ID);
        gvo.setSubject("Math");
        gvo.setGrade(BigDecimal.valueOf(100));
        DocumentVO d2 = createDoc(STUDENT_ID, DOC_TYPE_GRADE, JsonUtil.toJson(gvo));

        StudentGradeVO gvo2 = new StudentGradeVO();
        gvo2.setStudentId(STUDENT_ID);
        gvo2.setSubject("English");
        gvo2.setGrade(BigDecimal.valueOf(95));
        DocumentVO d3 = createDoc(STUDENT_ID, DOC_TYPE_GRADE, JsonUtil.toJson(gvo2));

        LuceneSession session = LuceneSession.link("").createSession();
        ServiceFacade.getLuceneService(session).createIndex(d1, d2, d3);
    }

    private static DocumentVO createDoc(String id, String type, String content) {
        DocumentVO vo = new DocumentVO();
        vo.setDocId(FieldVO.create(FieldOption.DOC_ID, id));
        vo.setDocType(FieldVO.create(FieldOption.DOC_TYPE, type));
        vo.setContents(FieldVO.create(FieldOption.CONTENTS, content));
        return vo;
    }
}

class StudentInfoVO {
    private String studentId;
    private String sex;
    private String phoneNumber;

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

class StudentGradeVO {
    private String studentId;
    private String subject;
    private BigDecimal grade;

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public BigDecimal getGrade() {
        return grade;
    }
    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }
}
