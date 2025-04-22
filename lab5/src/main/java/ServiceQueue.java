import java.util.*;
import java.util.concurrent.*;

public class ServiceQueue {

    private static final int NUM_CHANNELS = 5;
    private static final int QUEUE_CAPACITY = 10;
    private static final int CLIENTS_PER_QUEUE = 1000;
    private static final int CLIENT_ARRIVAL_INTERVAL_MS = 20;
    private static final int MIN_SERVICE_TIME_MS = 50;
    private static final int MAX_SERVICE_TIME_MS = 200;

    private final int id;
    private final ExecutorService servicePool;
    private final Queue<Runnable> queue;
    private final Random random = new Random();
    private final int runId;


    private int rejectedClients = 0;
    private int totalQueueLength = 0;
    private int queueCheckCount = 0;
    private int servedClients = 0;

    private final CountDownLatch completionLatch;

    public ServiceQueue(int id, int runId) {
        this.id = id;
        this.runId = runId;
        this.queue = new LinkedList<>();
        this.servicePool = Executors.newFixedThreadPool(NUM_CHANNELS);
        this.completionLatch = new CountDownLatch(CLIENTS_PER_QUEUE);

        startMonitorThread();
    }

    public void start() {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < CLIENTS_PER_QUEUE; i++) {
                try {
                    Thread.sleep(CLIENT_ARRIVAL_INTERVAL_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                int serviceTime = random.nextInt(MAX_SERVICE_TIME_MS - MIN_SERVICE_TIME_MS + 1) + MIN_SERVICE_TIME_MS;
                Runnable task = createServiceTask(i, serviceTime);

                synchronized (queue) {
                    totalQueueLength += queue.size();
                    queueCheckCount++;

                    if (((ThreadPoolExecutor) servicePool).getActiveCount() < NUM_CHANNELS) {
                        servicePool.submit(task);
                    } else {
                        if (queue.size() < QUEUE_CAPACITY) {
                            queue.offer(task);
                        } else {
                            rejectedClients++;
                            completionLatch.countDown();
                        }
                    }
                }

                checkAndServeQueue();
            }
        });
        thread.start();
    }

    private Runnable createServiceTask(int clientId, int serviceTime) {
        return () -> {
            try {
                Thread.sleep(serviceTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                synchronized (this) {
                    servedClients++;
                }
                completionLatch.countDown();
                checkAndServeQueue();
            }
        };
    }

    private void checkAndServeQueue() {
        synchronized (queue) {
            while (!queue.isEmpty() && ((ThreadPoolExecutor) servicePool).getActiveCount() < NUM_CHANNELS) {
                Runnable task = queue.poll();
                if (task != null) {
                    servicePool.submit(task);
                }
            }
        }
    }

    public int getRejectedCount() {
        return rejectedClients;
    }

    public void awaitCompletion() throws InterruptedException {
        completionLatch.await();
        servicePool.shutdown();
        servicePool.awaitTermination(1, TimeUnit.MINUTES);
    }

    public void printStats() {
        double failureProb = (double) rejectedClients / CLIENTS_PER_QUEUE;
        double avgQueueLen = (double) totalQueueLength / Math.max(1, queueCheckCount);
        System.out.printf("Queue %d: Rejections = %d, Rejection probability = %.4f, Avg queue length = %.2f%n",
                id, rejectedClients, failureProb, avgQueueLen);
    }

    public double getFailureProbability() {
        return (double) rejectedClients / CLIENTS_PER_QUEUE;
    }

    public double getAverageQueueLength() {
        return (double) totalQueueLength / Math.max(1, queueCheckCount);
    }

    private void startMonitorThread() {
        Thread monitor = new Thread(() -> {
            while (completionLatch.getCount() > 0) {
                synchronized (queue) {
                    int queueSize = queue.size();
                    int active = ((ThreadPoolExecutor) servicePool).getActiveCount();
                    System.out.printf("[Queue %d in run %d] Active tasks: %d, In queue: %d, Rejections: %d, Served: %d%n",
                            id, runId, active, queueSize, rejectedClients, servedClients);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        monitor.setDaemon(true);
        monitor.start();
    }
}
