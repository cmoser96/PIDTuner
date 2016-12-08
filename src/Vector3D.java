/**
 * Created by serena on 11/28/16.
 */
public class Vector3D {
    public final double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double dot(Vector3D other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3D subtract(Vector3D other) {
        return new Vector3D(x - other.x, y - other.y, z - other.z);
    }

    public Vector3D add(Vector3D other) {
        return new Vector3D(x + other.x, y + other.y, z + other.z);
    }

    public Vector3D unitVector() {
        double mag = magnitude();
        return new Vector3D(x / mag, y / mag, z / mag);
    }

    public double absAngleBetween(Vector3D other){
        double absAngle = Math.acos(dot(other) / (magnitude() * other.magnitude()));

        return absAngle;
    }

    @Override
    public String toString() {
        return String.format("%.2f, %.2f, %.2f", x, y, z);
    }
}
