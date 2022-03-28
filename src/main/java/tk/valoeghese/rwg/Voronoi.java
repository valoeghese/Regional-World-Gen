package tk.valoeghese.rwg;

public final class Voronoi {
	public Voronoi(long seed) {
		this.seed = seed;
	}

	private final long seed;

	public Point sampleVoronoiGrid(int x, int y, double relaxation) {
		double unrelaxation = 1.0f - relaxation;
		double vx = x + relaxation * 0.5f + unrelaxation * this.randomDouble(x, y, 0);
		double vy = y + relaxation * 0.5f + unrelaxation * this.randomDouble(x, y, 1);
		return new Point(vx, vy);
	}

	public Point sampleVoronoi(double x, double y, double relaxation) {
		double unrelaxation = 1.0f - relaxation;

		final int baseX = (int) Math.floor(x);
		final int baseY = (int) Math.floor(y);
		double rx = 0;
		double ry = 0;
		double rdist = 1000;

		for (int xo = -1; xo <= 1; ++xo) {
			int gridX = baseX + xo;

			for (int yo = -1; yo <= 1; ++yo) {
				int gridY = baseY + yo;

				double vx = gridX + relaxation * 0.5f + unrelaxation * this.randomDouble(gridX, gridY, 0);
				double vy = gridY + relaxation * 0.5f + unrelaxation * this.randomDouble(gridX, gridY, 1);
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

	public double sampleD1D2Worley(double x, double y) {
		final int baseX = (int) Math.floor(x);
		final int baseY = (int) Math.floor(y);
		double rdist2 = 1000;
		double rdist = 1000;

		for (int xo = -1; xo <= 1; ++xo) {
			int gridX = baseX + xo;

			for (int yo = -1; yo <= 1; ++yo) {
				int gridY = baseY + yo;

				double vx = gridX + this.randomDouble(gridX, gridY, 0);
				double vy = gridY + this.randomDouble(gridX, gridY, 1);
				double vdist = squaredDist(x, y, vx, vy);

				if (vdist < rdist) {
					rdist2 = rdist;
					rdist = vdist;
				} else if (vdist < rdist2) {
					rdist2 = vdist;
				}
			}
		}

		return Math.sqrt(rdist2) - Math.sqrt(rdist);
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
}