package org.rlms.lucene.sample;

import java.util.List;

import org.rlms.common.services.ServiceFacade;
import org.rlms.common.util.LogUtil;
import org.rlms.lucene.core.FieldOption;
import org.rlms.lucene.core.LuceneSession;
import org.rlms.lucene.criteria.Criteria;
import org.rlms.lucene.criteria.CriteriaPool;
import org.rlms.lucene.vo.DocumentVO;

public class LuceneSample2 {

    public static void main(String[] args) {
        indexByFilePath();
        searchByCriteria();
        searchBy2Criteria();
        searchBy3Criteria();
    }

    private static void indexByFilePath() {
        LuceneSession session = LuceneSession.link("").autoCommit(false).createSession();
        String dataDir = "D:\\Lucene\\Data";
        ServiceFacade.getLuceneService(session).createIndex(dataDir, f -> f.getName().endsWith("txt"));
        session.getTransactionManager().commit();
        session.close();
    }

    private static void searchByCriteria() {
        LuceneSession session = LuceneSession.link("").createSession();

        Criteria searchCriterian = CriteriaPool.createCriteria(FieldOption.CONTENTS, "Anna");
        List<DocumentVO> docs = ServiceFacade.getLuceneService(session).doSearch(searchCriterian);

        for(DocumentVO doc : docs) {
            LogUtil.info("1 Criteria, getFilePath: {}", doc.getFilePath().getFieldValue());
            LogUtil.info("1 Criteria, getFileName: {}", doc.getFileName().getFieldValue());
            LogUtil.info("1 Criteria, getContents: {}", doc.getContents().getFieldValue());
        }
    }

    private static void searchBy2Criteria() {
        Criteria criteria1 = CriteriaPool.createCriteria(FieldOption.CONTENTS, "Lian");
        Criteria criteria2 = CriteriaPool.createCriteria(FieldOption.CONTENTS, "Villa");

        Criteria searchCriterian = CriteriaPool.and(criteria1, criteria2);

        LuceneSession session = LuceneSession.link("").createSession();
        List<DocumentVO> docs = ServiceFacade.getLuceneService(session).doSearch(searchCriterian);

        for(DocumentVO doc : docs) {
            LogUtil.info("2 Criteria, getFilePath: {}", doc.getFilePath().getFieldValue());
            LogUtil.info("2 Criteria, getFileName: {}", doc.getFileName().getFieldValue());
            LogUtil.info("2 Criteria, getContents: {}", doc.getContents().getFieldValue());
        }
    }

    private static void searchBy3Criteria() {
        Criteria criteria1 = CriteriaPool.createCriteria(FieldOption.CONTENTS, "Anna");
        Criteria criteria2 = CriteriaPool.createCriteria(FieldOption.CONTENTS, "Lian");
        Criteria criteria3 = CriteriaPool.createCriteria(FieldOption.CONTENTS, "Villa");

        Criteria searchCriterian = CriteriaPool.or(criteria3, CriteriaPool.and(criteria1, criteria2));

        LuceneSession session = LuceneSession.link("").createSession();
        List<DocumentVO> docs = ServiceFacade.getLuceneService(session).doSearch(searchCriterian);

        for(DocumentVO doc : docs) {
            LogUtil.info("3 Criteria, getFilePath: {}", doc.getFilePath().getFieldValue());
            LogUtil.info("3 Criteria, getFileName: {}", doc.getFileName().getFieldValue());
            LogUtil.info("3 Criteria, getContents: {}", doc.getContents().getFieldValue());
            LogUtil.info("====================================================");
        }
    }
}
