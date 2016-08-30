package P2PSimulator;

import java.util.ArrayList;

public class MySimulator{
	private static int mDataPoints = 500;
	private static int[] mOLs = {5,10,15,20,30,40,50,60};
	private static int peerSize = 500;
		
	public static void main(String[] args){
		
		Prediction pred = new Prediction();
		int count = 0;
		ArrayList<Integer> leavingChurns = new ArrayList<>();
		ArrayList<ArrayList<Double>> smas = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> emas = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> demas = new ArrayList<ArrayList<Double>>();
		int[] predRF_sma = new int[mOLs.length];
		int[] predRF_ema = new int[mOLs.length];
		int[] predRF_dema = new int[mOLs.length];
		int[] validCount_sma = new int[mOLs.length];
		int[] validCount_ema = new int[mOLs.length];
		int[] validCount_dema = new int[mOLs.length];
		while(count<mDataPoints){
			int leave = Churn.generate(leavingChurns);
			int join = Churn.generate(leavingChurns);
			leavingChurns.add(leave);
			peerSize = peerSize-leave+join;
			
			int RF = Replication.replicationFactor(leave, peerSize);
//			System.out.println(RF);
//			System.out.println("churn"+count+" - "+"leave:"+leave+" join:"+join+" peerSize: "+peerSize);
			for(int i=0;i<mOLs.length;i++){
				int OL = mOLs[i];
				if(smas.size()<=i){
					smas.add(new ArrayList<Double>());
					emas.add(new ArrayList<Double>());
					demas.add(new ArrayList<Double>());
				}
				if(leavingChurns.size()<OL){
					smas.get(i).add(Double.valueOf(0));
					emas.get(i).add(Double.valueOf(0));
					demas.get(i).add(Double.valueOf(0));
					continue;
				}
				ArrayList<Double> currSmas = smas.get(i);
				ArrayList<Double> currEmas = emas.get(i);
				ArrayList<Double> currDemas = emas.get(i);
				double predSma = pred.sma(leavingChurns,currSmas,OL);
				smas.get(i).add(predSma);
				double predEma = pred.ema(leavingChurns,currEmas,OL);
				emas.get(i).add(predEma);
				double predDema = pred.dema(leavingChurns,currDemas);
				demas.get(i).add(predDema);
//				System.out.println("OL:"+OL+" prediction"+(count+1)+" - sma:"+predSma+" ema:"+predEma+" dema:"+predDema);
				
				// compare previous predicted RF then calculate new 
				if(count>=OL){
					if(predRF_sma[i]>=RF && predRF_sma[i]<=RF+3) validCount_sma[i]++;
					if(predRF_ema[i]>=RF && predRF_ema[i]<=RF+3) validCount_ema[i]++;
					if(predRF_dema[i]>=RF && predRF_dema[i]<=RF+3) validCount_dema[i]++;
//					System.out.println("OL:"+OL+"RF_sma:"+validCount_sma[i]+" RF_ema:"+validCount_ema[i]+" RF_dema:"+validCount_dema[i]);
				}				
				predRF_sma[i]= Replication.replicationFactor((int)predSma, peerSize);
				predRF_ema[i]= Replication.replicationFactor((int)predSma, peerSize);
				predRF_dema[i]= Replication.replicationFactor((int)predSma, peerSize);
			}
			count++;
		}
		FileUtil.createCSVFile("churns", leavingChurns);
		FileUtil.createCSVFile(smas, "smas");
		FileUtil.createCSVFile(emas, "emas");
		FileUtil.createCSVFile(demas, "demas");
		
		ArrayList<ArrayList<Double>> rfAccuracy = new ArrayList<ArrayList<Double>>();
		for(int k=0;k<3;k++){
			ArrayList<Double> temp = new ArrayList<Double>();
			for(int i=0;i<mOLs.length;i++){
				temp.add((double)validCount_sma[i]/mDataPoints);
			}
			rfAccuracy.add(temp);
		}
		FileUtil.createCSVFile(rfAccuracy, "rfAccuracy");		
	}	
	
}
