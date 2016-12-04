/**
 * Created by serena on 12/4/16.
 */
public class PIDTuner {
    private final PID pid;
    private final InvertedPendulum pendulum;

    public static final int TIMEOUT_TIME = 10_000; //ms

    public PIDTuner(PID pid, InvertedPendulum pendulum){
        this.pid = pid;
        this.pendulum = pendulum;
    }


}
