import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 500;
    public static final int HEIGHT = 400;

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");

        JLabel scoreLabel = new JLabel("Balls in hole: 0");
        List<Hole> holes = new ArrayList<>();

        this.canvas = new BallCanvas(scoreLabel, holes);

        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");

        buttonStart.addActionListener(e -> {
            Ball b = new Ball(canvas);
            canvas.add(b);
            BallThread thread = new BallThread(b, canvas);
            thread.start();
        });

        buttonStop.addActionListener(e -> System.exit(0));

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        buttonPanel.add(scoreLabel);
        content.add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        initializeHoles(holes);
    }

    private void initializeHoles(List<Hole> holes) {
        int holeSize = Hole.getSize();
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        holes.clear();
        holes.add(new Hole(0, 0));
        holes.add(new Hole(width - holeSize, 0));
        holes.add(new Hole(0, height - holeSize));
        holes.add(new Hole(width - holeSize, height - holeSize));

        canvas.repaint();
    }
}
