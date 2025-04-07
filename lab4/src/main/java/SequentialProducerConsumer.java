import java.util.Random;

class SequentialProducerConsumer {
    public static long run(int size) {
        int[] data = new int[size];
        Random random = new Random();

        long startTime = System.nanoTime();

        for (int i = 0; i < size; i++) {
            data[i] = random.nextInt(100);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        for (int num : data) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long endTime = System.nanoTime();
        long timeMillis = (endTime - startTime);

        return timeMillis;
    }
}
