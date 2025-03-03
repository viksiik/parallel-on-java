import java.util.Scanner;

public class Main {
    public static final int NACCOUNTS = 10;
    public static final int INITIAL_BALANCE = 10000;
    public static final int MAX_TRANSACTIONS = 10000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select synchronization method:");
        System.out.println("1 - synchronized method");
        System.out.println("2 - ReentrantLock");
        System.out.println("3 - ReadWriteLock");
        int choice = scanner.nextInt();
        scanner.close();

        Bank bank;
        switch (choice) {
            case 1:
                bank = new BankSync(NACCOUNTS, INITIAL_BALANCE);
                break;
            case 2:
                bank = new BankLock(NACCOUNTS, INITIAL_BALANCE);
                break;
            case 3:
                bank = new BankReadWriteLock(NACCOUNTS, INITIAL_BALANCE);
                break;
            default:
                System.out.println("Invalid choice. Defaulting to synchronized method.");
                bank = new BankSync(NACCOUNTS, INITIAL_BALANCE);
        }

        for (int i = 0; i < NACCOUNTS; i++) {
            TransferThread t = new TransferThread(bank, i, INITIAL_BALANCE);
            t.setPriority(Thread.NORM_PRIORITY + i % 2);
            t.start();
        }
    }
}