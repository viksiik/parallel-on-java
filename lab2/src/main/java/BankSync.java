class BankSync extends Bank {
    public BankSync(int n, int initialBalance) {
        super(n, initialBalance);
    }

    public synchronized void transfer(int from, int to, int amount) {
        if (ntransacts >= Main.MAX_TRANSACTIONS) return;
        if (accounts[from] < amount) return;
        accounts[from] -= amount;
        accounts[to] += amount;
        ntransacts++;
        test(from, to, amount);
    }
}