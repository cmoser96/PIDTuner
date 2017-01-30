import java.awt.*;

/**
 * A class for rendering the Pendulum on a JPanel
 * 
 * @author serena
 * @since 12/1/16
 */
public class InvertedPendulumDrawer {
	private InvertedPendulum pend; // the model
	private int centerX, centerY;
	
	private static final int POINT_OF_CONTACT_RADIUS = 10, HEAD_RADIUS = 15;
	private static final int SCALE = 20;
	
	
	public InvertedPendulumDrawer(InvertedPendulum p, int centerX, int centerY) {
		this.pend = p;
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	
	/**
	 * step and draw
	 */
	public void stepAndDraw(Graphics g) {
		pend.step(); // step
		draw(g); // draw
	}
	
	
	/**
	 * Displays the pendulum on a <code>Graphics</code> object
	 */
	public void draw(Graphics g) {
		int pointOfContactX = centerX + (int) pend.getPointOfContact().x * SCALE; // convert coordinates
		int pointOfContactY = centerY - (int) pend.getPointOfContact().z * SCALE;
										// be sure to cast to an int _before_ multiplying by a double (FIXME)
		int headX = centerX + (int) pend.getHead().x * SCALE;
		int headY = centerY - (int) pend.getHead().z * SCALE;
		
		g.drawLine(pointOfContactX, pointOfContactY, headX, headY); // draw the arm
		g.setColor(new Color(0x89cc0e));
		g.fillOval(pointOfContactX - POINT_OF_CONTACT_RADIUS, pointOfContactY - POINT_OF_CONTACT_RADIUS,
				2 * POINT_OF_CONTACT_RADIUS, 2 * POINT_OF_CONTACT_RADIUS); // and the pivot
		g.setColor(new Color(0x561872));
		g.fillOval(headX - HEAD_RADIUS, headY - HEAD_RADIUS, HEAD_RADIUS * 2, HEAD_RADIUS * 2); // and the head
	}
	
}
