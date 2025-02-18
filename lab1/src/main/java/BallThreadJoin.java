class BallThreadJoin extends Thread {
    private Ball ball;
    private BallCanvas canvas;
    private volatile int steps = 500;
    private final BallThread thread;

    public BallThreadJoin(Ball ball, BallCanvas canvas, BallThread thread) {
        this.ball = ball;
        this.canvas = canvas;
        this.thread = thread;
    }

    @Override
    public void run() {
        try {
            thread.join();
            for (int i = 0; i < steps; i++) {
                ball.move();
                System.out.println("Blue ball moving, step: " + i);
                Thread.sleep(4);
            }
        } catch (InterruptedException ignored) {
        }
    }
}
