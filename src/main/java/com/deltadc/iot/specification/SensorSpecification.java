package com.deltadc.iot.specification;

import com.deltadc.iot.model.entities.sensor.Sensor;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class SensorSpecification {

    public static Specification<Sensor> withDynamicQuery(Map<String, String> criteria, Class<Sensor> entityClass) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (criteria != null) {
                criteria.forEach((key, value) -> {
                    Predicate predicate = createPredicate(key, value, root, criteriaBuilder, entityClass);
                    if (predicate != null) {
                        predicates.add(predicate);
                    }
                });
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate createPredicate(String key, String value, Root<Sensor> root, CriteriaBuilder criteriaBuilder, Class<Sensor> entityClass) {
        try {
            if (isJsonField(key)) {
                return createJsonPredicate(key, value, root, criteriaBuilder);
            } else {
                return createFieldPredicate(key, value, root, criteriaBuilder, entityClass);
            }
        } catch (Exception e) {
            log.error("Error creating predicate for field: {}", key, e);
            return null;
        }
    }

    private static boolean isJsonField(String key) {
        return key.contains(".") || "temperature".equals(key) || "humidity".equals(key) || "brightness".equals(key);
    }

    private static Predicate createJsonPredicate(String key, String value, Root<Sensor> root, CriteriaBuilder criteriaBuilder) {
        String jsonPath = key.contains(".") ? key.replace(".", ".") : "$." + key + ".value";
        return criteriaBuilder.like(
                criteriaBuilder.function("json_extract", String.class, root.get("data"), criteriaBuilder.literal(jsonPath)),
                "%" + value + "%"
        );
    }

    private static Predicate createFieldPredicate(String key, String value, Root<Sensor> root, CriteriaBuilder criteriaBuilder, Class<Sensor> entityClass) throws NoSuchFieldException {
        Field field = getField(entityClass, key);
        if (field != null) {
            field.setAccessible(true);
            if ("id".equals(key)) {
                return criteriaBuilder.equal(root.get(field.getName()), Long.valueOf(value));
            } else {
                return criteriaBuilder.like(root.get(field.getName()), "%" + value + "%");
            }
        } else {
            log.error("Field not found: {}", key);
            return null;
        }
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), fieldName);
            }
        }
        return null;
    }
}
