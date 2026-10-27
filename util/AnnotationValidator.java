package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import model.annotation.MaxLength;
import model.annotation.Positive;

public class AnnotationValidator {

    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();
        Class<?> type = obj.getClass();

        while (type != null && type != Object.class) {
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(obj);
                } catch (IllegalAccessException e) {
                    continue;
                }

                if (field.isAnnotationPresent(Positive.class)
                        && value instanceof Number number && number.longValue() <= 0) {
                    errors.add(field.getName() + " " + field.getAnnotation(Positive.class).message());
                }

                if (field.isAnnotationPresent(MaxLength.class) && value != null) {
                    int max = field.getAnnotation(MaxLength.class).value();
                    if (value.toString().length() > max) {
                        errors.add(field.getName() + " must be at most " + max + " characters");
                    }
                }
            }
            type = type.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}
