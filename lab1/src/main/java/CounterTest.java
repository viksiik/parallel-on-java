public class CounterTest {
    public static void main(String[] args) throws InterruptedException {
        Counter counter1 = new Counter();
        Counter counter2 = new Counter();
        Counter counter3 = new Counter();
        Counter counter4 = new Counter();

        int startCounter1 = counter1.getCount();
        int startCounter2 = counter2.getCount();
        int startCounter3 = counter3.getCount();
        int startCounter4 = counter4.getCount();

        Thread thread1i = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter1.increment();
            }
        });
        Thread thread1d = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter1.decrement();
            }
        });

        Thread thread2i = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter2.incrementSyncBlock();
            }
        });
        Thread thread2d = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter2.decrementSyncBlock();
            }
        });

        Thread thread3i = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter3.incrementWithLock();
            }
        });
        Thread thread3d = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter3.decrementWithLock();
            }
        });

        Thread thread4i = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter4.incrementUnsynchronized();
            }
        });
        Thread thread4d = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter4.decrementUnsynchronized();
            }
        });

        thread1i.start();
        thread1d.start();
        thread1i.join();
        thread1d.join();

        thread2i.start();
        thread2d.start();
        thread2i.join();
        thread2d.join();

        thread3i.start();
        thread3d.start();
        thread3i.join();
        thread3d.join();

        thread4i.start();
        thread4d.start();
        thread4i.join();
        thread4d.join();

        System.out.println("Initial counter value (synchronized method): " + startCounter1);
        System.out.println("Initial counter value (synchronized block): " + startCounter2);
        System.out.println("Initial counter value (lock object): " + startCounter3);
        System.out.println("Initial counter value (unsynchronized method): " + startCounter4);
        System.out.println("----------------------------------------------------------");
        System.out.println("Final counter value (synchronized method): " + counter1.getCount());
        System.out.println("Final counter value (synchronized block): " + counter2.getCount());
        System.out.println("Final counter value (lock object): " + counter3.getCount());
        System.out.println("Final counter value (unsynchronized method): " + counter4.getCount());
    }
}
