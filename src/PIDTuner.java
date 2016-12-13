import java.util.Random;

import flanagan.analysis.Regression;

/**
 * Created by serena on 12/4/16.
 */
public class PIDTuner {
    private final PID pid;
    private final InvertedPendulum pendulum;
    private final Random random = new Random();
    private final MainFrame frame;

    public static final int TIMEOUT_TIME_STEPS = 500;
    private static final int NUM_TESTS = 500;

    private double[] dterms = new double[NUM_TESTS], pterms = new double[NUM_TESTS],
            iterms = new double[NUM_TESTS], ibounds = new double[NUM_TESTS];
    private double[] dterms2 = new double[NUM_TESTS], pterms2 = new double[NUM_TESTS],
            iterms2 = new double[NUM_TESTS], ibounds2 = new double[NUM_TESTS];
    private double[] dterms3 = new double[NUM_TESTS], pterms3 = new double[NUM_TESTS],
            iterms3 = new double[NUM_TESTS], ibounds3 = new double[NUM_TESTS];
    private final double[] score = new double[NUM_TESTS];
    private Vector3D originalPos;

    public static final double D_UPPER = 100.0, D_LOWER = 0.0, D_RANGE = D_UPPER - D_LOWER;
    public static final double P_UPPER = 100.0, P_LOWER = 0.0, P_RANGE = P_UPPER - P_LOWER;
    public static final double I_UPPER = 100.0, I_LOWER = 0.0, I_RANGE = I_UPPER - I_LOWER;
    public static final double I_BOUND_UPPER = 100.0, I_BOUND_LOWER = -100.0, I_BOUND_RANGE = I_BOUND_UPPER - I_BOUND_LOWER;

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
        if (gui)
            this.frame = new MainFrame(pendulum, false);
        else
            this.frame = null;
    }

    private void testPIDPoints() {
        for (int j = 0; j < NUM_TESTS; j++) {
            double d = random.nextDouble() * D_RANGE + D_LOWER;
            double p = random.nextDouble() * P_RANGE + P_LOWER;
            double i = random.nextDouble() * I_RANGE + I_LOWER;
            double bound = random.nextDouble() * I_BOUND_RANGE + I_BOUND_LOWER;

            pid.setConstants(d, p, i, bound);

            dterms[j] = d;
            pterms[j] = p;
            iterms[j] = i;
            ibounds[j] = bound;

            final int index = j;

            //simulate, with drawing?
            Simulator sim = new Simulator((int) (Main.DT * 10)) {
                @Override
                public void simulate() {
                    pendulum.step();
                    if (frame != null)
                        frame.repaint();
                    score[index] += Math.abs(pendulum.getAngleBtwnUnstable());
                }
            };
            sim.runSimulation(TIMEOUT_TIME_STEPS);

            pendulum.resetToPos(originalPos);
            System.out.println(j);
        }
    }

    private Regression getRegression() {
        for(int i = 0; i< NUM_TESTS; i++){
            pterms2[i] = pterms[i]*pterms[i];
            pterms3[i] = pterms2[i]*pterms[i];

            dterms2[i] = dterms[i]*dterms[i];
            dterms3[i] = dterms2[i]*dterms[i];

            iterms2[i] = iterms[i]*iterms[i];
            iterms3[i] = iterms2[i]*iterms[i];

            ibounds2[i] = ibounds[i]*ibounds[i];
            ibounds3[i] = ibounds2[i]*ibounds[i];
        }
        Regression regression = new Regression(new double[][]{dterms, pterms, iterms, ibounds,
                dterms2, pterms2, iterms2, ibounds2,
                dterms3, pterms3, iterms3, ibounds3}, score);
        regression.linearGeneral();
        System.out.println(regression.getSumOfSquares());
        return regression;
    }

    public void gradientDescent(Regression regression){

    }

    public void trainPID() {
        testPIDPoints();
        Regression regression = getRegression();
        gradientDescent(regression);
    }

}
