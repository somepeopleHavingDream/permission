package org.yangxin.permission.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.yangxin.permission.exception.ParamException;


/**
 * @author yangxin
 * 2019/09/02 16:29
 */
public class BeanValidator {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private static <T> Map<Object, Object> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap<Object, Object> errorMap = Maps.newLinkedHashMap();
            for (ConstraintViolation<T> constraintViolation : validateResult) {
                errorMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
            return errorMap;
        }
    }

    private static Map<Object, Object> validateList(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator<?> iterator = collection.iterator();
        Map errorMap;

        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object next = iterator.next();
            errorMap = validate(next);
        } while (errorMap.isEmpty());

        return errorMap;
    }

    private static Map<Object, Object> validateObject(Object first, Object... objects) {
        return objects != null && objects.length > 0
                ? validateList(Lists.asList(first, objects))
                : validate(first);
    }

    public static void check(Object param) {
        Map<Object, Object> map = BeanValidator.validateObject(param);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
    }
}
