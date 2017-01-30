/**
 * A third-degree polynomial in three-space
 * 
 * @author serena
 * @since 12/13/16
 */
public class PIDFunction {
	
	private double intercept; // zeroth-degree coefficient
	private double d1, p1, i1, bound1; // first-degree coefficient
	private double d2, p2, i2, pd, pi, id, bound2; // second-degree coefficient
	private double d3, p3, i3, pd2, p2d, id2, p2i, i2d, pi2, pid, bound3; // third-degree coefficient
	
	
	public PIDFunction(double[] coefficients) {
		intercept = coefficients[0]; // set
		d1 = coefficients[1];        // all
		p1 = coefficients[2];        // the
		i1 = coefficients[3];        // instance
		pd = coefficients[4];        // variables
		pi = coefficients[5];
		id = coefficients[6];
		d2 = coefficients[7];
		p2 = coefficients[8];
		i2 = coefficients[9];
		p2d = coefficients[10];
		pd2 = coefficients[11];
		p2i = coefficients[12];
		pi2 = coefficients[13];
		id2 = coefficients[14];
		i2d = coefficients[15];
		d3 = coefficients[16];
		p3 = coefficients[17];
		i3 = coefficients[18];
		pid = coefficients[19];
	}
	
	
	/**
	 * @return the value of the polynomial at (d, p, i)
	 */
	public double solve(double d, double p, double i, double bound) {
		return intercept + (d1 * d) + (p1 * p) + (i1 * i) + (d2 * d * d) + (p2 * p * p) + (i2 * i * i) + (pd * p * d)
				+ (pi * p * i) + (id * i * d) + (d3 * d * d * d) + (p3 * p * p * p) + (i3 * i * i * i)
				+ (pd2 * p * d * d) + (p2d * p * p * d) + (id2 * i * d * d) + (p2i * p * p * i) + (i2d * i * i * d)
				+ (pi2 * p * i * i) + (pid * p * i * d);
	}
	
	
	/**
	 * @return something like the partial derivative of <code>solve()</code>
	 * with respect to d, but the coefficients are wrong (FIXME)
	 */
	public double gradientD(double d, double p, double i) {
		return (d1) + (d2 * d) + (pd * p) + (id * i) + (d3 * d * d) + (pd2 * p * d) + (p2d * p * p) + (id2 * i * d)
				+ (i2d * i * i) + (pid * p * i);
	}
	
	
	/**
	 * @return something like the partial derivative of <code>solve()</code>
	 * with respect to p, but the coefficients are wrong (FIXME)
	 */
	public double gradientP(double d, double p, double i) {
		return (p1) + (p2 * p) + (pd * d) + (pi * i) + (p3 * p * p) + (pd2 * d * d) + (p2d * p * d) + (p2i * p * i)
				+ (pi2 * i * i) + (pid * i * d);
	}
	
	
	/**
	 * @return something like the partial derivative of <code>solve()</code>
	 * with respect to i, but the coefficients are wrong (FIXME)
	 */
	public double gradientI(double d, double p, double i) {
		return (i1) + (i2 * i) + (pi * p) + (id * d) + (i3 * i * i) + (id2 * d * d) + (p2i * p * p) + (i2d * i * d)
				+ (pi2 * p * i) + (pid * p * d);
	}
	
	
	/**
	 * @return the value of a separate single-dimensional polynomial
	 */
	public double gradientBound(double bound) {
		return bound1 + 2 * bound2 * bound + 3 * bound3 * bound * bound;
	}
}
