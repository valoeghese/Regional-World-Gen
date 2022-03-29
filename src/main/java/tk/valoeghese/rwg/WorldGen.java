package tk.valoeghese.rwg;

import java.util.Random;

public class WorldGen {
	public WorldGen(long seed) {
		Random random = new Random(seed);
		this.largeScaleNoise = new Noise(random);
		this.detailNoise = new Noise(random);
		this.regions = new Voronoi(random.nextLong());

		this.oceanicBorders = new LinearSpline()
				.point(0, -3.0)
				.point(0.4, 0.0)
				.point(1.6, 1.0);

		this.intraCoastalBorders = new LinearSpline(); // 0

		this.internalBorders = new LinearSpline()
				.point(0, 6.0)
				.point(0.2, 2.5)
				.point(0.4, 1.2)
				.point(1.0, 1.0);

		// I was originally planning to use d1-d2 worley, but I will need to blend different border blends.
		// Therefore, I might perform a weighted average of blends with all surrounding points (3x3 + 4extremes) on the jittered grid
	}

	private final Noise largeScaleNoise, detailNoise;
	private final Voronoi regions;
	private final LinearSpline oceanicBorders, intraCoastalBorders, internalBorders; // Coastal-Coastal (trench) vs Land-Coastal (river) vs Land-Land (mtn). Land vs Coast is decided on a per axis border

	// returns value which is just (-1.0, 1.0) -> (ocean,land). Also markers for mountains maybe
	public double sample(double x, double y) {
		// TODO stuff
		return 0;
	}
}
