import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Hole {
    private int x, y;
    private static final int SIZE = 30;

    public Hole(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double(x, y, SIZE, SIZE));
    }

    public boolean containsBall(Ball ball) {
        int ballX = ball.getX();
        int ballY = ball.getY();
        int ballSize = ball.getSize();

        int centerX = ballX + ballSize / 2;
        int centerY = ballY + ballSize / 2;

        return centerX >= x && centerX <= x + SIZE &&
                centerY >= y && centerY <= y + SIZE;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static int getSize() {
        return SIZE;
    }
}
