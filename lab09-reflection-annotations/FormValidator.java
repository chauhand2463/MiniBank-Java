import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FormValidator {

    public static List<String> validate(Object form) {
        List<String> errors = new ArrayList<>();
        for (Field field : form.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(form);
            } catch (IllegalAccessException e) {
                continue;
            }
            String text = value == null ? "" : value.toString();

            NotBlank notBlank = field.getAnnotation(NotBlank.class);
            if (notBlank != null && text.isBlank()) {
                errors.add(field.getName() + " must not be blank");
            }

            MaxLength maxLength = field.getAnnotation(MaxLength.class);
            if (maxLength != null && text.length() > maxLength.value()) {
                errors.add(field.getName() + " must be at most " + maxLength.value()
                        + " characters (was " + text.length() + ")");
            }
        }
        return errors;
    }
}
