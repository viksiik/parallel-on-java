import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class BallCanvas extends JPanel {
    private List<Ball> balls;
    private List<Hole> holes;
    private JLabel scoreLabel;
    private int score = 0;

    public BallCanvas(JLabel scoreLabel, List<Hole> holes) {
        this.scoreLabel = scoreLabel;
        this.balls = new java.util.ArrayList<>();
        this.holes = holes;
    }

    public List<Hole> getHoles() {
        return holes;
    }

    public void add(Ball b) {
        balls.add(b);
        repaint();
    }

    public void removeBall(Ball b) {
        balls.remove(b);
        score++;
        scoreLabel.setText("Balls in hole: " + score);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (Hole hole : holes) {
            hole.draw(g2);
        }

        Iterator<Ball> iterator = balls.iterator();
        while (iterator.hasNext()) {
            Ball b = iterator.next();
            if (b.isRemoved()) {
                iterator.remove();
            } else {
                b.draw(g2);
            }
        }
    }
}
