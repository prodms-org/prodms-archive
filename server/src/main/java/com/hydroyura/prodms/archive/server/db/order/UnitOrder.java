package com.hydroyura.prodms.archive.server.db.order;

import com.hydroyura.prodms.archive.server.db.entity.Unit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import java.util.function.Function;
import lombok.Getter;


public enum UnitOrder {

    NUMBER_ASC(0, "number", cb -> cb::asc),
    NUMBER_DESC(1, "number", cb -> cb::desc),
    NAME_ASC(2, "name", cb -> cb::asc),
    NAME_DESC(3, "name", cb -> cb::desc);


    UnitOrder(Integer code,
              String field,
              Function<CriteriaBuilder, Function<Expression<?>, Order>> functionGetExpression) {
        this.code = code;
        this.field = field;
        this.functionGetExpression = functionGetExpression;
    }


    @Getter
    private final Integer code;

    private final String field;
    private final Function<Root<Unit>, Path<String>> functionGetField = r -> r.get(this.getField());
    private final Function<CriteriaBuilder, Function<Expression<?>, Order>> functionGetExpression;

    private String getField() {
        return this.field;
    }

    public Order calcOrder(Root<Unit> r, CriteriaBuilder cb) {
        return this.functionGetExpression
            .apply(cb)
            .apply(this.functionGetField.apply(r));
    }
}
