public class Main {
    public static void main(String[] args) {
        int lines = 30;
        int repetitionsPerLine = 30;
        int unsynchronizedLines = 15;
        int synchronizedLines = lines - unsynchronizedLines;

        System.out.println("\nWithout synchronized method:\n");
        Thread t1 = new Thread(new SymbolPrinter('|', 0, unsynchronizedLines * repetitionsPerLine / 3, false));
        Thread t2 = new Thread(new SymbolPrinter('\\', 1, unsynchronizedLines * repetitionsPerLine / 3, false));
        Thread t3 = new Thread(new SymbolPrinter('/', 2, unsynchronizedLines * repetitionsPerLine / 3, false));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\nWith synchronized method:\n");
        Thread s1 = new Thread(new SymbolPrinter('|', 0, synchronizedLines * repetitionsPerLine / 3, true));
        Thread s2 = new Thread(new SymbolPrinter('\\', 1, synchronizedLines * repetitionsPerLine / 3, true));
        Thread s3 = new Thread(new SymbolPrinter('/', 2, synchronizedLines * repetitionsPerLine / 3, true));

        s1.start();
        s2.start();
        s3.start();

        try {
            s1.join();
            s2.join();
            s3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}