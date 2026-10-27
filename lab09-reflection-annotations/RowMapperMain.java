public class RowMapperMain {

    public static void main(String[] args) {
        String[] header = { "emp_name", "dept", "salary" };
        String[] row = { "Priya Nair", "Operations", "90000" };

        Employee employee = new Employee();
        java.util.List<String> warnings = RowMapper.populate(employee, header, row);

        System.out.println("Mapped object : " + employee);
        for (String warning : warnings) {
            System.out.println("Warning       : " + warning);
        }
    }
}
