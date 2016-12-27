/**
 * A point in R^3
 * 
 * @author serena
 * @since 11/28/16
 */
public class Vector3D {
	public final double x, y, z; // Cartesian components
	
	
	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	/**
	 * @return the dot-product of <code>this</code> and <code>other</code>
	 */
	public double dot(Vector3D other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}
	
	
	/**
	 * @return the length of <code>this</code>
	 */
	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	
	/**
	 * @return the difference between <code>this</code> and <code>other</code>
	 */
	public Vector3D subtract(Vector3D other) {
		return new Vector3D(x - other.x, y - other.y, z - other.z);
	}
	
	
	/**
	 * @return the sum of <code>this</code> and <code>other</code>
	 */
	public Vector3D add(Vector3D other) {
		return new Vector3D(x + other.x, y + other.y, z + other.z);
	}
	
	
	/**
	 * @return a <code>Vector</code> with magnitude 1 in the direction of <code>this</code>
	 */
	public Vector3D unitVector() {
		double mag = magnitude();
		return new Vector3D(x / mag, y / mag, z / mag);
	}
	
	
	/**
	 * @return the angular distance between <code>this</code> and <code>other</code>
	 */
	public double absAngleBetween(Vector3D other) {
		double absAngle = Math.acos(dot(other) / (magnitude() * other.magnitude()));
		
		return absAngle;
	}
	
	
	@Override
	public String toString() {
		return String.format("%.2f, %.2f, %.2f", x, y, z);
	}
}
