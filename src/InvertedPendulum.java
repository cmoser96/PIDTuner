/**
 * Created by serena on 11/28/16.
 */
public class InvertedPendulum {
    private Point pointOfContact, head;
    private final double mass; //kg
    private final double radius; //m

    private double angularVel, angularAccel;

    private final Point stableEq, unstableEq;

    private PID pid;

    public InvertedPendulum(double radius, PID pid) {
        this.pointOfContact = new Point(0, 0, 0);
        this.head = new Point(0, 0, radius);
        this.mass = 1;
        this.radius = radius;

        this.angularVel = 0;
        this.angularAccel = 0;

        this.stableEq = new Point(pointOfContact.x, pointOfContact.y, pointOfContact.z - radius);
        this.unstableEq = new Point(pointOfContact.x, pointOfContact.y, pointOfContact.z + radius);

        this.pid = pid;
    }

    public InvertedPendulum(double x, double y, double z, PID pid) {
        this.pointOfContact = new Point(0, 0, 0);
        this.head = new Point(x, y, z);
        this.mass = 1;
        this.radius = Math.sqrt(x * x + y * y + z * z);

        this.angularVel = 0;
        this.angularAccel = 0;

        this.stableEq = new Point(pointOfContact.x, pointOfContact.y, pointOfContact.z - radius);
        this.unstableEq = new Point(pointOfContact.x, pointOfContact.y, pointOfContact.z + radius);

        this.pid = pid;
    }

    public void computeAcceleration() {
        Point r = head.subtract(pointOfContact);
        Point equilibrium = stableEq.subtract(head);
        double theta = Math.asin(r.dot(equilibrium) / (r.magnitude() * equilibrium.magnitude()));
        this.angularAccel = -9.8 * theta / radius;
    }

    public double externalAcceleration() {
        double thetaDiff = getAngleBtwnUnstable();
        angularAccel = pid.getCorrection(thetaDiff);
        return angularAccel;
    }

    public void step() {
        computeAcceleration();
        angularVel += angularAccel * Main.DT;
        double dtheta = angularVel * Main.DT;
        Point r = head.subtract(pointOfContact);
        Point rhat = r.unitVector();
        double dz = -Math.cos(dtheta);
        double dxy = Math.sin(dtheta);
        double dx = dxy * rhat.x;
        double dy = dxy * rhat.y;
        head = new Point(head.x + dx, head.y + dy, head.z + dz);
        System.out.println(head);
    }

    public Point getPointOfContact() {
        return pointOfContact;
    }

    public Point getHead() {
        return head;
    }

    public double getAngleBtwnUnstable(){
        return unstableEq.angleBetween(head);
    }

}
