package tk.valoeghese.rwg;

public class Point {
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		this.value = this.hashCode() * 29 + Double.hashCode(this.x);
	}

	private Point(double x, double y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}

	private final double x;
	private final double y;
	private final int value;

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public int getValue() {
		return this.value;
	}

	public Point add(double x, double y) {
		return new Point(this.x + x, this.y + y);
	}

	public Point add(Point other) {
		return this.add(other.x, other.y);
	}

	public double squaredDist(Point other) {
		return this.squaredDist(other.x, other.y);
	}

	public double squaredDist(double x, double y) {
		double dx = Math.abs(x - this.x);
		double dy = Math.abs(y - this.y);
		return dx * dx + dy * dy;
	}

	public double distance(double x, double y) {
		return Math.sqrt(this.squaredDist(x, y));
	}

	public double fastInvDist(Point other) {
		return fastInvSqrt(this.squaredDist(other.x, other.y));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null) {
			return false;
		} else if (o instanceof Point) {
			Point vec2f = (Point) o;
			return vec2f.x == this.x && vec2f.y == this.y;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = 7;
		result = 29 * result + Double.hashCode(this.x);
		result = 29 * result + Double.hashCode(this.y);
		return result;
	}

	@Override
	public String toString() {
		return "(" + this.x
				+ ", " + this.y
				+ ')';
	}

	/**
	 * Creates a point at the given position, and with no other associated information.
	 * @param x the x position.
	 * @param y the y position.
	 * @return the created point object.
	 */
	public static Point onlyAt(double x, double y) {
		return new Point(x, y, 0);
	}

	// also 1/0 = 2.1611214334330826E154 according to fastInvSqrt ;)
	private static double fastInvSqrt(double x) { // https://stackoverflow.com/questions/11513344/how-to-implement-the-fast-inverse-square-root-in-java
		double xhalf = 0.5 * x;
		long i = Double.doubleToLongBits(x);
		i = 0x5fe6ec85e7de30daL - (i >> 1);
		x = Double.longBitsToDouble(i);
		x *= (1.5 - xhalf * x * x);
		x *= (1.5 - xhalf * x * x); // second iteration of newton's method
		return x;
	}
}