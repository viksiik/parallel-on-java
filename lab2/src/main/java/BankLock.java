import java.util.concurrent.locks.*;

class BankLock extends Bank {
    private final Lock lock = new ReentrantLock();

    public BankLock(int n, int initialBalance) {
        super(n, initialBalance);
    }

    public void transfer(int from, int to, int amount) {
        lock.lock();
        try {
            if (ntransacts >= Main.MAX_TRANSACTIONS) return;
            if (accounts[from] < amount) return;
            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;
            test(from, to, amount);
        } finally {
            lock.unlock();
        }
    }
}