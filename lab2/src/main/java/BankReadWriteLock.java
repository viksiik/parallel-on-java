import java.util.concurrent.locks.*;

class BankReadWriteLock extends Bank {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock writeLock = rwLock.writeLock();

    public BankReadWriteLock(int n, int initialBalance) {
        super(n, initialBalance);
    }

    public void transfer(int from, int to, int amount) {
        writeLock.lock();
        try {
            if (ntransacts >= Main.MAX_TRANSACTIONS) return;
            if (accounts[from] < amount) return;
            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;
            test(from, to, amount);
        } finally {
            writeLock.unlock();
        }
    }
}