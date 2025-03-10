class SymbolPrinter implements Runnable {
    private final char symbol;
    private static final Object lock = new Object();
    private static int turn = 0;
    private final int threadId;
    private final int totalTurns;
    private final boolean synchronizedMode;

    public SymbolPrinter(char symbol, int threadId, int totalTurns, boolean synchronizedMode) {
        this.symbol = symbol;
        this.threadId = threadId;
        this.totalTurns = totalTurns;
        this.synchronizedMode = synchronizedMode;
    }

    @Override
    public void run() {
        if (!synchronizedMode) {
            for (int i = 0; i < totalTurns; i++) {
                System.out.print(symbol);
                if ((i + 1) % 30 == 0) System.out.println();
            }
        } else {
            for (int i = 0; i < totalTurns; i++) {
                synchronized (lock) {
                    while (turn % 3 != threadId) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.print(symbol);
                    turn++;
                    if (turn % 30 == 0) System.out.println();
                    lock.notifyAll();
                }
            }
        }
    }
}