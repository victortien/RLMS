package org.rlms.lucene.criteria;

import java.util.ArrayList;
import java.util.List;

import org.rlms.lucene.vo.FieldVO;

public class Criteria {

    private CriteriaType criteriaType;

    private Criteria parentCriteria;

    private List<Criteria> childCriteria = new ArrayList<>();

    private FieldVO fieldCondition;

    private Criteria(CriteriaType criteriaType) {
        this.criteriaType = criteriaType;
    }

    public static Criteria type(CriteriaType criteriaType) {
        return new Criteria(criteriaType);
    }

    protected Criteria getParentCriteria() {
        return parentCriteria;
    }

    protected void setParentCriteria(Criteria parentCriteria) {
        this.parentCriteria = parentCriteria;
    }

    public List<Criteria> getChildCriteria() {
        return childCriteria;
    }

    protected void addChildCriteria(Criteria criteria) {
        this.childCriteria.add(criteria);
    }

    protected CriteriaType getCriteriaType() {
        return criteriaType;
    }

    protected void setCriteriaType(CriteriaType criteriaType) {
        this.criteriaType = criteriaType;
    }

    public FieldVO getFieldCondition() {
        return fieldCondition;
    }

    public void setFieldCondition(FieldVO fieldCondition) {
        this.fieldCondition = fieldCondition;
    }
}
