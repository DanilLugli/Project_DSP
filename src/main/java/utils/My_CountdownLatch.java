package utils;

public class My_CountdownLatch {
    private int count;

    public My_CountdownLatch(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }
        this.count = count;
    }

    public synchronized void await() throws InterruptedException {
        while (count > 0) {
            wait();
        }
    }

    public synchronized void countDown() {
        if (count > 0) {
            count--;
            if (count == 0) {
                notifyAll();
            }
        }
    }

    public synchronized int getCount() {
        return count;
    }
}
