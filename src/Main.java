import javax.swing.*;

/**
 * The main class.
 * 
 * @author carl
 * @since 11/19/16
 */
public class Main {
	
	public static final double DT = 1.0 / 1000.0; // the time-step
	
	private static PID pid = new PID(0.84, 0.092, 0.00001, 10_000); // the PID (I don't think these values ever get used)
	private static Vector3D originalPos = new Vector3D(10, 0, 5); // the initial position of the pendulum
	private static final InvertedPendulum pend = new InvertedPendulum(originalPos.x, originalPos.y, originalPos.z, pid);
	static int stable = 0, unstable = 0; // some unused zeros
	static int steps = 30000; // the number of steps to display
	
	
	/**
	 * The main method.
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {	// iterate once:
			PIDTuner tuner = new PIDTuner(pend, false); // plug the pendulum into a tuner
			tuner.trainPID(); // and make it tune the PID coefficients
			
			MainFrame frame = new MainFrame(pend); // now open a window
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			Simulator sim = new Simulator((int) (DT * 1000)) {
				@Override
				public void simulate() { // to show the pendulum simulation
					frame.repaint();
				}
			};
			sim.runSimulation(steps); // with the tuned coefficients
			
			pend.resetToPos(originalPos); // reset the pendulum's position for some reason
			frame.dispose(); // and close the window
		}
	}
}

