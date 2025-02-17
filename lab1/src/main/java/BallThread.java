public class BallThread extends Thread {
    private Ball ball;
    private BallCanvas canvas;
    private volatile boolean running = true;

    public BallThread(Ball ball, BallCanvas canvas) {
        this.ball = ball;
        this.canvas = canvas;
    }

    @Override
    public void run() {
        try {
            while (running) {
                ball.move(canvas.getHoles());

                if (ball.isInHole(canvas.getHoles())) {
                    running = false;
                    canvas.removeBall(ball);
                    System.out.println("Thread " + getName() + " stopped.");
                }

                System.out.println("Thread " + getName() + " is running...");
                Thread.sleep(5);
            }
        } catch (InterruptedException ex) {
            System.out.println("Thread " + getName() + " was interrupted.");
        }
    }
}
