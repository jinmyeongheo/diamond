package jm.diamond.batch.reader.modn.reader.options;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import jm.diamond.batch.reader.modn.reader.expression.Expression;

import javax.annotation.Nonnull;
import java.util.List;

public class QuerydslNoOffsetStringOptions<T> extends QuerydslNoOffsetOptions<T> {

    private String currentId;

    private final StringPath field;

    /**
     * 엔티티를 직접 조회할 때 사용하는 기존 생성자
     */
    public QuerydslNoOffsetStringOptions(@Nonnull StringPath field,
                                         @Nonnull Expression expression) {
        super(field, expression);
        this.field = field;
    }

    /**
     * DTO 프로젝션을 위해 새로 추가한 생성자
     * @param dtoFieldPath DTO 객체의 필드 경로 (예: "paymentBaseInfo.order.seq")
     * @param queryFieldPath 쿼리 생성을 위한 QueryDSL Path 객체
     * @param expression 정렬 방향
     */
    public QuerydslNoOffsetStringOptions(@Nonnull String dtoFieldPath,
                                         @Nonnull StringPath queryFieldPath,
                                         @Nonnull Expression expression) {
        super(dtoFieldPath, queryFieldPath, expression);
        this.field = queryFieldPath;
    }

    @Override
    public void initFirstId(JPAQuery<T> query, int page) {
        if (page == 0) {
            List<String> fetch = query
                    .select(selectFirstId())
                    .fetch();
            int size = fetch.size();
            if (size > 0) {
                int index = expression.isAsc() ? 0 : size - 1;
                currentId = fetch.get(index);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("First Select Key= " + currentId);
            }
        }
    }

    private StringExpression selectFirstId() {
        if (expression.isAsc()) {
            return field.min();
        }

        return field.max();
    }

    @Override
    public JPAQuery<T> createQuery(JPAQuery<T> query, int page) {
        if (currentId == null) {
            return query;
        }

        return query
                .where(whereExpression(page))
                .orderBy(orderExpression());
    }

    private BooleanExpression whereExpression(int page) {
        return expression.where(field, page, currentId);
    }

    private OrderSpecifier<String> orderExpression() {
        return expression.order(field);
    }

    @Override
    public void resetCurrentId(T item) {
        // 부모 클래스의 수정된 getFiledValue를 호출하여 정상적으로 동작합니다.
        currentId = (String) getFiledValue(item);
        if (logger.isDebugEnabled()) {
            logger.debug("Current Select Key= " + currentId);
        }
    }
}