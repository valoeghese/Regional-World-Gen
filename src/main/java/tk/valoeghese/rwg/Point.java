package tk.valoeghese.rwg;

import java.util.OptionalInt;

public class Point {
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		this.value = this.hashCode() * 29 + Double.hashCode(this.x);
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
}