import javax.swing.*;
import java.awt.*;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 500;
    public static final int HEIGHT = 400;

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");

        this.canvas = new BallCanvas();
        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");

        buttonStart.addActionListener(e -> startBalls());

        buttonStop.addActionListener(e -> System.exit(0));

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        content.add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void startBalls() {
        Ball redBall = new Ball(canvas, Color.RED);
        canvas.add(redBall);
        BallThread redThread = new BallThread(redBall, canvas, Thread.MAX_PRIORITY);
        redThread.start();

        for (int i = 0; i < 1000; i++) {
            Ball blueBall = new Ball(canvas, Color.BLUE);
            canvas.add(blueBall);
            BallThread blueThread = new BallThread(blueBall, canvas, Thread.MIN_PRIORITY);
            blueThread.start();
        }
    }

}
