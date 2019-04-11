package homework10;

import java.io.IOException;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.streams.impl.LazyWitnessStream;

public class Exercise58 {
	public static void main(String[] args) {
		int d = 6;
		int n = 1000;
		int numLandmarkPoints = 50;
		double maxDistance = 0.1;

		double[][] points = PointCloudExamples.getRandomSpherePoints(n, d);
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		LandmarkSelector<double[]> landmarkSelector = new MaxMinLandmarkSelector<double[]>(metricSpace, numLandmarkPoints);
		
		LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(metricSpace, landmarkSelector, d + 1, maxDistance, 20);
		stream.finalizeStream();
		
		System.out.println("Number of simpleces in complex: " + stream.getSize());
		
		AbstractPersistenceAlgorithm<Simplex> algorithm = Plex4.getDefaultSimplicialAlgorithm(d + 1);
		
		BarcodeCollection<Double> intervals = algorithm.computeIntervals(stream);
		System.out.println("\nBarcodes for " + d + "-sphere:");
		System.out.println(intervals);
		try {
			Plex4.createBarcodePlot(intervals, "", 0.5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
