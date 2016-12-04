import javax.swing.*;
import java.awt.*;

/**
 * Created by carl on 11/19/16.
 */
public class Main {

    public static final double DT = 1 / 100;
    public static final int WIDTH = 800, HEIGHT = 800;
    private static PID pid = new PID();
    private static InvertedPendulum pend = new InvertedPendulum(3, 0, 5, pid);
    private static InvertedPendulumDrawer pendulumDrawer;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Inverted Pendulum");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pendulumDrawer = new InvertedPendulumDrawer(pend, WIDTH / 2, HEIGHT / 2);

        frame.add(mainDrawing());

        frame.setVisible(true);

        while (true) {
            frame.repaint();
            try {
                Thread.sleep((long) (DT * 1000));
            } catch (InterruptedException e) {
            }
        }
    }

    public static JPanel mainDrawing() {
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.clearRect(0, 0, WIDTH, HEIGHT);
                super.paintComponent(g);
                pendulumDrawer.stepAndDraw(g);
            }
        };
        return panel;
    }
}
