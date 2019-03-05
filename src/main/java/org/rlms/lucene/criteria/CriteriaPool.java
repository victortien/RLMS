package org.rlms.lucene.criteria;

import org.rlms.lucene.core.FieldOption;
import org.rlms.lucene.vo.FieldVO;

public class CriteriaPool {

    private CriteriaPool() {}

    public static Criteria createCriteria(FieldOption fieldOption, String content) {
        Criteria criteria = Criteria.type(CriteriaType.LEAF);
        FieldVO fieldVO = new FieldVO();
        fieldVO.setFieldType(fieldOption);
        fieldVO.setFieldValue(content);
        criteria.setFieldCondition(fieldVO);
        return criteria;
    }

    public static Criteria and(Criteria... allCriteria) {
        Criteria andCriteria = Criteria.type(CriteriaType.AND);
        for(Criteria criteria : allCriteria) {
            andCriteria.addChildCriteria(criteria);
        }
        return andCriteria;
    }

    public static Criteria or(Criteria... allCriteria) {
        Criteria orCriteria = Criteria.type(CriteriaType.OR);
        for(Criteria criteria : allCriteria) {
            orCriteria.addChildCriteria(criteria);
        }
        return orCriteria;
    }

}
