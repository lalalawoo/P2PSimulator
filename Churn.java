package P2PSimulator;

import java.util.ArrayList;
import java.util.Random;

public class Churn{
	
	private static double mChurnFluctuation = 10;
	private static int mInitChurn = 100;
	
	public static int generate(ArrayList<Integer> leavingChurns){
		Random r = new Random();
		int lastChurn = leavingChurns.size()==0? mInitChurn :leavingChurns.get(leavingChurns.size()-1);
		return (int)(r.nextGaussian()*mChurnFluctuation)+lastChurn;		
	}	
}
