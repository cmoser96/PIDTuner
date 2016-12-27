/**
 * A Proportional-Integral-Derivative control loop
 * 
 * @author serena
 * @since 12/4/16
 */
public class PID {
	private double pterm, dterm, iterm; // PID coefficients
	private double istateBound; // the limit on the integral
	
	private double istate = 0.0; // the current value of the integral
	private double prevError = 0.0; // 0
	
	
	public PID() {
		this(0.0, 1.0, 0.0, 1000.0);
	}
	
	
	public PID(double dterm, double pterm, double iterm, double istateBound) {
		this.dterm = dterm;
		this.pterm = pterm;
		this.iterm = iterm;
		this.istateBound = istateBound;
	}
	
	
	/**
	 * @return the force to be exerted based on the proportional term, the integral, and the derivative
	 */
	public double getCorrection(double error) {
		double proportional = pterm * error; // proportional term
		double derivative = dterm * (error - prevError); // another proportional term, but with zero subtracted out of it for some reason (probably a bug? (FIXME))
		istate += error;
		boundIstate();
		double integral = iterm * istate; // integral term
		
		return proportional + derivative - integral; // P.D.I.
	}
	
	
	/**
	 * Coerces |istate| <= istatebound
	 */
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
	
	
	public double getIstate() {
		return istate;
	}
	
	
	/**
	 * Changes all of the instance variables except istate
	 */
	public void setConstants(double newDTerm, double newPTerm, double newITerm, double newIBound) {
		this.dterm = newDTerm;
		this.pterm = newPTerm;
		this.iterm = newITerm;
		this.istateBound = newIBound;
	}
}
