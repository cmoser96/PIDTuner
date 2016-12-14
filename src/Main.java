/**
 * Created by carl on 11/19/16.
 */
public class Main {

    public static final double DT = 1.0 / 1000.0;

    private static PID pid = new PID(0,0,0,0);
    private static InvertedPendulum pend = new InvertedPendulum(10, 0, 5, pid);

    public static void main(String[] args) {
        PIDTuner tuner = new PIDTuner(pend, false);
        tuner.trainPID();

        MainFrame frame = new MainFrame(pend);
        Simulator sim = new Simulator((int) (DT * 1000)) {
            @Override
            public void simulate() {
                frame.repaint();
            }
        };
        sim.runSimulation(50000);
    }
}

