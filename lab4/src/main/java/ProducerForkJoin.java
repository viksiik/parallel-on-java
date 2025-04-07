import java.util.*;
import java.util.concurrent.*;

class ProducerForkJoin extends RecursiveTask<Void> {
    private final BlockingQueue<Integer> queue;
    private final int[] data;

    public ProducerForkJoin(BlockingQueue<Integer> queue, int size) {
        this.queue = queue;
        this.data = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            data[i] = random.nextInt(100);
        }
    }

    @Override
    protected Void compute() {
        try {
            for (int num : data) {
                queue.put(num);
                Thread.sleep(10);
            }
            queue.put(-1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return null;
    }
}