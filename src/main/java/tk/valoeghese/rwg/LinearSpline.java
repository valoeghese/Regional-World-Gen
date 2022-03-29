package tk.valoeghese.rwg;

import java.util.LinkedList;
import java.util.List;

public class LinearSpline {
	public LinearSpline() {
		this.entries = new LinkedList<>();
	}

	private final List<Point> entries;

	public LinearSpline point(double x, double y) {
		this.entries.add(Point.onlyAt(x, y));
		return this;
	}

	/**
	 * It is recommended to avoid calling this method where the points can be added in order of increasing x value instead.
	 */
	public void sort() {
		this.entries.sort((p0, p1) -> (int) Math.signum(p0.getX() - p1.getX()));
	}

	public int size() {
		return this.entries.size();
	}

	public double sample(double x) {
		if (this.entries.isEmpty()) return 0; // default value if no points is 0

		Point previous = null;

		for (Point next : this.entries) {
			if (x < next.getX()) {
				if (previous == null) return next.getY(); // at the very beginning, clamp to next point's y
				else return map(x, previous.getX(), next.getX(), previous.getY(), next.getY()); // line
			}

			previous = next;
		}

		return previous.getY(); // if passes through all points clamp to final point's y.
	}

	private static final double map(double value, double min, double max, double newmin, double newmax) {
		double prog = (value - min) / (max - min);
		return newmin + prog * (newmax - newmin);
	}
}
