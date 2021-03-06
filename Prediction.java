package P2PSimulator;

import java.util.ArrayList;
import java.util.List;

public class Prediction{
	private static double k1 = 1.0;
	private static double k2 = 0.05;
	private static double k3 = 0.02;
	
	public double sma(ArrayList<Integer> churns, ArrayList<Double> smas, int OL){
		double sma = 0;
		for(int i=0;i<OL;i++){
				sma+=churns.get(churns.size()-1-i);
		}
		return (double)sma/OL;		
	}

	public double ema(ArrayList<Integer> churns, ArrayList<Double> emas, int OL){
		double alpha = 2.0/(OL+1);
		final int lastObservation = churns.get(churns.size() - 1);
		final double lastEMA = emas.get(emas.size() - 1);
		// EMA today = EMA_yesterday + alpha * (value_today - EMA_yesterday)
		return lastEMA + (alpha * (lastObservation - lastEMA));	
	}
	
	public double dema(ArrayList<Integer> churns, ArrayList<Double> demas, int OL){
		double alpha = bestSmoothingFactor(churns.subList(churns.size()-OL, churns.size()), demas.subList(demas.size()-OL, demas.size()));
		final int lastObservation = churns.get(churns.size() - 1);
		final double lastEMA = demas.get(demas.size() - 1);
		// EMA today = EMA_yesterday + alpha * (value_today - EMA_yesterday)
		return lastEMA + (alpha * (lastObservation - lastEMA));
	}
	
	public double bestSmoothingFactor(List<Integer> x, List<Double> y) {
		final int size = x.size();
		double max = 0;
		int interval = size;
		//make sure we have more than 2 points, otherwise we will have a perfect linear regression
		for (int i = size; i >= 3; i--) {
			double r2 = linearRegression(x, y, i);
			if (r2 >= max) {
				max = r2;
				interval = i;
			}
		}
		return 2.0 / (interval + 1);
	}	
	
	public double linearRegression(final List<Integer> x, final List<Double> y, final int n) {
		// first pass: read in data, compute xbar and ybar
		double sumx = 0.0, sumy = 0.0;
		for (int i = n - 1; i >= 0; i--) {
			sumx += x.get(i);
			sumy += y.get(i);
		}
		double xbar = sumx / n;
		double ybar = sumy / n;

		// second pass: compute summary statistics
		double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
		for (int i = n - 1; i >= 0; i--) {
			xxbar += (x.get(i) - xbar) * (x.get(i) - xbar);
			yybar += (y.get(i) - ybar) * (y.get(i) - ybar);
			xybar += (x.get(i) - xbar) * (y.get(i) - ybar);
		}
		double beta1 = xybar / xxbar;
		double beta0 = ybar - beta1 * xbar;

		// analyze results
		double ssr = 0.0; // regression sum of squares
		for (int i = n - 1; i >= 0; i--) {
			double fit = beta1 * x.get(i) + beta0;
			ssr += (fit - ybar) * (fit - ybar);
		}
		double r2 = ssr / yybar;
		return r2;
	}
	
	public double pidfe(ArrayList<Integer> churns, ArrayList<Double> pidfes, int OL){

		final int lastObservation = churns.get(churns.size() - 1);
		final double lastPIDFE = pidfes.get(pidfes.size() - 1);
		
		// averaging part
		double avg = 0;
		for(int i=0;i<OL;i++){
				avg+=churns.get(churns.size()-1-i);
		}
		avg = (double)avg/OL;
		
		// linear part
		double delta = (lastObservation - lastPIDFE);
		double alpha = 2.0/(OL+1);
		
		// response part
		double re = (delta - (churns.get(churns.size()-2) - pidfes.get(pidfes.size()-2)));		
		
		return k1 * avg + alpha * delta + k3 * re;
	}
}
