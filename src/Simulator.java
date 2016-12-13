/**
 * Created by serena on 12/4/16.
 */
public class Simulator {
    private static final double TIME_CONSTANT = 100.0;
    private long prevTime = 0L;
    private int dt;

    public Simulator(int msDelay) {
        this.dt = (int) msDelay;
    }

    public void simulate() {
    }

    public void runSimulation(int numSteps) {
        int i = 0;
        while (i < numSteps) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= (prevTime + dt)) {
                simulate();
                prevTime = currentTime;
                i++;
            }
        }
    }
}
