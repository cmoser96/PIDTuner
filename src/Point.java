/**
 * Created by serena on 11/28/16.
 */
public class Point
{
   public final double x, y, z;

   public Point(double x, double y, double z){
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public double dot(Point other){
      return this.x*other.x + this.y*other.y + this.z*other.z;
   }
}
