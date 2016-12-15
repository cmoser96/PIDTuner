import javax.swing.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by carl on 11/19/16.
 */
public class Main {

    public static final double DT = 1.0 / 1000.0;

    private static PID pid = new PID(0.84, 0.092, 0.00001, 10_000);
    private static Vector3D originalPos = new Vector3D(10, 0, 5);
    private static final InvertedPendulum pend = new InvertedPendulum(originalPos.x, originalPos.y, originalPos.z, pid);
//    private static Scanner in = new Scanner(System.in);
    static int stable = 0, unstable = 0;
    static int steps = 30000;

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            PIDTuner tuner = new PIDTuner(pend, false);
            tuner.trainPID();

            MainFrame frame = new MainFrame(pend);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Simulator sim = new Simulator((int) (DT * 1000)) {
                @Override
                public void simulate() {
                    frame.repaint();
                }
            };
            sim.runSimulation(steps);

            pend.resetToPos(originalPos);
            frame.dispose();

//            boolean done = false;
//            while(!done) {
//                done = true;
//                System.out.println("type 1 for stable and 0 for unstable");
//                String line = in.nextLine().trim();
//                if (line.contains("0")) {
//                    unstable++;
//                    System.out.println("UNSTABLE");
//                    System.out.println("----------------------------------");
//                } else if (line.contains("1")) {
//                    stable++;
//                    System.out.println("STABLE");
//                    double avgError = error[0]/steps;
//                    System.out.println(avgError);
//                    System.out.println(Arrays.toString(istates));
//                    System.out.println("----------------------------------");
//                }
//                else{
//                    done = false;
//                }
//            }

        }
//        System.out.println(String.format("Stable: %d", stable));
//        System.out.println(String.format("Unstable: %d", unstable));

    }
}

