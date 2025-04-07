import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        int size = 100;
        int threads = 6;

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(size);
        ForkJoinPool pool = new ForkJoinPool(threads);

        long startTimeForkJoin = System.nanoTime();

        ForkJoinTask<Void> producerTask = new ProducerForkJoin(queue, size);
        ForkJoinTask<Void> consumerTask = new ConsumerForkJoin(queue);

        producerTask.fork();
        consumerTask.fork();

        producerTask.join();
        consumerTask.join();

        long endTimeForkJoin = System.nanoTime();
        long timeForkJoin = endTimeForkJoin - startTimeForkJoin;


        BlockingQueue<Integer> queue1 = new ArrayBlockingQueue<>(size);
        Thread producer = new Thread(new Producer(queue1, size));
        Thread consumer = new Thread(new Consumer(queue1));

        long startTimeRegular = System.nanoTime();
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long endTimeRegular = System.nanoTime();
        long timeRegular = endTimeRegular - startTimeRegular;


        long timeSequential = SequentialProducerConsumer.run(size);


        System.out.printf("Sequential Time: %.2f ms\n", timeSequential / 1e6);
        System.out.printf("Regular Time: %.2f ms\n", timeRegular / 1e6);
        System.out.printf("ForkJoin Time: %.2f ms\n", timeForkJoin / 1e6);


        double speedupThreads = (double) timeSequential / timeRegular;
        double speedupForkJoin = (double) timeSequential / timeForkJoin;

        double efficiencyThreads = speedupThreads / threads;
        double efficiencyForkJoin = speedupForkJoin / threads;

        System.out.printf("\nSpeedup (Threads): %.2f\n", speedupThreads);
        System.out.printf("Efficiency (Threads): %.2f\n", efficiencyThreads);

        System.out.printf("\nSpeedup (ForkJoin): %.2f\n", speedupForkJoin);
        System.out.printf("Efficiency (ForkJoin): %.2f\n", efficiencyForkJoin);
    }
}
