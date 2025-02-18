public class BallThread extends Thread {
    private Ball ball;
    private BallCanvas canvas;
    private volatile boolean running = true;

    public BallThread(Ball ball, BallCanvas canvas, int priority) {
        this.ball = ball;
        this.canvas = canvas;
        this.setPriority(priority);
    }

    @Override
    public void run() {
        try {
            while (running) {
                ball.move();
                Thread.sleep(4);
            }
        } catch (InterruptedException ex) {
            System.out.println("Thread " + getName() + " was interrupted.");
        }
    }
}
