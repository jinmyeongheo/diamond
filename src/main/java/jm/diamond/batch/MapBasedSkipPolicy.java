package jm.diamond.batch;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.boot.json.JsonParseException;

import java.util.HashMap;
import java.util.Map;

public class MapBasedSkipPolicy implements SkipPolicy {
    private final Map<Class<? extends Throwable>, Integer> limits = new HashMap<>();

    public MapBasedSkipPolicy() {
        limits.put(JsonParseException.class, 100);
        limits.put(ConstraintViolationException.class, 10);
    }

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) {
        return limits.entrySet().stream()
                .anyMatch(e -> e.getKey().isAssignableFrom(t.getClass())
                        && skipCount < e.getValue());
    }
}

