/**
 * Created by serena on 11/28/16.
 */
public class InvertedPendulum
{
   private Point pointOfContact, head;
   private final double mass; //kg
   private final double radius; //m

   private double ax, ay, az;

   public InvertedPendulum(double radius){
      this.pointOfContact = new Point(0,0,0);
      this.head = new Point(0,0,radius);
      this.mass = 1;
      this.radius = radius;
   }

   public InvertedPendulum(double x, double y, double z){
      this.pointOfContact = new Point(0,0,0);
      this.head = new Point(x,y,z);
      this.mass = 1;
      this.radius = Math.sqrt(x*x + y*y + z*z);
   }

   public void computeAccelerations(){
      Point rhat = new Point(head.x - pointOfContact.x,
                              head.y - pointOfContact.y,
                              head.z - pointOfContact.z);
      Point gravity = new Point(0,0,-9.8*mass);
      ax = gravity.dot(new Point(rhat.z, rhat.y, rhat.x));
      ay = gravity.dot(new Point(rhat.z, rhat.z, rhat.y));
      az = gravity.dot(rhat);
   }
}
