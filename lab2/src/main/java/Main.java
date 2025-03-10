import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the array size (100, 1000, or 5000): ");
        int size = scanner.nextInt();
        scanner.close();

        if (size != 100 && size != 1000 && size != 5000) {
            System.out.println("Invalid size. Please choose 100, 1000, or 5000.");
            return;
        }

        System.out.println("\nTesting with array size: " + size);
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(size);
        Thread producer = new Thread(new Producer(queue, size));
        Thread consumer = new Thread(new Consumer(queue));

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Test with size " + size + " completed.\n");
    }
}
