/**
 * Created by serena on 11/28/16.
 */
public class InvertedPendulum {
    private Vector3D pointOfContact, head;
    private final double mass; //kg
    private final double radius; //m

    private double angularVel, angularAccel;

    private final Vector3D stableEq, unstableEq;

    private PID pid;

    public InvertedPendulum(double radius, PID pid) {
        this(0, 0, radius, radius, pid);
    }

    public InvertedPendulum(double x, double y, double z, PID pid) {
        this(x, y, z, Math.sqrt(x * x + y * y + z * z), pid);
    }

    private InvertedPendulum(double x, double y, double z, double radius, PID pid){
        this.pointOfContact = new Vector3D(0, 0, 0);
        this.head = new Vector3D(x, y, z);
        this.mass = 1;
        this.radius = radius;

        this.angularVel = 0;
        this.angularAccel = 0;

        this.stableEq = new Vector3D(pointOfContact.x, pointOfContact.y, pointOfContact.z - radius);
        this.unstableEq = new Vector3D(pointOfContact.x, pointOfContact.y, pointOfContact.z + radius);

        this.pid = pid;
    }

    public void computeAcceleration() {
        double theta = getAngleBtwnStable();

        //Lagrangian: (for reference)
        double T = .5*this.mass*Math.pow(this.radius, 2)*Math.pow(this.angularVel, 2);
        double U = 9.81*this.mass*this.radius*(1- Math.cos(theta));

        this.angularAccel = -9.81*Math.sin(theta)/this.radius + externalAcceleration();
    }

    public double externalAcceleration() {
        double thetaDiff = getAngleBtwnUnstable();
        double externalAccel = pid.getCorrection(thetaDiff);
        return externalAccel;
    }

    public void step() {
        computeAcceleration();
        angularVel += angularAccel * Main.DT;
        double dtheta = angularVel * Main.DT;
        double theta = getAngleBtwnStable()+dtheta;
        double z = -Math.cos(theta)*radius;
        double x = Math.sin(theta)*radius;
        head = new Vector3D(x+pointOfContact.x, head.y, z+pointOfContact.z);
    }

    public Vector3D getPointOfContact() {
        return pointOfContact;
    }

    public Vector3D getHead() {
        return head;
    }

    public double getAngleBtwnUnstable(){
        double absVal = unstableEq.absAngleBetween(head);

        if(head.x > unstableEq.x)
            return absVal;
        else
            return -absVal;
    }

    public double getAngleBtwnStable(){
        double absVal = stableEq.absAngleBetween(head);

        if(head.x > stableEq.x)
            return absVal;
        else
            return -absVal;
    }

    public void resetToPos(Vector3D pos){
        this.head = pos;
        this.angularAccel = 0.0;
        this.angularVel = 0.0;
    }

    public PID getPID(){
        return pid;
    }

}
