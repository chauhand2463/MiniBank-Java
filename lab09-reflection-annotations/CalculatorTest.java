public class CalculatorTest {

    @Run
    void testAddition() {
        if (2 + 3 != 5) {
            throw new AssertionError("Expected 5 for 2 + 3");
        }
    }

    @Run
    void testSubtraction() {
        if (10 - 4 != 6) {
            throw new AssertionError("Expected 6 for 10 - 4");
        }
    }

    @Run
    void testMultiplication() {
        if (7 * 6 != 42) {
            throw new AssertionError("Expected 42 for 7 * 6");
        }
    }

    void utilityHelper() {
    }
}
