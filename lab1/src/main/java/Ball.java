import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ball {
    private Component canvas;
    private static final int SIZE = 20;
    private static final int SPEED = 2;
    private int x;
    private int y;
    private int dx = SPEED;
    private int dy = SPEED;
    private Color color;

    public Ball(Component c, Color color) {
        this.canvas = c;
        this.color = color;

        this.x = canvas.getWidth() / 2;
        this.y = canvas.getHeight() / 2;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fill(new Ellipse2D.Double(x, y, SIZE, SIZE));
    }

    public void move() {
        x += dx;
        y += dy;

        if (x < 0 || x + SIZE >= this.canvas.getWidth()) {
            dx = -dx;
        }
        if (y < 0 || y + SIZE >= this.canvas.getHeight()) {
            dy = -dy;
        }

        this.canvas.repaint();
    }

    public Color getColor() {
        return color;
    }
}
