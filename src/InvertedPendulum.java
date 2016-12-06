/**
 * Created by serena on 11/28/16.
 */
public class InvertedPendulum {
    private Point pointOfContact, head;
    private final double mass; //kg
    private final double radius; //m

    private double angularVel, angularAccel;

    private final Point stableEq, unstableEq;

    private PID pid = new PID(); //will change this later after we get a thing that trains PIDs. Or have this passed in to the constructor

    public InvertedPendulum(double radius) {
        this.pointOfContact = new Point(0, 0, 0);
        this.head = new Point(0, 0, radius);
        this.mass = 1;
        this.radius = radius;

        this.angularVel = 0;
        this.angularAccel = 0;

        this.stableEq = new Point(pointOfContact.x, pointOfContact.y, pointOfContact.z - radius);
        this.unstableEq = new Point(pointOfContact.x, pointOfContact.y, pointOfContact.z + radius);
    }

    public InvertedPendulum(double x, double y, double z) {
        this.pointOfContact = new Point(0, 0, 0);
        this.head = new Point(x, y, z);
        this.mass = 1;
        this.radius = Math.sqrt(x * x + y * y + z * z);

        this.angularVel = 0;
        this.angularAccel = 0;

        this.stableEq = new Point(pointOfContact.x, pointOfContact.y, pointOfContact.z - radius);
        this.unstableEq = new Point(pointOfContact.x, pointOfContact.y, pointOfContact.z + radius);
    }

    public void computeAcceleration() {
        Point r = head.subtract(pointOfContact);
        Point equilibrium = stableEq.subtract(head);
        double theta = Math.asin(r.dot(equilibrium) / (r.magnitude() * equilibrium.magnitude()));

        //Lagrangian:
        double T = .5*this.mass*Math.pow(this.radius, 2)*Math.pow(this.angularVel, 2);
        double U = 9.81*this.mass*this.radius*(1- Math.cos(theta));

        this.angularAccel = -9.81*Math.sin(theta)/this.radius;
    }

    public double externalAcceleration() {
        double thetaDiff = unstableEq.angleBetween(head);
        angularAccel = pid.getCorrection(thetaDiff);
        return angularAccel;
    }

    public void step() {
        computeAcceleration();
        angularVel += angularAccel * Main.DT;
        double dtheta = angularVel * Main.DT;
        Point r = head.subtract(pointOfContact);
        Point rhat = r.unitVector();
        double theta = stableEq.angleBetween(head)+dtheta;
        double z = -Math.cos(theta);
        double x = Math.sin(theta);
        System.out.println();
        head = new Point(x, head.y, z);
        System.out.println(head);
    }

    public Point getPointOfContact() {
        return pointOfContact;
    }

    public Point getHead() {
        return head;
    }

}
