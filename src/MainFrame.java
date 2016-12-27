import javax.swing.*;
import java.awt.*;

/**
 * The <code>JFrame</code> that displays the pendulum simulation
 * 
 * @author serena
 * @since 12/8/16
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L; // I'm not sure what this does
	
	private final InvertedPendulumDrawer pendulumDrawer; // a class for rendering the pendulum
	
	public static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 800;
	
	
	public MainFrame(InvertedPendulum pend) {
		this(pend, true);
	}
	
	
	public MainFrame(InvertedPendulum pend, boolean stepDuringPaint) {
		super("Inverted Pendulum");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // size the window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make it close when it closes
		
		pendulumDrawer = new InvertedPendulumDrawer(pend, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
		
		add(mainDrawing(stepDuringPaint)); // throw in a JFrame
		
		setVisible(true); // show it
	}
	
	
	/**
	 * @param stepDuringPaint
	 *        Do you want the pendulum to move when you animate it?
	 * @return a JPanel that draws (and possibly simulates) a pendulum when its
	 * <code>repaint</code> is called
	 */
	public JPanel mainDrawing(boolean stepDuringPaint) {
		final boolean step = stepDuringPaint;
		@SuppressWarnings("serial")
		JPanel panel = new JPanel() { // return a JPanel that,
			@Override
			public void paintComponent(Graphics g) { // when its paint method gets called,
				g.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
				super.paintComponent(g);
				if (step)
					pendulumDrawer.stepAndDraw(g); // draws the pendulum using the pendulumDrawer
				else
					pendulumDrawer.draw(g); // and possibly moves it as well
			}
		};
		return panel;
	}
}

