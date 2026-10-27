public class Employee {

    @Column(name = "emp_name")
    private String name;

    @Column(name = "dept")
    private String department;

    @Column(name = "salary")
    private long salary;

    @Column(name = "grade")
    private String grade;

    @Override
    public String toString() {
        return "Employee [name=" + name + ", department=" + department
                + ", salary=" + salary + ", grade=" + grade + "]";
    }
}
