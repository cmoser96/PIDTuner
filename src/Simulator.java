/**
 * Created by serena on 12/4/16.
 */
public class Simulator {
    private long prevTime = 0L;
    private int dt;

    public Simulator(int msDelay){
        this.dt = msDelay;
    }

    public void simulate(){}

    public void runSimulation(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(true){
                    long currentTime = System.currentTimeMillis();
                    if(currentTime>=(prevTime+dt)){
                        simulate();
                        prevTime = currentTime;
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
