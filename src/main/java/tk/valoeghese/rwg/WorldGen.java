package tk.valoeghese.rwg;

import java.util.Random;

public class WorldGen {
	public WorldGen(long seed) {
		Random random = new Random(seed);
		this.largeScaleNoise = new Noise(random);
		this.detailNoise = new Noise(random);
		this.regions = new Voronoi(random.nextLong(), 0.3);

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
	private final LinearSpline oceanicBorders, intraCoastalBorders, internalBorders; // Coastal-Coastal (trench) vs Land-Coastal (river) vs Land-Land (mtn).
	// Land vs Coast would be decided on a per axis border but I am doing it per point for now

	public double[] sampleRaw(double x, double y) {
		Voronoi.MultiD1D2Result border = this.regions.sampleD1D2Worley(x * REGION_SCALE, y * REGION_SCALE);
		int regionX = border.closestGridX();
		int regionY = border.closestGridY();
		Point regionCentre = this.regions.sampleGrid(regionX, regionY);

		return new double[] {border.value(), regionCentre.getValue() & 1};
	}

	// returns value which is just (-1.0, 1.0) -> (ocean,land). Also markers for mountains maybe
	public double sample(double x, double y) {
		Voronoi.MultiD1D2Result border = this.regions.sampleD1D2Worley(x * REGION_SCALE, y * REGION_SCALE);
		double worley = border.value();
		//if (true) return worley;
//		if (worley > largest) {
//			largest = worley;
//			System.out.println(worley); 1.0112717951763213 was the largest the one time I tested
//		}

		int regionX = border.closestGridX();
		int regionY = border.closestGridY();

		Point regionCentre = this.regions.sampleGrid(regionX, regionY);
		int regionType = regionCentre.getValue() & 1;

		double regionHeightAdjustment = 0;
		double totalWeight = 0;

		for (int dx = -1; dx <= 1; dx++) {
			int sampleX = regionX + dx;

			for (int dy = -1; dy <= 1; dy++) {
				Point pt = this.regions.sampleGrid(sampleX, regionY + dy);
				double invDist = Math.min(1000.0, regionCentre.fastInvDist(pt)); // if the invdists get too high (as dist approaches 0) it will screw with precision
				int ptType = pt.getValue() & 1;

				totalWeight += invDist;

				if (ptType == 1 && regionType == 1) { // land land
					regionHeightAdjustment += invDist * this.internalBorders.sample(worley);
				}
				else if (ptType == 0 && regionType == 0) { // coast coast
					regionHeightAdjustment += invDist * this.oceanicBorders.sample(worley);
				}
				else { // land coast
					regionHeightAdjustment += invDist * this.intraCoastalBorders.sample(worley);
				}
			}
		}

		return regionHeightAdjustment / totalWeight;
	}

	private static final double REGION_SCALE = 1.0 / 3600.0;
}
