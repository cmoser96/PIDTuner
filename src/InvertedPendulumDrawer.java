import javax.print.attribute.standard.ColorSupported;
import java.awt.*;

/**
 * Created by serena on 12/1/16.
 */
public class InvertedPendulumDrawer {
    private InvertedPendulum pend;
    private int centerX, centerY;

    private static final int POINT_OF_CONTACT_RADIUS = 10, HEAD_RADIUS = 15;
    private static final int SCALE = 20;

    public InvertedPendulumDrawer(InvertedPendulum p, int centerX, int centerY) {
        this.pend = p;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void stepAndDraw(Graphics g) {
        pend.step();
        draw(g);
    }

    public void draw(Graphics g){
        int pointOfContactX = centerX + (int) pend.getPointOfContact().x * SCALE;
        int pointOfContactY = centerY - (int) pend.getPointOfContact().z * SCALE;

        int headX = centerX + (int) pend.getHead().x * SCALE;
        int headY = centerY - (int) pend.getHead().z * SCALE;

        g.drawLine(pointOfContactX, pointOfContactY, headX, headY);
        g.setColor(new Color(0x89cc0e));
        g.fillOval(pointOfContactX - POINT_OF_CONTACT_RADIUS, pointOfContactY - POINT_OF_CONTACT_RADIUS, 2 * POINT_OF_CONTACT_RADIUS, 2 * POINT_OF_CONTACT_RADIUS);
        g.setColor(new Color(0x561872));
        g.fillOval(headX - HEAD_RADIUS, headY - HEAD_RADIUS, HEAD_RADIUS * 2, HEAD_RADIUS * 2);
    }

}
