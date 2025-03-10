import java.util.concurrent.BlockingQueue;

class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int num = queue.take();
                if (num == -1) {
                    break;
                }
                System.out.println("Consumed: " + num);
                Thread.sleep(15);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}