package com.deltadc.iot.specification;

import com.deltadc.iot.model.entities.History;
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
public class HistorySpecification {


    public static Specification<History> withDynamicQuery(Map<String, String> criteria, Class<History> entityClass) {
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

    private static Predicate createPredicate(String key, String value, Root<History> root, CriteriaBuilder criteriaBuilder, Class<History> entityClass) {
        try {
            return createFieldPredicate(key, value, root, criteriaBuilder, entityClass);
        } catch (Exception e) {
            log.error("Error creating predicate for field: {}", key, e);
            return null;
        }
    }

    private static Predicate createFieldPredicate(String key, String value, Root<History> root, CriteriaBuilder criteriaBuilder, Class<History> entityClass){
        Field field = getField(entityClass, key);
        if (field != null) {
            field.setAccessible(true);
            if (isNumeric(value)) {
                return criteriaBuilder.equal(root.get(field.getName()), Double.valueOf(value));
            } else {
                return criteriaBuilder.like(root.get(field.getName()), "%" + value + "%");
            }
        } else {
            log.error("Field not found: {}", key);
            return null;
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
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
