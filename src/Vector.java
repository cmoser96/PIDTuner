/**
 * Created by serena on 11/28/16.
 */
public class Vector {
    public final double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double dot(Vector other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector subtract(Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    public Vector unitVector() {
        double mag = magnitude();
        return new Vector(x / mag, y / mag, z / mag);
    }

    public double absAngleBetween(Vector other){
        double absAngle = Math.acos(dot(other) / (magnitude() * other.magnitude()));

        return absAngle;
    }

    @Override
    public String toString() {
        return String.format("%.2f, %.2f, %.2f", x, y, z);
    }
}
