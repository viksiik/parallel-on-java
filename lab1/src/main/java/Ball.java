import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Random;

public class Ball {
    private Component canvas;
    private static final int SIZE = 20;
    private int x = 0;
    private int y = 0;
    private int dx = 2;
    private int dy = 2;
    private boolean isInHole = false;

    public Ball(Component c) {
        this.canvas = c;

        if (Math.random() < 0.5) {
            x = new Random().nextInt(this.canvas.getWidth());
            y = 0;
        } else {
            x = 0;
            y = new Random().nextInt(this.canvas.getHeight());
        }
    }

    public void draw(Graphics2D g2) {
        if (!isInHole) {
            g2.setColor(Color.darkGray);
            g2.fill(new Ellipse2D.Double(x, y, SIZE, SIZE));
        }
    }

    public void move(List<Hole> holes) {
        if (isInHole) return;

        x += dx;
        y += dy;

        if (x < 0 || x + SIZE >= this.canvas.getWidth()) {
            dx = -dx;
        }
        if (y < 0 || y + SIZE >= this.canvas.getHeight()) {
            dy = -dy;
        }

        for (Hole hole : holes) {
            if (hole.containsBall(this)) {
                isInHole = true;
                break;
            }
        }

        this.canvas.repaint();
    }

    public boolean isRemoved() {
        return isInHole;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return SIZE;
    }

    public boolean isInHole(List<Hole> holes) {
        for (Hole hole : holes) {
            int holeX = hole.getX();
            int holeY = hole.getY();
            int holeSize = Hole.getSize();

            if (Math.abs(x - holeX) < holeSize && Math.abs(y - holeY) < holeSize) {
                return true;
            }
        }
        return false;
    }

}
