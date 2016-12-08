/**
 * Created by serena on 12/4/16.
 */
public class Simulator {
    private static final double TIME_CONSTANT = 100.0;
    private long prevTime = 0L;
    private int dt;
    Thread thread = null;
    Runnable runnable = null;

    public Simulator(int msDelay) {
        this.dt = (int) (msDelay * TIME_CONSTANT);
    }

    public void simulate() {
    }

    public void runSimulation(int numSteps) {
//        if (thread == null) {
//            runnable = new Runnable() {
//                @Override
//                public void run() {
        int i = 0;
                    while(i<numSteps){
                        long currentTime = System.currentTimeMillis();
                        if (currentTime >= (prevTime + dt)) {
                            simulate();
                            prevTime = currentTime;
                            i++;
                        }
                    }
                }
//            };
//            this.thread = new Thread(runnable);
////        }
//
//        thread.start();
//    }

//    public void stopSimulation() {
//        if (thread.isAlive()) {
//            thread.interrupt();
//
//        }
//    }
}
