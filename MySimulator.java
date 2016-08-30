package P2PSimulator;

import java.util.ArrayList;

public class MySimulator{
	private static int mDataPoints = 500;
	private static int[] mOLs = {5,10,15,20,30,40,50,60};
	private static int peerSize = 500;
	private static int mChurnFluctuation = 20;
	private static int mInitChurn = 100;
		
	public static void main(String[] args){
		
		Prediction pred = new Prediction();
		Churn c = new Churn(mChurnFluctuation);
		int lastChurnSize = mInitChurn;
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
		int[] win_sma = new int[mOLs.length];
		int[] win_ema = new int[mOLs.length];
		int[] win_dema = new int[mOLs.length];
		while(count<mDataPoints){

			int leave = c.generate(lastChurnSize);
			int join = c.generate(lastChurnSize);
			leavingChurns.add(leave);
			lastChurnSize = leave;
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
				if(leavingChurns.size()<=OL){
					smas.get(i).add(Double.valueOf(0));
					emas.get(i).add(Double.valueOf(0));
					demas.get(i).add(Double.valueOf(0));
					continue;
				}
				ArrayList<Double> currSmas = smas.get(i);
				ArrayList<Double> currEmas = emas.get(i);
				ArrayList<Double> currDemas = demas.get(i);
				
				// compare previous predicted value with current churn number
				if(count>=OL){
					if(Math.abs(currSmas.get(currSmas.size()-1)-leave)<5) win_sma[i]++;
					if(Math.abs(currEmas.get(currEmas.size()-1)-leave)<5) win_ema[i]++;
					if(Math.abs(currDemas.get(currDemas.size()-1)-leave)<5) win_dema[i]++;
				}
				
				double predSma = pred.sma(leavingChurns,currSmas,OL);
				smas.get(i).add(predSma);
				double predEma = pred.ema(leavingChurns,currEmas,OL);
				emas.get(i).add(predEma);
				double predDema = pred.dema(leavingChurns,currDemas,OL);
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
				predRF_ema[i]= Replication.replicationFactor((int)predEma, peerSize);
				predRF_dema[i]= Replication.replicationFactor((int)predDema, peerSize);
			}
			count++;
		}
		FileUtil.createCSVFile("churns", leavingChurns);
		System.out.println("Churn data set created.");
		FileUtil.createCSVFile(smas, "smas");
		System.out.println("Predictions using SMA created.");
		FileUtil.createCSVFile(emas, "emas");
		System.out.println("Predictions using EMA created.");
		FileUtil.createCSVFile(demas, "demas");
		System.out.println("Predictions using DEMA created.");
		
		ArrayList<ArrayList<Double>> winPercent = new ArrayList<ArrayList<Double>>();
		winPercent.add(dataProcessHelper(win_sma));
		winPercent.add(dataProcessHelper(win_ema));
		winPercent.add(dataProcessHelper(win_dema));		
		FileUtil.createCSVFile(winPercent, "winPercent");
		System.out.println("Win percentage has been calculated.");
		
		ArrayList<ArrayList<Double>> rfAccuracy = new ArrayList<ArrayList<Double>>();
		rfAccuracy.add(dataProcessHelper(validCount_sma));
		rfAccuracy.add(dataProcessHelper(validCount_ema));
		rfAccuracy.add(dataProcessHelper(validCount_dema));
		FileUtil.createCSVFile(rfAccuracy, "rfAccuracy");
		System.out.println("RF accuracy has been calculated.");		
	}
	
	public static ArrayList<Double> dataProcessHelper(int[] arr){
		ArrayList<Double> res = new ArrayList<Double>();
		for(int i=0;i<mOLs.length;i++){
			res.add((double)arr[i]/mDataPoints);
		}
		return res;
	}
	
}
