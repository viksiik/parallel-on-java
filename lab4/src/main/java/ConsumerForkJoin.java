import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RecursiveTask;

class ConsumerForkJoin extends RecursiveTask<Void> {
    private final BlockingQueue<Integer> queue;

    public ConsumerForkJoin(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    protected Void compute() {
        try {
            while (true) {
                int num = queue.take();
                if (num == -1) {
                    break;
                }
                Thread.sleep(15);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return null;
    }
}