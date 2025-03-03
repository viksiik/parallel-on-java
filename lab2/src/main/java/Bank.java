abstract class Bank {
    protected final int[] accounts;
    protected long ntransacts = 0;

    public Bank(int n, int initialBalance) {
        accounts = new int[n];
        for (int i = 0; i < accounts.length; i++)
            accounts[i] = initialBalance;
    }

    public abstract void transfer(int from, int to, int amount);

    protected void test(int from, int to, int amount) {
        int sum = 0;
        for (int account : accounts) sum += account;
        if (shouldPrint(ntransacts)) {
            System.out.println("Transaction #" + ntransacts + ":\n" +
                    "   From account " + from + " sent " + amount + ", to account " + to +
                    ". Total sum: " + sum);
        }
    }

    private boolean shouldPrint(long n) {
        return n <= 10 || n == 15 || n == 25 || n == 40 || n == 100 || n == 200 ||
                n == 500 || n == 1000 || n == 5000 || n == 10000;
    }

    public int size() {
        return accounts.length;
    }
}