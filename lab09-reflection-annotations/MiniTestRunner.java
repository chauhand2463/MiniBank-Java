import java.lang.reflect.Method;

public class MiniTestRunner {

    public static void main(String[] args) throws Exception {
        CalculatorTest test = new CalculatorTest();
        int ran = 0;
        int passed = 0;

        for (Method m : CalculatorTest.class.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Run.class)) {
                ran++;
                try {
                    m.setAccessible(true);
                    m.invoke(test);
                    System.out.println("PASS " + m.getName());
                    passed++;
                } catch (Exception e) {
                    System.out.println("FAIL " + m.getName() + " -> " + e.getCause());
                }
            }
        }

        System.out.println("Total @Run methods ran: " + ran + ", passed: " + passed);
    }
}
