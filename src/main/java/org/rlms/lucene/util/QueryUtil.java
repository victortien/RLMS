package org.rlms.lucene.util;

import java.util.EnumMap;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.rlms.common.exception.BaseException;
import org.rlms.lucene.criteria.Criteria;
import org.rlms.lucene.criteria.CriteriaParser;
import org.rlms.lucene.criteria.CriteriaType;
import org.rlms.lucene.vo.FieldVO;

public class QueryUtil {

    private QueryUtil(){
    }

    public static Query paresQuery(FieldVO searchContent) {
        try {
            QueryParser queryParser = new QueryParser(searchContent.getFieldType().getName(), new StandardAnalyzer());
            return queryParser.parse(searchContent.getFieldValue());
        } catch (ParseException e) {
            throw new BaseException(e);
        }
    }

    public static Query paresQuery(Criteria searchCriterian) {
        CriteriaParser parser = new CriteriaParser(searchCriterian);
        return parser.paresCriteria();
    }

    public static Occur getOccur(CriteriaType criteriaType) {
        EnumMap<CriteriaType, Occur> moodMap = new EnumMap<>(CriteriaType.class);
        moodMap.put(CriteriaType.AND, Occur.MUST);
        moodMap.put(CriteriaType.OR, Occur.SHOULD);
        return moodMap.get(criteriaType);
    }

}
