/**
 * Created by serena on 12/13/16.
 */
public class PIDFunction {

    private double intercept;
    private double d1, p1, i1, bound1;
    private double d2, p2, i2, bound2;
    private double d3, p3, i3, bound3;

    public PIDFunction(double[] coefficients) {
//        intercept = coefficients[0];
//        d1 = coefficients[1];
//        p1 = coefficients[2];
//        i1 = coefficients[3];
//        bound1 = coefficients[4];
//
//        d2 = coefficients[5];
//        p2 = coefficients[6];
//        i2 = coefficients[7];
//        bound2 = coefficients[8];
//
//        d3 = coefficients[9];
//        p3 = coefficients[10];
//        i3 = coefficients[11];
//        bound3 = coefficients[12];

        intercept = coefficients[0];
        d1 = coefficients[1];
        p1 = coefficients[2];
        i1 = 0;
        bound1 = 0;

        d2 = coefficients[3];
        p2 = coefficients[4];
        i2 = 0;
        bound2 = 0;

        d3 = coefficients[5];
        p3 = coefficients[6];
        i3 =  0;
        bound3 =  0;
    }

    public double solve(double d, double p, double i, double bound) {
        return intercept + d1*d + p1*p + i1*i + bound1*bound +
                d2*d*d + p2*p*p + i2*i*i + bound2*bound*bound +
                d3*d*d*d + p3*p*p*p + i3*i*i*i + bound3*bound*bound*bound;
    }


    public double gradientD(double d) {
        return d1 + 2 * d2 * d + 3 * d3 * d * d;
    }

    public double gradientP(double p) {
        return p1 + 2 * p2 * p + 3 * p3 * p * p;
    }

    public double gradientI(double i) {
        return i1 + 2 * i2 * i + 3 * i3 * i * i;
    }

    public double gradientBound(double bound) {
        return bound1 + 2 * bound2 * bound + 3 * bound3 * bound * bound;
    }
}
