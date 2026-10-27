import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RowMapper {

    public static List<String> populate(Object target, String[] header, String[] data) {
        List<String> warnings = new ArrayList<>();

        for (Field field : target.getClass().getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }

            int index = -1;
            for (int i = 0; i < header.length; i++) {
                if (header[i].equalsIgnoreCase(column.name())) {
                    index = i;
                    break;
                }
            }

            if (index == -1) {
                warnings.add("missing column '" + column.name() + "' for field "
                        + field.getName() + " -> left as default");
                continue;
            }

            if (index >= data.length) {
                warnings.add("row too short for column '" + column.name()
                        + "' -> left as default");
                continue;
            }

            String value = data[index];
            field.setAccessible(true);
            try {
                if (field.getType() == long.class) {
                    field.setLong(target, Long.parseLong(value));
                } else {
                    field.set(target, value);
                }
            } catch (Exception e) {
                warnings.add("cannot convert '" + value + "' for field " + field.getName()
                        + " -> left as default");
            }
        }

        return warnings;
    }
}
