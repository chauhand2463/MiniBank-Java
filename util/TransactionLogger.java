package util;

public class TransactionLogger implements AutoCloseable {

    private final String name;

    public TransactionLogger(String name) {
        this.name = name;
        System.out.println("[LOG] opened " + name);
    }

    public void log(String message) {
        System.out.println("[LOG] " + message);
    }

    @Override
    public void close() {
        System.out.println("[LOG] closed " + name);
    }
}
