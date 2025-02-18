class Counter {
    private int count = 0;
    private final Object lock = new Object();

    public synchronized void increment() {
        count++;
    }

    public synchronized void decrement() {
        count--;
    }

    public void incrementSyncBlock() {
        synchronized (this) {
            count++;
        }
    }

    public void decrementSyncBlock() {
        synchronized (this) {
            count--;
        }
    }

    public void incrementWithLock() {
        synchronized (lock) {
            count++;
        }
    }

    public void decrementWithLock() {
        synchronized (lock) {
            count--;
        }
    }

    public void incrementUnsynchronized() {
        count++;
    }

    public void decrementUnsynchronized() {
        count--;
    }

    public synchronized int getCount() {
        return count;
    }
}