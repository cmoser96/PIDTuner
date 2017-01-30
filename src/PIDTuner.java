import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

import flanagan.analysis.Regression;

/**
 * A class to adjust the values of the PID coefficients to optimize an inverted pendulum
 * 
 * @author serena
 * @since 12/4/16
 */
public class PIDTuner {
	private final PID pid; // the PID controller
	private final InvertedPendulum pendulum; // the pendulum model
	private final Random random = new Random(); // RNJesus
	private final MainFrame frame; // display window (usually null)
	
	public static final int TIMEOUT_TIME_STEPS = (int) (5.0 / Main.DT); // run the simulation for 5 seconds
	private static final int NUM_TESTS = 1000; // the number of data points to take
	
	private double[] dterms = new double[NUM_TESTS], pterms = new double[NUM_TESTS], iterms = new double[NUM_TESTS],
			ibounds = new double[NUM_TESTS]; // all the possible coefficient values
	private double[] d2terms = new double[NUM_TESTS], p2terms = new double[NUM_TESTS], i2terms = new double[NUM_TESTS],
			piterms = new double[NUM_TESTS], idterms = new double[NUM_TESTS], pdterms = new double[NUM_TESTS];
	private double[] d3terms = new double[NUM_TESTS], p3terms = new double[NUM_TESTS], i3terms = new double[NUM_TESTS],
			p2iterms = new double[NUM_TESTS], pi2terms = new double[NUM_TESTS], p2dterms = new double[NUM_TESTS],
			pd2terms = new double[NUM_TESTS], i2dterms = new double[NUM_TESTS], id2terms = new double[NUM_TESTS],
			pidterms = new double[NUM_TESTS];
	private final double[] score = new double[NUM_TESTS]; // all the corresponding scores
	private Vector3D originalPos; // the start position of the pendulum
	
	public static final double D_UPPER = 1.5, D_LOWER = 0.0, D_RANGE = D_UPPER - D_LOWER; // the valid ranges for the coefficients
	public static final double P_UPPER = 1.5, P_LOWER = 0.0, P_RANGE = P_UPPER - P_LOWER;
	public static final double I_UPPER = 0.0005, I_LOWER = 0.0, I_RANGE = I_UPPER - I_LOWER;
	public static final double I_BOUND_UPPER = 10000.0, I_BOUND_LOWER = 0.0,
			I_BOUND_RANGE = I_BOUND_UPPER - I_BOUND_LOWER; // and some I_BOUND-related stuff that never gets used
	
	private static final int GRAD_DESCENT_ITERATIONS = Integer.MAX_VALUE; // don't give up on gradient descent until we get a satisfactory PID system
	private static final double GRAD_DESCENT_THRESHOLD = 0.01; // the acceptable error where gradient descent will stop
	private static final double ALPHA = 1e-9; // the gradient descent step size
	
	
	public PIDTuner(InvertedPendulum pendulum) {
		this.pid = pendulum.getPID();
		this.pendulum = pendulum;
		this.originalPos = pendulum.getHead();
		this.frame = new MainFrame(pendulum, false);
	}
	
	
	public PIDTuner(InvertedPendulum pendulum, boolean gui) {
		this.pid = pendulum.getPID();
		this.pendulum = pendulum;
		this.originalPos = pendulum.getHead();
		if (gui) // if a display is not necessary, this.frame can be null
			this.frame = new MainFrame(pendulum, false);
		else
			this.frame = null;
	}
	
	
	/**
	 * fills <code>dterms</code>, <code>pterms</code>, and <code>iterms</code> with random numbers, and <code>scores</code> with the corresponding pendulum
	 * scores
	 */
	private void testPIDPoints() {
		for (int j = 0; j < NUM_TESTS; j++) { // for each test
			double d = random.nextDouble() * D_RANGE + D_LOWER; // pick some random coefficents
			double p = random.nextDouble() * P_RANGE + P_LOWER;
			double i = random.nextDouble() * I_RANGE + I_LOWER;
			double bound = 10_000;
			
			pid.setConstants(d, p, i, bound);
			
			dterms[j] = d; // save them
			pterms[j] = p;
			iterms[j] = i;
			ibounds[j] = bound;
			
			final int index = j;
			
			Simulator sim = new Simulator(0) { // and rum the simulation with those coefficients
				@Override
				public void simulate() {
					pendulum.step();
					if (frame != null) // drawing simultaneously if there's a window to draw on
						frame.repaint();
					score[index] += Math.abs(pendulum.getAngleBtwnUnstable()); // and scoring based on cumulative distance from the vertical
				}
			};
			sim.runSimulation(TIMEOUT_TIME_STEPS);
			
			pendulum.resetToPos(originalPos); // reset the pendulum for the next simulation
		}
		
		try { // try to write the results to a log file
			PrintWriter out = new PrintWriter(new FileWriter("src/python/pidout.txt"));
			out.println("d");
			out.println(Arrays.toString(dterms));
			out.println("p");
			out.println(Arrays.toString(pterms));
			out.println("i");
			out.println(Arrays.toString(iterms));
			out.println("bounds");
			out.println(Arrays.toString(ibounds));
			out.println("scores");
			out.println(Arrays.toString(score));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @return a <code>flanagan.analysis.Regression</code> object approximating
	 * the score as a third-degree polynomial in terms of p, i, and d
	 */
	private Regression getRegression() {
		for (int i = 0; i < NUM_TESTS; i++) { // for each element in the p, i, and d terms arrays,
			pdterms[i] = pterms[i] * dterms[i]; // fill the corresponding element in the second-
			piterms[i] = pterms[i] * iterms[i];
			idterms[i] = iterms[i] * dterms[i];
			pidterms[i] = pterms[i] * iterms[i] * dterms[i];
			
			p2terms[i] = pterms[i] * pterms[i];
			p2dterms[i] = p2terms[i] * dterms[i]; // and third-degree coefficient arrays
			p2iterms[i] = p2terms[i] * iterms[i]; // since this is apparently the best way to get a
			p3terms[i] = p2terms[i] * pterms[i]; // cubic regression out of this flanagan package
			
			d2terms[i] = dterms[i] * dterms[i];
			pd2terms[i] = d2terms[i] * pterms[i];
			id2terms[i] = d2terms[i] * iterms[i];
			d3terms[i] = d2terms[i] * dterms[i];
			
			i2terms[i] = iterms[i] * iterms[i];
			pi2terms[i] = i2terms[i] * pterms[i];
			i2dterms[i] = i2terms[i] * dterms[i];
			i3terms[i] = i2terms[i] * iterms[i];
		}
		Regression regression = new Regression( // plop the values into a Regression object
				new double[][] { dterms, pterms, iterms, pdterms, piterms, idterms, d2terms, p2terms, i2terms, p2dterms,
						pd2terms, p2iterms, pi2terms, id2terms, i2dterms, d3terms, p3terms, i3terms, pidterms },
				score);
		regression.linear(); // and let it do its thing
		return regression;
	}
	
	
	/**
	 * Use a cubic Regression to perform gradient descent in the p, i, and d
	 * coefficients in order to minimize score
	 */
	public void gradientDescent(Regression regression) {
		double[] coefficients = regression.getCoeff(); // move the coefficients from the Regression
		PIDFunction f = new PIDFunction(coefficients); // to a PIDFunction
		
		double d = random.nextDouble() * D_RANGE + D_LOWER; // pick a random initial condition
		double p = random.nextDouble() * P_RANGE + P_LOWER;
		double i = random.nextDouble() * I_RANGE + I_LOWER;
		double bound = 10_000;
		double absError = Math.abs(f.solve(d, p, i, bound)); // calculate the initial error
		
		int count = 0;
		
		while (count++ < GRAD_DESCENT_ITERATIONS && absError > GRAD_DESCENT_THRESHOLD) { // and until the error is small enough,
			
			double gradD = f.gradientD(d, p, i); // calculate the gradient
			double gradP = f.gradientP(d, p, i);
			double gradI = f.gradientI(d, p, i);
			
			d = d - ALPHA * gradD; // and descend it
			p = p - ALPHA * gradP;
			i = i - ALPHA / 1e9 * gradI;
			absError = Math.abs(f.solve(d, p, i, bound));
		}
		System.out.println(count);
		
		pid.setConstants(d, p, i, bound); // prepare for the next simulation
		System.out.println(String.format("%f %f %f %f", d, p, i, absError)); // and print out the results
	}
	
	
	/**
	 * Call all the above methods in sequence
	 */
	public void trainPID() {
		testPIDPoints();
		Regression regression = getRegression();
		gradientDescent(regression);
	}
	
}
