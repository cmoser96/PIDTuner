import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
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

    public static final int TIMEOUT_TIME_STEPS = (int) (5.0 / Main.DT);
    private static final int NUM_TESTS = 1000;

    private double[] dterms = new double[NUM_TESTS], pterms = new double[NUM_TESTS],
            iterms = new double[NUM_TESTS], ibounds = new double[NUM_TESTS];
    private double[] d2terms = new double[NUM_TESTS], p2terms = new double[NUM_TESTS],
            i2terms = new double[NUM_TESTS], i2bounds = new double[NUM_TESTS],
            piterms = new double[NUM_TESTS], idterms = new double[NUM_TESTS],
            pdterms = new double[NUM_TESTS];
    private double[] d3terms = new double[NUM_TESTS], p3terms = new double[NUM_TESTS],
            i3terms = new double[NUM_TESTS], i3bounds = new double[NUM_TESTS],
            p2iterms = new double[NUM_TESTS], pi2terms = new double[NUM_TESTS],
            p2dterms = new double[NUM_TESTS], pd2terms = new double[NUM_TESTS],
            i2dterms = new double[NUM_TESTS], id2terms = new double[NUM_TESTS],
            pidterms = new double[NUM_TESTS];
    private final double[] score = new double[NUM_TESTS];
    private Vector3D originalPos;

    public static final double D_UPPER = 1.5, D_LOWER = 0.0, D_RANGE = D_UPPER - D_LOWER;
    public static final double P_UPPER = 1.5, P_LOWER = 0.0, P_RANGE = P_UPPER - P_LOWER;
    public static final double I_UPPER = 0.0005, I_LOWER = 0.0, I_RANGE = I_UPPER - I_LOWER;
    public static final double I_BOUND_UPPER = 10000.0, I_BOUND_LOWER = 0.0, I_BOUND_RANGE = I_BOUND_UPPER - I_BOUND_LOWER;

    private static final int GRAD_DESCENT_ITERATIONS = Integer.MAX_VALUE;
    private static final double GRAD_DESCENT_THRESHOLD = 0.01;
    private static final double ALPHA = 1e-9;

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
            double bound = 10_000;//random.nextDouble() * I_BOUND_RANGE + I_BOUND_LOWER;

            pid.setConstants(d, p, i, bound);

            dterms[j] = d;
            pterms[j] = p;
            iterms[j] = i;
            ibounds[j] = bound;

            final int index = j;

            //simulate, with drawing?
            Simulator sim = new Simulator(0) {
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
        }

        try {
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

    private Regression getRegression() {
        for (int i = 0; i < NUM_TESTS; i++) {
            pdterms[i] = pterms[i] * dterms[i];
            piterms[i] = pterms[i] * iterms[i];
            idterms[i] = iterms[i] * dterms[i];
            pidterms[i] = pterms[i] * iterms[i] * dterms[i];

            p2terms[i] = pterms[i] * pterms[i];
            p2dterms[i] = p2terms[i] * dterms[i];
            p2iterms[i] = p2terms[i] * iterms[i];
            p3terms[i] = p2terms[i] * pterms[i];

            d2terms[i] = dterms[i] * dterms[i];
            pd2terms[i] = d2terms[i] * pterms[i];
            id2terms[i] = d2terms[i] * iterms[i];
            d3terms[i] = d2terms[i] * dterms[i];

            i2terms[i] = iterms[i] * iterms[i];
            pi2terms[i] = i2terms[i] * pterms[i];
            i2dterms[i] = i2terms[i] * dterms[i];
            i3terms[i] = i2terms[i] * iterms[i];
        }
        Regression regression = new Regression(new double[][]{dterms, pterms, iterms,
                pdterms, piterms, idterms, d2terms, p2terms, i2terms,
                p2dterms, pd2terms, p2iterms, pi2terms, id2terms, i2dterms, d3terms, p3terms, i3terms, pidterms}, score);
        regression.linear();
//        System.out.println(regression.getSumOfSquares());
        return regression;
    }

    public void gradientDescent(Regression regression) {
        double[] coefficients = regression.getCoeff();
        PIDFunction f = new PIDFunction(coefficients);

        double d = random.nextDouble() * D_RANGE + D_LOWER;
        double p = random.nextDouble() * P_RANGE + P_LOWER;
        double i = random.nextDouble() * I_RANGE + I_LOWER;
        double bound = 10_000;//random.nextDouble() * I_BOUND_RANGE + I_BOUND_LOWER;
        double absError = Math.abs(f.solve(d, p, i, bound));

        int count = 0;

        while (count++ < GRAD_DESCENT_ITERATIONS && absError > GRAD_DESCENT_THRESHOLD) {

            double gradD = f.gradientD(d, p, i);
            double gradP = f.gradientP(d, p, i);
            double gradI = f.gradientI(d, p, i);
//            double gradBound = f.gradientBound(bound);

            d = d - ALPHA * gradD;
            p = p - ALPHA * gradP;
            i = i - ALPHA/1e9 * gradI;
            absError = Math.abs(f.solve(d, p, i, bound));
        }
        System.out.println(count);

        pid.setConstants(d, p, i, bound);
        System.out.println(String.format("%f %f %f %f", d, p, i, absError));
    }


    public void trainPID() {
        testPIDPoints();
        Regression regression = getRegression();
        gradientDescent(regression);
    }

}
