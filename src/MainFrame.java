import javax.swing.*;
import java.awt.*;

/**
 * Created by serena on 12/8/16.
 */
public class MainFrame extends JFrame {
    private final InvertedPendulumDrawer pendulumDrawer;

    public static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 800;

    public MainFrame(InvertedPendulum pend) {
        super("Inverted Pendulum");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pendulumDrawer = new InvertedPendulumDrawer(pend, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);

        add(mainDrawing(true));

        setVisible(true);
    }

    public MainFrame(InvertedPendulum pend, boolean stepDuringPaint) {
        super("Inverted Pendulum");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pendulumDrawer = new InvertedPendulumDrawer(pend, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);

        add(mainDrawing(stepDuringPaint));

        setVisible(true);
    }

    public JPanel mainDrawing(boolean stepDuringPaint) {
        final boolean step = stepDuringPaint;
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                super.paintComponent(g);
                if(step)
                    pendulumDrawer.stepAndDraw(g);
                else
                    pendulumDrawer.draw(g);
            }
        };
        return panel;
    }
}

