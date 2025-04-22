import java.util.*;

public class Main {

    private static final int NUM_QUEUES = 10;
    private static final int CLIENTS_PER_QUEUE = 1000;
    static double totalFailureProb = 0;
    static double totalAvgQueueLen = 0;
    static int totalRequests = 0;
    static int totalRejected = 0;

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        List<ServiceQueue> serviceQueues = new ArrayList<>();

        for (int i = 0; i < NUM_QUEUES; i++) {
            ServiceQueue queue = new ServiceQueue(i);
            serviceQueues.add(queue);
            queue.start();
        }

        for (ServiceQueue queue : serviceQueues) {
            queue.awaitCompletion();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("\n=== Queue results ===");

        for (ServiceQueue queue : serviceQueues) {
            queue.printStats();
            totalFailureProb += queue.getFailureProbability();
            totalAvgQueueLen += queue.getAverageQueueLength();
            totalRequests += CLIENTS_PER_QUEUE;
            totalRejected += queue.getRejectedCount();
        }

        double avgFailureProb = totalFailureProb / NUM_QUEUES;
        double avgQueueLength = totalAvgQueueLen / NUM_QUEUES;

        System.out.println("\n=== General results ===");
        System.out.println("General number of requests: " + totalRequests);
        System.out.println("General number of rejections: " + totalRejected);
        System.out.printf("Avg rejection probability: %.4f\n", avgFailureProb);
        System.out.printf("Avg queue length: %.2f\n", avgQueueLength);

        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }
}