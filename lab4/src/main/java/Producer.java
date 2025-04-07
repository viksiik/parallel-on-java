import java.util.Random;
import java.util.concurrent.BlockingQueue;

class Producer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final int[] data;

    public Producer(BlockingQueue<Integer> queue, int size) {
        this.queue = queue;
        this.data = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            data[i] = random.nextInt(100);
        }
    }

    @Override
    public void run() {
        try {
            for (int num : data) {
                queue.put(num);
                Thread.sleep(10);
            }
            queue.put(-1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}