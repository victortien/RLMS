package org.rlms.lucene.criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.rlms.common.exception.BaseException;
import org.rlms.lucene.util.QueryUtil;
import org.rlms.lucene.vo.FieldVO;

@SuppressWarnings("unchecked")
public class CriteriaParser {

    private Criteria searchCriterian;

    private MultiValueMap criteriaQueryMap;

    public CriteriaParser(Criteria searchCriterian) {
        this.searchCriterian = searchCriterian;
        this.criteriaQueryMap = new MultiValueMap();
    }

    public Query paresCriteria() {
        List<Criteria> criteriaList = getCriteriaList(searchCriterian);

        Criteria rootCriteria = null;

        for(Criteria currCriteria : criteriaList) {

            if(CriteriaType.ROOT.equals(currCriteria.getParentCriteria().getCriteriaType())) {
                rootCriteria = currCriteria.getParentCriteria();
            }

            Query currQuery = CriteriaType.LEAF.equals(currCriteria.getCriteriaType())
                    ? getLeafCriteriaQuery(currCriteria) : getBooleanCriteriaQuery(currCriteria);

            criteriaQueryMap.put(currCriteria.getParentCriteria(), currQuery);
        }

        Collection<Query> rootQuery = criteriaQueryMap.getCollection(rootCriteria);
        return rootQuery.iterator().next();
    }

    private List<Criteria> getCriteriaList(Criteria searchCriterian) {
        List<Criteria> iterateResult = new ArrayList<>();

        iterateCriteria(searchCriterian, Criteria.type(CriteriaType.ROOT), iterateResult);
        Collections.reverse(iterateResult);

        return iterateResult;
    }

    private void iterateCriteria(Criteria criteria, Criteria parentCriteria, List<Criteria> iterateResult) {
        criteria.setParentCriteria(parentCriteria);

        iterateResult.add(criteria);

        if(CriteriaType.LEAF.equals(criteria.getCriteriaType()))
            return;

        for(Criteria subCriteria : criteria.getChildCriteria()) {
            iterateCriteria(subCriteria, criteria, iterateResult);
        }
    }

    private Query getLeafCriteriaQuery(Criteria currCriteria) {
        try {
            FieldVO fieldCondition = currCriteria.getFieldCondition();
            QueryParser queryParser = new QueryParser(fieldCondition.getFieldType().getName(), new StandardAnalyzer());
            return queryParser.parse(fieldCondition.getFieldValue());
        } catch (Exception e) {
            throw new BaseException(e);
        }
    }

    private Query getBooleanCriteriaQuery(Criteria currCriteria) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        Collection<Query> subQuery = criteriaQueryMap.getCollection(currCriteria);
        for(Query query : subQuery) {
            builder.add(query, QueryUtil.getOccur(currCriteria.getCriteriaType()));
        }
        return builder.build();
    }

}
