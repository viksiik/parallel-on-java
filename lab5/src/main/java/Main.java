import java.util.*;
import java.util.concurrent.*;

public class Main {

    private static final int NUM_PARALLEL_RUNS = 10;
    private static final int NUM_QUEUES = 10;
    private static final int CLIENTS_PER_QUEUE = 1000;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_PARALLEL_RUNS);
        List<Future<RunResult>> results = new ArrayList<>();

        long globalStart = System.currentTimeMillis();

        for (int i = 0; i < NUM_PARALLEL_RUNS; i++) {
            int runId = i;
            results.add(executor.submit(() -> runSimulation(runId)));
        }

        double totalFailureProb = 0;
        double totalAvgQueueLen = 0;
        int totalRequests = 0;
        int totalRejected = 0;

        for (Future<RunResult> resultFuture : results) {
            try {
                RunResult result = resultFuture.get();
                totalFailureProb += result.avgFailureProb;
                totalAvgQueueLen += result.avgQueueLen;
                totalRequests += result.totalRequests;
                totalRejected += result.totalRejected;
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        long globalEnd = System.currentTimeMillis();

        System.out.println("\n=== Overall Simulation Results ===");
        System.out.println("Total requests: " + totalRequests);
        System.out.println("Total rejections: " + totalRejected);
        System.out.printf("Average rejection probability: %.4f\n", totalFailureProb / NUM_PARALLEL_RUNS);
        System.out.printf("Average queue length: %.2f\n", totalAvgQueueLen / NUM_PARALLEL_RUNS);
        System.out.println("Total execution time: " + (globalEnd - globalStart) + " ms");
    }

    private static RunResult runSimulation(int runId) throws InterruptedException {
        List<ServiceQueue> serviceQueues = new ArrayList<>();

        for (int i = 0; i < NUM_QUEUES; i++) {
            ServiceQueue queue = new ServiceQueue(runId, i);
            serviceQueues.add(queue);
            queue.start();
        }


        for (ServiceQueue queue : serviceQueues) {
            queue.awaitCompletion();
        }

        double totalFailureProb = 0;
        double totalAvgQueueLen = 0;
        int totalRejected = 0;

        for (ServiceQueue queue : serviceQueues) {
            totalFailureProb += queue.getFailureProbability();
            totalAvgQueueLen += queue.getAverageQueueLength();
            totalRejected += queue.getRejectedCount();
        }

        double avgFailureProb = totalFailureProb / NUM_QUEUES;
        double avgQueueLen = totalAvgQueueLen / NUM_QUEUES;
        int totalRequests = CLIENTS_PER_QUEUE * NUM_QUEUES;

        System.out.printf("Run %d completed: Avg rejection = %.4f, Avg queue length = %.2f%n",
                runId, avgFailureProb, avgQueueLen);

        return new RunResult(avgFailureProb, avgQueueLen, totalRequests, totalRejected);
    }

    static class RunResult {
        double avgFailureProb;
        double avgQueueLen;
        int totalRequests;
        int totalRejected;

        RunResult(double avgFailureProb, double avgQueueLen, int totalRequests, int totalRejected) {
            this.avgFailureProb = avgFailureProb;
            this.avgQueueLen = avgQueueLen;
            this.totalRequests = totalRequests;
            this.totalRejected = totalRejected;
        }
    }
}
