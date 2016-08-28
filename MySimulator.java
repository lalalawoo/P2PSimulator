package P2PSimulator;

import java.util.ArrayList;

public class MySimulator{
	private static int mDataPoints = 100;
	private static int[] mOLs = {5,10,15,20,30,40,50,60};
	private static double mChurnRate = 0.9;
	private static int peerSize = 1000;
		
	public static void main(String[] args){
		Prediction pred = new Prediction();
		int count = 0;
		ArrayList<Integer> leavingChurns = new ArrayList<>();
		ArrayList<ArrayList<Double>> smas = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> emas = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> demas = new ArrayList<ArrayList<Double>>();
//		ArrayList<Integer> predRFs = new ArrayList<>();
//		ArrayList<Integer> RFs = new ArrayList<>();
		while(count<mDataPoints){
			int leave = Churn.generate(peerSize,mChurnRate);
			int join = Churn.generate(peerSize,mChurnRate);
			leavingChurns.add(leave);
			peerSize = peerSize-leave+join;
			System.out.println("churn"+count+": "+leave+" peerSize: "+peerSize);
			for(int i=0;i<mOLs.length;i++){
				int OL = mOLs[i];
				if(smas.size()<=i){
					smas.add(new ArrayList<Double>());
					emas.add(new ArrayList<Double>());
//					demas.add(new ArrayList<Double>());
				}
				if(leavingChurns.size()<OL){
					smas.get(i).add(Double.valueOf(0));
					continue;
				}
				ArrayList<Double> currSmas = smas.get(i);
				ArrayList<Double> currEmas = emas.get(i);
//				ArrayList<Double> currDemas = emas.get(i);
				double predSma = pred.sma(leavingChurns,currSmas,OL);
				smas.get(i).add(predSma);
				double predEma = pred.ema(leavingChurns,currEmas,OL);
//				double predDema = pred.dema(churns,currDemas,OL);
				System.out.println("prediction"+(count+1)+" - sma:"+predSma+" ema:"+predEma);
				count++;
			}
			ReplicationFactor
			
			Replication(peers)
		}
	}
}