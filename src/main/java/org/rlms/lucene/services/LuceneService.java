package org.rlms.lucene.services;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.Query;
import org.rlms.common.exception.BaseException;
import org.rlms.common.util.FileUtil;
import org.rlms.common.util.JsonUtil;
import org.rlms.common.util.ObjectUtil;
import org.rlms.common.util.StreamUtil;
import org.rlms.lucene.core.FieldOption;
import org.rlms.lucene.core.LuceneSession;
import org.rlms.lucene.criteria.Criteria;
import org.rlms.lucene.criteria.CriteriaPool;
import org.rlms.lucene.util.QueryUtil;
import org.rlms.lucene.vo.DocumentVO;
import org.rlms.lucene.vo.FieldVO;

public class LuceneService {

    private LuceneSession session;

    public LuceneService(LuceneSession session) {
        this.session = session;
    }

    public List<DocumentVO> doSearch(Criteria searchCriterian) {
        Query query = QueryUtil.paresQuery(searchCriterian);
        return doSearch(query);
    }

    public List<DocumentVO> searchTextField(FieldVO searchContent) {
        Query query = QueryUtil.paresQuery(searchContent);
        return doSearch(query);
    }

    public List<DocumentVO> doSearch(Query query) {
        List<Document> hitDocs = session.getLuceneManager().search(new BoostQuery(query, 2.0f));

        return StreamUtil.ofNullable(hitDocs).map(hitDoc -> {
            DocumentVO vo = new DocumentVO();
            vo.setDocId(FieldVO.create(FieldOption.DOC_ID, hitDoc.get(FieldOption.DOC_ID.getName())));
            vo.setDocType(FieldVO.create(FieldOption.DOC_TYPE, hitDoc.get(FieldOption.DOC_TYPE.getName())));
            vo.setFileName(FieldVO.create(FieldOption.FILE_NAME, hitDoc.get(FieldOption.FILE_NAME.getName())));
            vo.setFilePath(FieldVO.create(FieldOption.FILE_PATH, hitDoc.get(FieldOption.FILE_PATH.getName())));
            vo.setContents(FieldVO.create(FieldOption.CONTENTS, hitDoc.get(FieldOption.CONTENTS.getName())));
            return vo;
        }).collect(Collectors.toList());
    }

    public void createIndex(List<DocumentVO> documentList) {
        if(CollectionUtils.isEmpty(documentList))
            return;

        createIndex(documentList.toArray(new DocumentVO[] {}));
    }

    public void createIndex(DocumentVO... documentVOs) {
        if(ArrayUtils.isEmpty(documentVOs))
            return;

        Document[] luceneDocs = StreamUtil.ofNullable(documentVOs).map(this::getDocument).toArray(Document[]::new);

        session.getLuceneManager().create(luceneDocs);
    }

    public void createIndex(File file) {
        if(FileUtil.isEmptyFile(file))
            return;

        DocumentVO documentVO = convertFileToDoc(file);
        createIndex(documentVO);
    }

    public void createIndex(String filePath, FileFilter fileFilter) {
        if(StringUtils.isEmpty(filePath)) return;

        File[] files = new File(filePath).listFiles();

        FileFilter adjustFilter = Optional.ofNullable(fileFilter).orElse(f -> true);

        DocumentVO[] documents = StreamUtil.ofNullable(files)
                .filter(adjustFilter::accept)
                .map(this::convertFileToDoc).toArray(DocumentVO[]::new);

        createIndex(documents);
    }

    private DocumentVO convertFileToDoc(File file) {
        try {
            DocumentVO documentVO = new DocumentVO();
            documentVO.setContents(FieldVO.create(FieldOption.CONTENTS, FileUtils.readFileToString(file, StandardCharsets.UTF_8)));
            documentVO.setFileName(FieldVO.create(FieldOption.FILE_NAME, file.getName()));
            documentVO.setFilePath(FieldVO.create(FieldOption.FILE_PATH, file.getCanonicalPath()));
            return documentVO;
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public void updateIndex(FieldVO updateCheckField, DocumentVO documentVO) {
        Document doc = getDocument(documentVO);
        Term updateCheckTerm = new Term(updateCheckField.getFieldType().getName(), updateCheckField.getFieldValue());
        session.getLuceneManager().update(updateCheckTerm, doc);
    }


    private Document getDocument(DocumentVO documentVO) {
        Document document = new Document();

        if(ObjectUtil.isNotEmpty(documentVO.getContents())) {
            addField(documentVO.getContents(), document);
        }
        if(ObjectUtil.isNotEmpty(documentVO.getDocId())) {
            addField(documentVO.getDocId(), document);
        }
        if(ObjectUtil.isNotEmpty(documentVO.getDocType())) {
            addField(documentVO.getDocType(), document);
        }
        if(ObjectUtil.isNotEmpty(documentVO.getFileName())) {
            addField(documentVO.getFileName(), document);
        }
        if(ObjectUtil.isNotEmpty(documentVO.getFilePath())) {
            addField(documentVO.getFilePath(), document);
        }

        return document;
    }

    private void addField(FieldVO fieldVO, Document document) {
        Field field = new TextField(fieldVO.getFieldType().getName(), fieldVO.getFieldValue(), Store.YES);
        document.add(field);
    }

    public List<Map<Object, Object>> getMapListByKeyType(String key, String type) {

        Criteria docIdCriteria = CriteriaPool.createCriteria(FieldOption.DOC_ID, key);
        Criteria typeCriteria = CriteriaPool.createCriteria(FieldOption.DOC_TYPE, type);
        Criteria searchCriterian = CriteriaPool.and(docIdCriteria, typeCriteria);
        List<DocumentVO> resultList = doSearch(searchCriterian);
        return StreamUtil.ofNullable(resultList)
                .map(hitDoc -> JsonUtil.fromJsonToMap(hitDoc.getContents().getFieldValue()))
                .collect(Collectors.toList());
    }


    public String getMergedTypeBySingleId(String docId, String... doctype) {

        List<String> docTypeList = Arrays.asList(doctype);

        Map<String, Object> collectMap = new HashMap<>();
        collectMap.put("COLLECT", combineToMap(docId, docTypeList));

        return JsonUtil.toJson(collectMap);
    }


    private Map<String, Object> combineToMap(String docId, List<String> docTypeList) {
        return StreamUtil.ofNullable(docTypeList)
                .map(type -> searchByDocId(docId, type))
                .filter(ObjectUtil::isNotEmpty)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k1, k2) -> k1));
    }

    private Entry<String, Object> searchByDocId(String docId, String type) {

        Criteria docIdCriteria = CriteriaPool.createCriteria(FieldOption.DOC_ID, docId);
        Criteria typeCriteria = CriteriaPool.createCriteria(FieldOption.DOC_TYPE, type);

        Criteria searchCriterian = CriteriaPool.and(docIdCriteria, typeCriteria);

        List<DocumentVO> resultList = doSearch(searchCriterian);

        List<Map<Object, Object>> contentList = StreamUtil.ofNullable(resultList)
                .map(hitDoc -> JsonUtil.fromJsonToMap(hitDoc.getContents().getFieldValue()))
                .collect(Collectors.toList());

        Map<String, Object> typeMap = new HashMap<>();
        typeMap.put(type, contentList);
        return typeMap.entrySet().iterator().next();
    }

    public List<String> listAllDocId(String docType) {
        List<DocumentVO> resultList = doSearch(CriteriaPool.createCriteria(FieldOption.DOC_TYPE, docType));
        return StreamUtil.ofNullable(resultList).map(d -> d.getDocId().getFieldValue()).collect(Collectors.toList());
    }
}
