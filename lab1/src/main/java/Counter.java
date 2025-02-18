import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Counter {
    private int count = 0;
    private final Lock lock = new ReentrantLock();

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
        lock.lock();
        try {
            count++;
        } finally {
           lock.unlock();
        }
    }

    public void decrementWithLock() {
        lock.lock();
        try {
            count--;
        } finally {
            lock.unlock();
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