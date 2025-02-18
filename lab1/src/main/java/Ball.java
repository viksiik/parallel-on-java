import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

class Ball {
    private Component canvas;
    private static final int XSIZE = 20;
    private static final int YSIZE = 20;
    private int x = 0;
    private int y = 0;
    private int dx = 2;
    private int dy = 2;
    private Color color;
    Random rand = new Random();

    public Ball(Component c, Color color) {
        this.canvas = c;
        this.color = color;
        this.x = rand.nextInt(canvas.getWidth() - XSIZE);
        this.y = rand.nextInt(canvas.getHeight() - YSIZE);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(this.color);
        g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));
    }

    public void move() {
        x += dx;
        y += dy;
        if (x < 0 || x + XSIZE >= this.canvas.getWidth()) {
            dx = -dx;
        }
        if (y < 0 || y + YSIZE >= this.canvas.getHeight()) {
            dy = -dy;
        }
        this.canvas.repaint();
    }
}