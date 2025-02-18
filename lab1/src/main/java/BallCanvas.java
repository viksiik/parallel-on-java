import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class BallCanvas extends JPanel {
    private List<Ball> balls;

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

        Iterator<Ball> iterator = balls.iterator();
        while (iterator.hasNext()) {
            Ball b = iterator.next();
            b.draw(g2);
        }

        for (Ball b : balls) {
            if (b.getColor() == Color.RED) {
                b.draw(g2);
            }
        }
    }

}
