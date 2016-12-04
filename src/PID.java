/**
 * Created by serena on 12/4/16.
 */
public class PID
{
   private double pterm, dterm, iterm;

   private double istate = 0.0;
   private static final double ISTATE_BOUND = 1000.0;
   private double prevError = 0.0;

   public PID(){
      this(0.0, 1.0, 0.0);
   }

   public PID(double dterm, double pterm, double iterm){
      this.dterm = dterm;
      this.pterm = pterm;
      this.iterm = iterm;
   }

   public double getCorrection(double error){
      double proportional = pterm*error;
      double derivative = dterm*(error-prevError);
      istate += error;
      boundIstate();
      double integral = iterm*istate;

      return proportional+derivative+integral;
   }

   private void boundIstate(){
      if (istate > ISTATE_BOUND)
         istate = ISTATE_BOUND;
      else if (istate < -ISTATE_BOUND)
         istate = -ISTATE_BOUND;
   }
}
