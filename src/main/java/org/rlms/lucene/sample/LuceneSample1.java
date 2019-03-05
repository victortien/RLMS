package org.rlms.lucene.sample;

import java.util.List;

import org.rlms.common.services.ServiceFacade;
import org.rlms.common.util.LogUtil;
import org.rlms.lucene.core.FieldOption;
import org.rlms.lucene.core.LuceneSession;
import org.rlms.lucene.vo.DocumentVO;
import org.rlms.lucene.vo.FieldVO;

public class LuceneSample1 {

    public static void main(String[] args) {
        indexByCustomizedObj();
        searchByText("lian");
    }

    private static void indexByCustomizedObj() {
        DocumentVO doc = new DocumentVO();
        FieldVO field = new FieldVO();
        field.setFieldType(FieldOption.CONTENTS);
        field.setFieldValue("Jacky, Mary, Josh, Lian, Anna");
        doc.setContents(field);

        DocumentVO doc2 = new DocumentVO();
        FieldVO field2 = new FieldVO();
        field2.setFieldType(FieldOption.CONTENTS);
        field2.setFieldValue("Daniel, Emma, Lian, Villa, Caroline");
        doc2.setContents(field2);

        LuceneSession session = LuceneSession.link("D:\\Lucene").autoCommit(false).createSession();
        ServiceFacade.getLuceneService(session).createIndex(doc, doc2);
        session.getTransactionManager().commit();
        session.close();
    }

    private static void searchByText(String searchText) {
        FieldVO field = new FieldVO();
        field.setFieldType(FieldOption.CONTENTS);
        field.setFieldValue(searchText);

        LuceneSession session = LuceneSession.link("D:\\Lucene").createSession();
        List<DocumentVO> docs = ServiceFacade.getLuceneService(session).searchTextField(field);
        for(DocumentVO doc : docs) {
            LogUtil.info("The Contents is: {}", doc.getContents().getFieldValue());
        }
    }

}
