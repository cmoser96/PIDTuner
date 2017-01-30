/**
 * An abstract class for calling a method repeatedly in realtime
 * 
 * @author serena
 * @since 12/4/16
 */
public abstract class Simulator {
	private long prevTime = 0L;
	private int dt;
	
	
	/**
	 * @param msDelay The minimum time each step can take
	 */
	public Simulator(int msDelay) {
		this.dt = (int) msDelay;
	}
	
	
	/**
	 * This method gets called many times in runSimulation
	 */
	public abstract void simulate();
	
	
	/**
	 * Call the simulate method a bunch of times, with a delay between each call
	 */
	public void runSimulation(int numSteps) {
		int i = 0;
		while (i < numSteps) { // for the requisite number of steps
			long currentTime = System.currentTimeMillis();
			if (currentTime >= (prevTime + dt)) { // wait the appropriate amount of time
				prevTime = currentTime;
				simulate(); // call the method
				i++;
			}
		}
	}
}
