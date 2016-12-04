/**
 * Created by serena on 12/4/16.
 */
public class PID {
    private double pterm, dterm, iterm;

    private double istate = 0.0;
    private double istateBound;
    private double prevError = 0.0;

    public PID() {
        this(0.0, 1.0, 0.0, 1000.0);
    }

    public PID(double dterm, double pterm, double iterm, double istateBound) {
        this.dterm = dterm;
        this.pterm = pterm;
        this.iterm = iterm;
        this.istateBound = istateBound;
    }

    public double getCorrection(double error) {
        double proportional = pterm * error;
        double derivative = dterm * (error - prevError);
        istate += error;
        boundIstate();
        double integral = iterm * istate;

        return proportional + derivative + integral;
    }

    private void boundIstate() {
        if (istate > istateBound)
            istate = istateBound;
        else if (istate < -istateBound)
            istate = -istateBound;
    }

    public double getPterm() {
        return pterm;
    }

    public double getDterm() {
        return dterm;
    }

    public double getIterm() {
        return iterm;
    }

    public double getIstateBound() {
        return istateBound;
    }

}
