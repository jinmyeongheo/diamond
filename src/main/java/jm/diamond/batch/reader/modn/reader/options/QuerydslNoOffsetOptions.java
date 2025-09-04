package jm.diamond.batch.reader.modn.reader.options;

import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQuery;
import jm.diamond.batch.reader.modn.reader.expression.Expression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

public abstract class QuerydslNoOffsetOptions<T> {
    protected Log logger = LogFactory.getLog(getClass());

    // 쿼리 생성을 위한 QueryDSL Path 객체
    protected final Path<?> queryFieldPath;

    // DTO 필드 접근을 위한 문자열 경로 (새로 추가)
    protected final String dtoFieldPath;

    protected final Expression expression;

    /**
     * 엔티티를 직접 조회할 때 사용하는 기존 생성자
     */
    public QuerydslNoOffsetOptions(@Nonnull Path<?> field,
                                   @Nonnull Expression expression) {
        this.queryFieldPath = field;
        this.dtoFieldPath = null; // DTO 경로가 없으므로 null로 설정
        this.expression = expression;
    }

    /**
     * DTO 프로젝션을 사용할 때를 위한 새로운 생성자
     * @param dtoFieldPath DTO 객체의 필드 경로 (예: "paymentBaseInfo.order.seq")
     * @param queryFieldPath 쿼리 생성을 위한 QueryDSL Path 객체
     * @param expression 정렬 방향
     */
    public QuerydslNoOffsetOptions(@Nonnull String dtoFieldPath,
                                   @Nonnull Path<?> queryFieldPath,
                                   @Nonnull Expression expression) {
        this.dtoFieldPath = dtoFieldPath;
        this.queryFieldPath = queryFieldPath;
        this.expression = expression;
    }

    public abstract void initFirstId(JPAQuery<T> query, int page);

    public abstract JPAQuery<T> createQuery(JPAQuery<T> query, int page);

    public abstract void resetCurrentId(T item);

    /**
     * DTO의 중첩된 필드 값을 재귀적으로 찾아오는 로직으로 완전히 교체
     */
    protected Object getFiledValue(T item) {
        // DTO 경로가 지정되었다면 그 경로를 사용하고, 아니면 기존처럼 Path 객체의 단순 이름을 사용
        String pathToTraverse = this.dtoFieldPath != null ? this.dtoFieldPath : this.queryFieldPath.getMetadata().getName();

        try {
            String[] fields = pathToTraverse.split("\\.");
            Object currentValue = item;

            for (String fieldName : fields) {
                if (currentValue == null) {
                    throw new IllegalStateException("Cannot access field '" + fieldName + "' on a null object while traversing path '" + pathToTraverse + "'");
                }
                Field field = ReflectionUtils.findField(currentValue.getClass(), fieldName);
                if (field == null) {
                    throw new NoSuchFieldException("Field '" + fieldName + "' not found in class " + currentValue.getClass().getName());
                }
                ReflectionUtils.makeAccessible(field);
                currentValue = field.get(currentValue);
            }
            return currentValue;

        } catch (Exception e) {
            logger.error("Failed to access field path: " + pathToTraverse, e);
            throw new IllegalArgumentException("Not Found or Not Access Field: " + pathToTraverse, e);
        }
    }
}