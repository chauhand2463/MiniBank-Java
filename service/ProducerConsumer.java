package service;

public class ProducerConsumer {

    private final long[] buffer = new long[5];
    private int head = 0;
    private int tail = 0;
    private int count = 0;

    public synchronized void produce(long amount) throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }
        buffer[tail] = amount;
        tail = (tail + 1) % buffer.length;
        count++;
        notifyAll();
    }

    public synchronized long consume() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        long amount = buffer[head];
        head = (head + 1) % buffer.length;
        count--;
        notifyAll();
        return amount;
    }
}
