import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class BallCanvas extends JPanel {
    private final ArrayList<Ball> balls;

    public BallCanvas() {
        this.balls = new ArrayList<>();
    }

    public void add(Ball b) {
        balls.add(b);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Ball b : balls) {
            b.draw(g2);
        }
    }
}
