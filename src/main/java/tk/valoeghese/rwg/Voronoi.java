package tk.valoeghese.rwg;

public final class Voronoi {
	public Voronoi(long seed, double relaxation) {
		this.seed = seed;
		this.relaxation = relaxation;
	}

	private final long seed;
	private final double relaxation;

	public Point sampleGrid(int x, int y) {
		double unrelaxation = 1.0 - this.relaxation;
		double vx = x + this.relaxation * 0.5 + unrelaxation * this.randomDouble(x, y, 0);
		double vy = y + this.relaxation * 0.5 + unrelaxation * this.randomDouble(x, y, 1);
		return new Point(vx, vy);
	}

	public Point sample(double x, double y) {
		double unrelaxation = 1.0 - this.relaxation;

		final int baseX = (int) Math.floor(x);
		final int baseY = (int) Math.floor(y);
		double rx = 0;
		double ry = 0;
		double rdist = 1000;

		for (int xo = -2; xo <= 2; ++xo) {
			int gridX = baseX + xo;

			for (int yo = -2; yo <= 2; ++yo) {
				if (xo * xo == 4 && yo * yo == 4) continue; // shave off corners

				int gridY = baseY + yo;

				double vx = gridX + this.relaxation * 0.5 + unrelaxation * this.randomDouble(gridX, gridY, 0);
				double vy = gridY + this.relaxation * 0.5 + unrelaxation * this.randomDouble(gridX, gridY, 1);
				double vdist = squaredDist(x, y, vx, vy);

				if (vdist < rdist) {
					rx = vx;
					ry = vy;
					rdist = vdist;
				}
			}
		}

		return new Point(rx, ry);
	}

	public MultiD1D2Result sampleD1D2Worley(double x, double y) {
		double unrelaxation = 1.0 - this.relaxation;

		final int baseX = (int) Math.floor(x);
		final int baseY = (int) Math.floor(y);
		double rdist2 = 1000;
		double rdist = 1000;
		// closest point's grid X and Y
		int closestGridX = 0;
		int closestGridY = 0;

		for (int xo = -2; xo <= 2; ++xo) {
			int gridX = baseX + xo;

			for (int yo = -2; yo <= 2; ++yo) {
				if (xo * xo == 4 && yo * yo == 4) continue; // shave off corners

				int gridY = baseY + yo;

				double vx = gridX + this.relaxation * 0.5 + unrelaxation * this.randomDouble(gridX, gridY, 0);
				double vy = gridY + this.relaxation * 0.5 + unrelaxation * this.randomDouble(gridX, gridY, 1);
				double vdist = squaredDist(x, y, vx, vy);

				if (vdist < rdist) {
					rdist2 = rdist;
					rdist = vdist;
					closestGridX = gridX;
					closestGridY = gridY;
				} else if (vdist < rdist2) {
					rdist2 = vdist;
				}
			}
		}

		return new MultiD1D2Result(Math.sqrt(rdist2) - Math.sqrt(rdist), closestGridX, closestGridY);
	}

	private int randomInt(int x, int y, long salt) {
		long v = this.seed;

		v *= 6364136223846793005L * v + 1442695040888963407L;
		v += x + salt;
		v *= 6364136223846793005L * v + 1442695040888963407L;
		v += y + salt;
		v *= 6364136223846793005L * v + 1442695040888963407L;
		v += x;
		v *= 6364136223846793005L * v + 1442695040888963407L;
		v += y;

		return (int) (v & 0xFFFFFFFFL); // 32 bits
	}

	private double randomDouble(int x, int y, long salt) {
		return (double) this.randomInt(x, y, salt) * DOUBLE_UNIT;
	}

	private static final double DOUBLE_UNIT = 1.0 / (double) Integer.MAX_VALUE;

	private static double squaredDist(double x0, double y0, double x1, double y1) {
		double dx = x1 - x0;
		double dy = y1 - y0;
		return dx * dx + dy * dy;
	}

	public record MultiD1D2Result(double value, int closestGridX, int closestGridY) {}
}