class BallThread extends Thread {
    private Ball ball;
    private BallCanvas canvas;
    private volatile int steps = 500;

    public BallThread(Ball ball, BallCanvas canvas) {
        this.ball = ball;
        this.canvas = canvas;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < steps; i++) {
                ball.move();
                System.out.println("Red ball moving, step: " + i);
                Thread.sleep(4);
            }
        } catch (InterruptedException ignored) {
        }
    }
}
